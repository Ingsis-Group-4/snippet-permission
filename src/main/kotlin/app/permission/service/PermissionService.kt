package app.permission.service

import app.permission.exception.PermissionTypeNotFound
import app.permission.exception.UserAlreadyHasPermission
import app.permission.model.dto.CreatePermissionInput
import app.permission.model.dto.PermissionListOutput
import app.permission.model.dto.PermissionOutput
import app.permission.persistance.entity.Permission
import app.permission.persistance.repository.PermissionRepository
import app.permission.persistance.repository.PermissionTypeRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PermissionService
    @Autowired
    constructor(
        private val permissionRepository: PermissionRepository,
        private val permissionTypeRepository: PermissionTypeRepository,
    ) {
        fun createPermission(input: CreatePermissionInput) {
            if (hasPermissionForSnippet(input.userId!!, input.snippetId!!)) {
                throw UserAlreadyHasPermission()
            }

            val type = permissionTypeRepository.findByType(input.permissionType)

            if (type.isEmpty) {
                throw PermissionTypeNotFound()
            }

            permissionRepository.save(
                Permission(
                    input.snippetId,
                    input.userId,
                    type.get(),
                ),
            )
        }

        fun getAllUserPermissions(
            userId: String,
            pageNum: Int,
            pageSize: Int,
        ): PermissionListOutput {
            val pagination = PageRequest.of(pageNum, pageSize)
            val permissionEntities = permissionRepository.findAllByUserId(userId, pagination)
            val userPermissionTotalCount = permissionRepository.countAllByUserId(userId)

            val authorType = permissionTypeRepository.findByType("OWNER").get()

            val permissionOutputs =
                permissionEntities.map {
                    val authorPermission =
                        this.permissionRepository.getByPermissionType_TypeAndSnippetId(authorType.type, it.snippetId)
                    PermissionOutput(it.id!!, it.snippetId, authorPermission.userId, it.permissionType.type)
                }

            return PermissionListOutput(permissionOutputs, userPermissionTotalCount)
        }

        private fun hasPermissionForSnippet(
            userId: String,
            snippetId: String,
        ): Boolean {
            val permission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
            return !permission.isEmpty
        }

        @Transactional
        fun deleteAllPermissionsForSnippet(snippetId: String) {
            permissionRepository.deleteAllBySnippetId(snippetId)
        }

        fun getAuthorFromSnippetId(snippetId: String): String {
            val authorType = permissionTypeRepository.findByType("OWNER").get()
            val authorPermission = permissionRepository.getByPermissionType_TypeAndSnippetId(authorType.type, snippetId)
            return authorPermission.userId
        }
    }
