package org.ptss.support.api.dtos.requests.media

import jakarta.ws.rs.FormParam
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.jboss.resteasy.reactive.PartType
import java.io.InputStream

data class UploadMediaRequest(
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: InputStream? = null
)
