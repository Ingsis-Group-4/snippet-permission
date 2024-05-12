package app.permission.service

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.ShareSnippetInput
import app.permission.persistance.entity.Snippet
import app.permission.persistance.entity.SnippetShare
import app.permission.persistance.repository.SnippetRepository
import app.permission.persistance.repository.SnippetShareRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService {
    @Autowired
    val snippetRepository: SnippetRepository? = null

    @Autowired
    val snippetShareRepository: SnippetShareRepository? = null

    fun createSnippet(input: CreateSnippetInput) {
        snippetRepository?.save(Snippet(input.name, input.snippetKey, input.userId))
    }

    fun shareSnippet(input: ShareSnippetInput) {
        val snippet = snippetRepository?.findBySnippetKey(input.snippetKey)
        val shares = input.userIds.stream().map { SnippetShare(it, snippet) }.toList()

        snippetShareRepository?.saveAll(shares)
    }
}
