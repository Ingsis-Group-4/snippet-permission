package app.permission.model.dto

data class PermissionOutput(
    val id: String,
    val snippetId: String,
    val userId: String,
    val permissionType: String,
)
