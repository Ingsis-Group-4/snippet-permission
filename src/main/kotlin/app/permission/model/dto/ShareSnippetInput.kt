package app.permission.model.dto

data class ShareSnippetInput(
    val snippetKey: String,
    val userIds: List<String>,
)
