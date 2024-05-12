package app.permission.persistance.repository

import app.permission.persistance.entity.Snippet
import org.springframework.data.jpa.repository.JpaRepository

interface SnippetRepository : JpaRepository<Snippet, String> {
    fun findBySnippetKey(snippetKey: String): Snippet?

    fun findAllByUserId(userId: String): List<Snippet>

    fun existsBySnippetKey(snippetKey: String): Boolean
}
