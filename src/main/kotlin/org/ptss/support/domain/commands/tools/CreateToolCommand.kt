package org.ptss.support.domain.commands.tools

data class CreateToolCommand(
    val name: String,
    val description: String,
    val category: List<String>,
    val createdBy: String, // Assuming you'll get the user info from the authenticated session
    val userId: String // Assuming you'll get the user info from the authenticated session
)
