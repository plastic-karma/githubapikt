package com.plastickarma.githubapikt.pagination

import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.Response
import com.plastickarma.githubapikt.pagination.Relation.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URL

class ResponseExtensionsTest {

    @Test
    fun `nextLink returns header link with next relation`() {
        val headers = Headers().also {
            it["Link"] = listOf("<https://api.github.com/search/issues/test>; rel=\"next\"")
        }
        val response = Response(
            url = URL("https://api.github.com"),
            headers = headers
        )

        val nextLink = response.nextLink()
        assertTrue(nextLink != null)

        assertThat(nextLink).isEqualTo(LinkHeader("https://api.github.com/search/issues/test", NEXT))
    }

    @Test
    fun `nextLink returns header link with correct next relation`() {
        val headers = Headers().also {
            it["Link"] = listOf("<https://api.github.com/search/issues/test1>; rel=\"prev\",<https://api.github.com/search/issues/test2>; rel=\"next\"")
        }
        val response = Response(
            url = URL("https://api.github.com"),
            headers = headers
        )

        val nextLink = response.nextLink()
        assertTrue(nextLink != null)

        assertThat(nextLink).isEqualTo(LinkHeader("https://api.github.com/search/issues/test2", NEXT))
    }

    @Test
    fun `nextLink returns null when header is missing`() {
        val headers = Headers()
        val response = Response(
            url = URL("https://api.github.com"),
            headers = headers
        )

        assertTrue(response.nextLink() == null)
    }

    @Test
    fun `nextLink returns null when next link is missing is missing`() {
        val headers = Headers().also {
            it["Link"] = listOf("<https://api.github.com/search/issues/test>; rel=\"last\"")
        }
        val response = Response(
            url = URL("https://api.github.com"),
            headers = headers
        )

        assertTrue(response.nextLink() == null)
    }
}
