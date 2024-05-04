package entity

import jakarta.persistence.Entity

// TODO: This is for testing purposes only. Should be updated or deleted

@Entity
class Snippet(
    val name: String,
) : BaseEntity()
