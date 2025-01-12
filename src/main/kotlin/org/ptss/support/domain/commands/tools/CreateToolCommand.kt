package org.ptss.support.domain.commands.tools

data class CreateToolCommand(
    val name: String,
    val description: String,
    val category: List<String>
)
