package app.permission.model.dto

import jakarta.validation.constraints.NotBlank

data class CreateSnippetInput(
    @field:NotBlank(message = "Name must not be blank")
    val name: String?,
    @field:NotBlank(message = "Snippet Key must not be blank")
    val snippetKey: String?,
    @field:NotBlank(message = "User Id must not be blank")
    val userId: String?,
)
