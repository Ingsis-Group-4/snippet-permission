package app.permission.service

import app.permission.exception.PermissionTypeNotFound
import app.permission.exception.UserAlreadyHasPermission
import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.PermissionOutput
import app.permission.persistance.entity.Permission
import app.permission.persistance.repository.PermissionRepository
import app.permission.persistance.repository.PermissionTypeRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService
    @Autowired
    constructor(
        private val permissionRepository: PermissionRepository,
        private val permissionTypeRepository: PermissionTypeRepository,
    ) {
        fun createPermission(input: CreateSnippetInput) {
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

        fun getAllUserPermissions(userId: String): List<PermissionOutput> {
            val permissionEntities = permissionRepository.findAllByUserId(userId)
            return permissionEntities.map { PermissionOutput(it.id!!, it.snippetId, it.userId, it.permissionType.type) }
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
    }
