package org.ptss.support.infrastructure.config

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithName

@ConfigMapping(prefix = "postgresql")
interface PostgreSQLConfig {
    @WithName("url")
    fun url(): String

    @WithName("username")
    fun username(): String

    @WithName("password")
    fun password(): String
}