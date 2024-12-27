package org.ptss.support.core.util

import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode

object ValidatePagination {
    fun validatePagination(pageSize: Int, sortOrder: String) {
        if (pageSize !in 1..50) {
            throw APIException(
                errorCode = ErrorCode.VALIDATION_ERROR,
                message = "Page size must be between 1 and 50"
            )
        }
        if (sortOrder !in listOf("asc", "desc")) {
            throw APIException(
                errorCode = ErrorCode.VALIDATION_ERROR,
                message = "Sort order must be 'asc' or 'desc'"
            )
        }
    }
}