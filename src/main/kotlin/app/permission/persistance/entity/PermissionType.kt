package app.permission.persistance.entity

import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
data class PermissionType(
    val type: String,
    @OneToMany(mappedBy = "permissionType")
    val permission: List<Permission>? = null,
) : BaseEntity()
