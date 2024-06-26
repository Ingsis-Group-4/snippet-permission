package app.permission.model.dto

data class PermissionOutput(
    val id: String,
    val snippetId: String,
    val authorId: String,
    val permissionType: String,
)
