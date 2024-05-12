package app.permission.persistance.repository

import app.permission.persistance.entity.SnippetShare
import org.springframework.data.jpa.repository.JpaRepository

interface SnippetShareRepository : JpaRepository<SnippetShare, String>
