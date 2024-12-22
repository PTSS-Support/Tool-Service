package org.ptss.support.core.context

import io.quarkus.test.junit.QuarkusTestProfile

class NoDbTestProfile : QuarkusTestProfile {
    override fun getConfigOverrides(): Map<String, String> {
        return mapOf(
            "quarkus.datasource.enabled" to "false",
            "quarkus.hibernate-orm.enabled" to "false"
        )
    }
}