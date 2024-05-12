package app.permission.model.dto

data class CreateSnippetInput(
    val name: String,
    val snippetKey: String,
    val userId: String,
)
