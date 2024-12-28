package org.ptss.support.api.dtos.requests.media

import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.PartType
import org.jboss.resteasy.reactive.RestForm
import java.io.InputStream

data class CreateMediaInfoRequest(
    @RestForm("media")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    val media: InputStream? = null,

    @RestForm("href")
    @PartType(MediaType.TEXT_PLAIN)
    val href: String? = null
)
