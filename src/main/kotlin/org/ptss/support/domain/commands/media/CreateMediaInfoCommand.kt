package org.ptss.support.domain.commands.media

import java.io.InputStream

data class CreateMediaInfoCommand(
    val toolId: String,
    val fileData: InputStream?,
    val href: String?
)

