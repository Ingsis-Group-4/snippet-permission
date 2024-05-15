package app.permission.service

import app.permission.model.dto.CreateSnippetInput
import app.permission.model.dto.ShareSnippetInput
import app.permission.model.dto.SnippetOutput
import app.permission.model.enums.PermissionTypeInput
import app.permission.persistance.entity.Snippet
import app.permission.persistance.entity.SnippetShare
import app.permission.persistance.repository.SnippetRepository
import app.permission.persistance.repository.SnippetShareRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PermissionService(
    @Autowired val snippetRepository: SnippetRepository,
    @Autowired val snippetShareRepository: SnippetShareRepository,
) {
    fun createSnippet(input: CreateSnippetInput) {
        if (snippetKeyExists(input.snippetKey!!)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Snippet with snippet key ${input.snippetKey} already exists",
            )
        }
        snippetRepository.save(Snippet(input.name!!, input.snippetKey, input.userId!!))
    }

    fun shareSnippet(input: ShareSnippetInput) {
        val snippet = snippetRepository.findBySnippetKey(input.snippetKey!!)
        val shares = input.userIds.stream().map { SnippetShare(it, snippet) }.toList()

        snippetShareRepository.saveAll(shares)
    }

    fun getAllSnippets(
        userId: String,
        permissionTypeInput: PermissionTypeInput?,
    ): List<SnippetOutput> {
        val permissionType = permissionTypeInput ?: PermissionTypeInput.ALL

        val snippets: List<SnippetOutput> =
            when (permissionType) {
                PermissionTypeInput.OWNED -> {
                    getOwnedSnippets(userId)
                }

                PermissionTypeInput.SHARED -> {
                    getSharedSnippets(userId)
                }

                PermissionTypeInput.ALL -> {
                    getOwnedSnippets(userId) + getSharedSnippets(userId)
                }
            }

        return snippets
    }

    private fun getOwnedSnippets(userId: String): List<SnippetOutput> {
        val snippets = snippetRepository.findAllByUserId(userId)
        return snippets.stream().map { SnippetOutput(it.name, it.snippetKey) }.toList()
    }

    private fun getSharedSnippets(userId: String): List<SnippetOutput> {
        val snippets = snippetShareRepository.findAllSharedSnippetsByUserId(userId)
        return snippets.stream().map { SnippetOutput(it.name, it.snippetKey) }.toList()
    }

    private fun snippetKeyExists(snippetKey: String): Boolean {
        return snippetRepository.existsBySnippetKey(snippetKey)
    }
}
