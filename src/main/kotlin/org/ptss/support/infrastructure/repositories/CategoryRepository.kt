package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.domain.interfaces.repositories.ICategoryRepository
import org.ptss.support.domain.models.Category
import org.ptss.support.infrastructure.persistence.entities.CategoryEntity
import java.util.UUID

@ApplicationScoped
class CategoryRepository @Inject constructor(
    private val entityManager: EntityManager
) : ICategoryRepository, PanacheRepository<CategoryEntity> {
    @Transactional
    override suspend fun getAll(): List<Category> {
        return entityManager.createQuery(
            """
            SELECT DISTINCT c 
            FROM CategoryEntity c 
            LEFT JOIN FETCH c.tools
            """, CategoryEntity::class.java
        )
            .resultList
            .map { it.toDomain() }
    }
}