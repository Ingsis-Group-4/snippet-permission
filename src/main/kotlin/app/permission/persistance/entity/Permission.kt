package app.permission.persistance.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class Permission(
    val snippetId: String,
    val userId: String,
    @ManyToOne
    @JoinColumn(name = "permission_type_id")
    val permissionType: PermissionType,
) : BaseEntity()
