package app.permission.persistance.repository

import app.permission.persistance.entity.PermissionType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PermissionTypeRepository : JpaRepository<PermissionType, String> {
    fun findByType(type: String): Optional<PermissionType>
}
