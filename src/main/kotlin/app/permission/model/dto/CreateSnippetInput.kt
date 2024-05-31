package app.permission.model.dto

import jakarta.validation.constraints.NotBlank

data class CreateSnippetInput(
    @field:NotBlank(message = "Snippet id must not be blank")
    val snippetId: String?,
    @field:NotBlank(message = "User Id must not be blank")
    val userId: String?,
    @field:NotBlank(message = "Permission Type must not be blank")
    val permissionType: String,
)
