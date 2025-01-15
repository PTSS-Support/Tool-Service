package org.ptss.support.domain.enums

import org.ptss.support.common.exceptions.APIException

enum class SortOrder {
    ASC, DESC;

    companion object {
        fun fromString(value: String): SortOrder =
            entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw APIException(
                    errorCode = ErrorCode.VALIDATION_ERROR,
                    message = "Invalid sort order $value"
                )
    }
}