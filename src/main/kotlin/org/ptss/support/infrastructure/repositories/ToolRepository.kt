package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.enums.MediaType
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.domain.models.Tool
import java.time.Instant

@ApplicationScoped
class ToolRepository : IToolRepository {
    override fun getAll(): List<Tool> {
        // Simulate database interaction with sample tools
        return listOf(
            Tool(
                id = "123e4567-e89b-12d3-a456-426614174000",
                name = "5-4-3-2-1 Methode",
                description = "Een praktische en effectieve...",
                createdBy = "Pricilla Simons",
                createdAt = Instant.parse("2024-01-01T10:00:00Z"),
                media = listOf(
                    MediaInfo(
                        id = "8cf6d7e4-2442-4fbb-8378-27bc941014b7",
                        url = "https://www.youtube.com/watch?v=example1",
                        type = MediaType.VIDEO
                    )
                )
            ),
            Tool(
                id = "234e5678-f90c-23e4-b567-537725285111",
                name = "478 Ademhalingsoefening",
                description = "Een ademhalingstechniek om stress te verminderen en je hartslag...",
                createdBy = "Anna de Vries",
                createdAt = Instant.parse("2024-03-20T09:45:00Z"),
                media = listOf(
                    MediaInfo(
                        id = "9df6d7e4-2442-4fbb-8378-27bc941014c8",
                        url = "https://www.youtube.com/watch?v=example2",
                        type = MediaType.IMAGE
                    )
                )
            )
        )
    }
}