package com.plastickarma.githubapikt.http

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.RequestTransformer
import com.github.kittinunf.fuel.httpGet
import org.assertj.core.api.AssertionsForInterfaceTypes.*
import org.junit.jupiter.api.Test

class SearchQueryEncoderTest {

    @Test
    fun `searchQueryEncoder replaces escaped plus signs`() {
        val request = "https://gitgub.com&q=label%2Blanguage".httpGet()
        val transformedRequest = SearchQueryEncoder(Noop).invoke(request)
        assertThat(transformedRequest.url.toExternalForm()).isEqualTo("https://gitgub.com&q=label+language")
    }
}

private object Noop : RequestTransformer {
    override fun invoke(request: Request) = request
}
