package org.ptss.support.api.dtos.requests.tools

data class CreateToolRequest(
    val name: String,
    val description: String,
    val category: List<String>
)
