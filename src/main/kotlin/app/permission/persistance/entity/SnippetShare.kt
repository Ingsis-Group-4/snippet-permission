package app.permission.persistance.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class SnippetShare(
    val userId: String,
    @ManyToOne()
    @JoinColumn(name = "snippet_id", nullable = false)
    val snippet: Snippet? = null,
) : BaseEntity()
