package app.permission.persistance.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
data class Snippet(
    val name: String,
    @Column(unique = true)
    val snippetKey: String,
    val userId: String,
    @OneToMany(mappedBy = "snippet")
    var shares: List<SnippetShare> = listOf(),
) : BaseEntity()
