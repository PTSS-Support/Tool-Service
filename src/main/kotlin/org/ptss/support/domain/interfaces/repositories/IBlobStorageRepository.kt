package org.ptss.support.domain.interfaces.repositories

import java.io.InputStream

interface IBlobStorageRepository {
    suspend fun uploadFileToBlobStorage(fileStream: InputStream, fileName: String): String
    suspend fun deleteFileFromBlob(blobName: String)
}