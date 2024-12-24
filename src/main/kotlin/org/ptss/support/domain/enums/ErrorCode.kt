package org.ptss.support.domain.enums

enum class ErrorCode(
    val code: String,
    val status: Int,
    val description: String
) {
    // Authentication errors
    INVALID_TOKEN("INVALID_TOKEN", 401, "Invalid or expired authentication token"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", 401, "Invalid email or password provided"),
    EXPIRED_TOKEN("EXPIRED_TOKEN", 401, "Authentication token has expired"),
    TOKEN_GENERATION_FAILED("TOKEN_GENERATION_FAILED", 401, "Failed to generate a valid access token"),

    // Authorization errors
    INSUFFICIENT_PERMISSIONS("INSUFFICIENT_PERMISSIONS", 403, "User does not have required permissions"),

    // Validation errors
    VALIDATION_ERROR("VALIDATION_ERROR", 400, "Request validation failed"),
    DUPLICATE_ENTRY("DUPLICATE_ENTRY", 400, "Resource already exists"),

    // Resource errors
    USER_NOT_FOUND("USER_NOT_FOUND", 404, "The requested user was not found"),
    GROUP_NOT_FOUND("GROUP_NOT_FOUND", 404, "The specified group does not exist"),
    NOT_FOUND("NOT_FOUND", 404, "The requested resource was not found"),

    // Tool errors
    TOOL_NOT_FOUND("TOOL_NOT_FOUND", 404, "The requested tool was not found"),

    // Category errors
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", 404, "The requested category was not found"),

    // Comment errors
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", 404, "The requested comment was not found"),

    INTERNAL_ERROR("INTERNAL_ERROR", 500, "An unexpected system error occurred"),
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", 429, "Too many requests"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", 503, "Service temporarily unavailable"),
    GATEWAY_TIMEOUT("GATEWAY_TIMEOUT", 504, "Request timed out"),

    // Product errors
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", 404, "The requested product was not found"),
    PRODUCT_VALIDATION_ERROR("PRODUCT_VALIDATION_ERROR", 400, "Product data validation failed"),
    PRODUCT_CREATION_ERROR("PRODUCT_CREATION_ERROR", 400, "Failed to create product"),

    // Tool errors
    TOOL_VALIDATION_ERROR("TOOL_VALIDATION_ERROR", 400, "tool data validation failed"),
    TOOL_CREATION_ERROR("TOOL_CREATION_ERROR", 400, "Failed to create tool"),
    TOOL_DELETION_ERROR("TOOL_DELETION_ERROR", 400, "Failed to delete tool"),

    // Comment errors
    COMMENT_UPDATE_ERROR("COMMENT_UPDATE_ERROR", 400, "Dailed to update comment"),
    COMMENT_VALIDATION_ERROR("COMMENT_VALIDATION_ERROR", 400, "Comment data validation failed"),
    COMMENT_CREATION_ERROR("COMMENT_CREATION_ERROR", 400, "Failed to create comment"),
    COMMENT_DELETION_ERROR("COMMENT_DELETION_ERROR", 400, "Failed to delete comment"),

    // Category errors
    CATEGORY_UPDATE_ERROR("CATEGORY_UPDATE_ERROR", 400, "Failed to update category"),
    CATEGORY_CREATION_ERROR("CATEGORY_CREATION_ERROR", 400, "Failed to create comment");

    companion object {
        fun fromCode(code: String): ErrorCode? = values().find { it.code == code }
    }
}