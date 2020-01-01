package com.plastickarma.githubapikt.search.query

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class DateBuilderTest {

    @Test
    fun `last(month()) creates a smaller-than date from 30 days ago`() {
        val builder = DateBuilder(Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC")))
        builder.last(builder.month())
        assertThat(builder.build()).isEqualTo(">1969-12-01")
    }

    @Test
    fun `last(3 month()) creates a smaller-than date from 90 days ago`() {
        val builder = DateBuilder(Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC")))
        builder.last(builder.month(3))
        assertThat(builder.build()).isEqualTo(">1969-10-02")
    }
}
