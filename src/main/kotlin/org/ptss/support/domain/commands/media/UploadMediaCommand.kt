package org.ptss.support.domain.commands.media

import java.io.InputStream

data class UploadMediaCommand(
    val toolId: String,       // The ID of the tool
    val fileData: InputStream?,  // The media file data (image, video, audio, etc.)
)
