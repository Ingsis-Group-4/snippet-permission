package app.permission.persistance.repository

import app.permission.persistance.entity.Permission
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PermissionRepository : JpaRepository<Permission, String> {
    fun findAllByUserId(userId: String): List<Permission>

    fun findByUserIdAndSnippetId(
        userId: String,
        snippetId: String,
    ): Optional<Permission>
}