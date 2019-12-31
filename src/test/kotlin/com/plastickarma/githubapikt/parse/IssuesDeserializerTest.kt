package com.plastickarma.githubapikt.parse

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.requests.DefaultBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URL

class IssuesDeserializerTest {

    @Test
    fun `deserialize returns a list of issues from json`() {
        val response = Response(
            url = URL("https://api.github.com"),
            body = DefaultBody(
                openStream = { IssuesDeserializerTest::class.java.getResourceAsStream("/issues.json") })
        )
        val issues = IssuesDeserializer().deserialize(response)
        assertThat(issues).isNotEmpty
    }
}
