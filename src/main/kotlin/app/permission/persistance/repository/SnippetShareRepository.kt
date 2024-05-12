package app.permission.persistance.repository

import app.permission.persistance.entity.Snippet
import app.permission.persistance.entity.SnippetShare
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SnippetShareRepository : JpaRepository<SnippetShare, String> {
    @Query(
        """
            select s from Snippet as s
            join SnippetShare as ss on s = ss.snippet
            where ss.userId = ?1
        """,
    )
    fun findAllSharedSnippetsByUserId(userId: String): List<Snippet>
}
