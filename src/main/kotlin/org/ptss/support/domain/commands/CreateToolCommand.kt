package org.ptss.support.domain.commands

data class CreateToolCommand(
    val name: String,
    val description: String,
    val category: List<String>,
    val createdBy: String // Assuming you'll get the user info from the authenticated session
)
