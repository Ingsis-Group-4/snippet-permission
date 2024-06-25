package app.permission.service

import app.permission.exception.PermissionTypeNotFound
import app.permission.exception.UserAlreadyHasPermission
import app.permission.model.dto.CreatePermissionInput
import app.permission.model.dto.PermissionOutput
import app.permission.persistance.entity.Permission
import app.permission.persistance.repository.PermissionRepository
import app.permission.persistance.repository.PermissionTypeRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService
    @Autowired
    constructor(
        private val permissionRepository: PermissionRepository,
        private val permissionTypeRepository: PermissionTypeRepository,
    ) {
        private val logger = LoggerFactory.getLogger(PermissionService::class.java)

        fun createPermission(input: CreatePermissionInput) {
            logger.info(
                "Request received for creating permission for user with id: ${input.userId} and snippet with id: ${input.snippetId}",
            )
            if (hasPermissionForSnippet(input.userId!!, input.snippetId!!)) {
                logger.error("User with id: ${input.userId} already has permission for snippet with id: ${input.snippetId}")
                throw UserAlreadyHasPermission()
            }

            val type = permissionTypeRepository.findByType(input.permissionType)

            if (type.isEmpty) {
                logger.error("Permission type with type: ${input.permissionType} not found")
                throw PermissionTypeNotFound()
            }
            permissionRepository.save(
                Permission(
                    input.snippetId,
                    input.userId,
                    type.get(),
                ),
            )
            logger.info("Permission created for user with id: ${input.userId} and snippet with id: ${input.snippetId}")
        }

        fun getAllUserPermissions(userId: String): List<PermissionOutput> {
            logger.info("Request received for getting all permissions for user with id: $userId")
            val permissionEntities = permissionRepository.findAllByUserId(userId)

            val authorType = permissionTypeRepository.findByType("OWNER").get()

            logger.info("Returning all permissions for user with id: $userId")
            return permissionEntities.map {
                val authorPermission =
                    this.permissionRepository.getByPermissionType_TypeAndSnippetId(authorType.type, it.snippetId)
                PermissionOutput(it.id!!, it.snippetId, authorPermission.userId, it.permissionType.type)
            }
        }

        private fun hasPermissionForSnippet(
            userId: String,
            snippetId: String,
        ): Boolean {
            logger.info("Checking if user with id: $userId has permission for snippet with id: $snippetId")
            val permission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
            return !permission.isEmpty
        }

        @Transactional
        fun deleteAllPermissionsForSnippet(snippetId: String) {
            logger.info("Request received for deleting all permissions for snippet with id: $snippetId")
            permissionRepository.deleteAllBySnippetId(snippetId)
        }

        fun getAuthorFromSnippetId(snippetId: String): String {
            logger.info("Request received for getting author of snippet with id: $snippetId")
            val authorType = permissionTypeRepository.findByType("OWNER").get()
            val authorPermission = permissionRepository.getByPermissionType_TypeAndSnippetId(authorType.type, snippetId)
            return authorPermission.userId
        }
    }
