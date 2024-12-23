package org.ptss.support.domain.models

import java.time.Instant

data class Category(
    val category: String, // Acts as the primary key
    val groupId: String,
    val createdAt: Instant,
    val tools: List<Tool>
)