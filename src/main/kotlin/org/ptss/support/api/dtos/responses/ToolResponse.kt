package org.ptss.support.api.dtos.responses

import java.time.Instant

data class ToolResponse(
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val createdAt: Instant,
    val media: List<MediaInfoResponse>
)
