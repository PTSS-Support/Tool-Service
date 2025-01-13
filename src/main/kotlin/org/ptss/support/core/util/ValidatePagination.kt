package org.ptss.support.core.util

import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode

object ValidatePagination {
    fun validatePagination(pageSize: Int) {
        if (pageSize !in 1..50) {
            throw APIException(
                errorCode = ErrorCode.VALIDATION_ERROR,
                message = "Page size must be between 1 and 50"
            )
        }
    }
}