package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import org.ptss.support.domain.models.Category
import java.time.Instant

@Entity
@Table(name = "categories")
class CategoryEntity {
    @Id
    @Column(nullable = false, unique = true)
    lateinit var category: String // Primary key

    @Column(name = "group_id", nullable = false, columnDefinition = "UUID")
    lateinit var groupId: String

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: Instant

    @ManyToMany(targetEntity = ToolEntity::class)
    @JoinTable(
        name = "category_tools",
        joinColumns = [JoinColumn(name = "category", referencedColumnName = "category")],
        inverseJoinColumns = [JoinColumn(name = "tool_id", referencedColumnName = "id")]
    )
    var tools: List<ToolEntity> = emptyList()

    constructor()

    constructor(category: String, groupId: String, createdAt: Instant, tools: List<ToolEntity>) {
        this.category = category
        this.groupId = groupId
        this.createdAt = createdAt
        this.tools = tools
    }

    fun toDomain() = Category(
        category = category,
        groupId = groupId,
        createdAt = createdAt,
        tools = tools.map { it.toDomain() }
    )

    companion object {
        /*fun fromDomain(domain: Category) = CategoryEntity(
            category = domain.category,
            groupId = domain.groupId,
            createdAt = domain.createdAt,
            tools = domain.tools.map { ToolEntity.fromDomain(it) }
        )*/
    }
}
