package org.ptss.support.api.dtos.requests.tool

data class CreateToolRequest(
    val name: String,
    val description: String,
    val category: List<String>
)
