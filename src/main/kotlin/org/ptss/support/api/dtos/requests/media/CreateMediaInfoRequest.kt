package org.ptss.support.api.dtos.requests.media

import java.io.InputStream

data class CreateMediaInfoRequest(
    val media: InputStream,
    val href: String?
)
