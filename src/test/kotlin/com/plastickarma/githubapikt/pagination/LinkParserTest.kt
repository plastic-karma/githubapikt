package com.plastickarma.githubapikt.pagination

import com.plastickarma.githubapikt.pagination.Relation.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class LinkParserTest {

    @Test
    fun `parseLinkHeader returns link with next relation`() {
        val header = "<https://api.github.com/search/issues/test>; rel=\"next\""

        assertThat(parseLinkHeader(header)).containsExactly(
            LinkHeader("https://api.github.com/search/issues/test", NEXT)
        )
    }

    @Test
    fun `parseLinkHeader returns link with last relation`() {
        val header = "<https://api.github.com/search/issues/test>; rel=\"last\""

        assertThat(parseLinkHeader(header)).containsExactly(
            LinkHeader("https://api.github.com/search/issues/test", LAST)
        )
    }

    @Test
    fun `parseLinkHeader returns link with prev relation`() {
        val header = "<https://api.github.com/search/issues/test>; rel=\"prev\""

        assertThat(parseLinkHeader(header)).containsExactly(
            LinkHeader("https://api.github.com/search/issues/test", PREV)
        )
    }

    @Test
    fun `parseLinkHeader returns link with unknown relation`() {
        val header = "<https://api.github.com/search/issues/test>; rel=\"magic\""

        assertThat(parseLinkHeader(header)).containsExactly(
            LinkHeader("https://api.github.com/search/issues/test", UNKNOWN)
        )
    }

    @Test
    fun `parseLinkHeader returns empty when rel is missing`() {
        val header = "<https://api.github.com/search/issues/test>"
        assertThat(parseLinkHeader(header)).isEmpty()
    }

    @Test
    fun `parseLinkHeader returns empty when link is missing`() {
        val header = "rel=\"magic\""
        assertThat(parseLinkHeader(header)).isEmpty()
    }

    @Test
    fun `parseLinkHeader returns empty header is malformed`() {
        val header = "<https://api.github.com/search/issues/test>; rel:\"magic\""
        assertThat(parseLinkHeader(header)).isEmpty()
    }

    @Test
    fun `parseLinkHeader returns multiple links with relation`() {
        val header =
            "<https://api.github.com/search/issues/test1>; rel=\"next\"," +
            "<https://api.github.com/search/issues/test2>; rel=\"prev\""

        assertThat(parseLinkHeader(header)).containsExactly(
            LinkHeader("https://api.github.com/search/issues/test1", NEXT),
            LinkHeader("https://api.github.com/search/issues/test2", PREV)
        )
    }

    @Test
    fun `parseLinkHeader ignores malformed headers`() {
        val header =
            "<https://api.github.com/search/issues/test1>; rel=\"next\"," +
            "<https://api.github.com/search/issues/test2>; rel:\"prev\"," +
            "<https://api.github.com/search/issues/test3>; rel=\"last\""

        assertThat(parseLinkHeader(header)).containsExactly(
            LinkHeader("https://api.github.com/search/issues/test1", NEXT),
            LinkHeader("https://api.github.com/search/issues/test3", LAST)
        )
    }
}
