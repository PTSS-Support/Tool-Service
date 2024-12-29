package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import org.ptss.support.domain.models.Category
import java.time.Instant
import java.util.*

@Entity
@Table(name = "categories")
class CategoryEntity {
    @Id
    @Column(nullable = false, unique = true)
    lateinit var category: String // Primary key

    @Column(name = "group_id", nullable = false, columnDefinition = "UUID")
    lateinit var groupId: UUID

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: Instant

    @ManyToMany(targetEntity = ToolEntity::class)
    @JoinTable(
        name = "category_tools",
        joinColumns = [
            JoinColumn(
                name = "category",
                referencedColumnName = "category",
                foreignKey = ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (category) REFERENCES categories(category) ON UPDATE CASCADE"
                )
            )
        ],
        inverseJoinColumns = [
            JoinColumn(name = "tool_id", referencedColumnName = "id")
        ]
    )
    var tools: List<ToolEntity> = emptyList()

    constructor()

    constructor(category: String, groupId: UUID, createdAt: Instant, tools: List<ToolEntity>) {
        this.category = category
        this.groupId = groupId
        this.createdAt = createdAt
        this.tools = tools
    }

    fun toDomain() = Category(
        category = category,
        groupId = groupId.toString(),
        createdAt = createdAt,
        tools = tools.map { it.toDomain() }
    )

    companion object {
        fun fromDomain(domain: Category, tools: List<ToolEntity> = emptyList()) = CategoryEntity(
            category = domain.category,
            groupId = UUID.fromString(domain.groupId),
            createdAt = domain.createdAt,
            tools = tools
        )
    }
}
