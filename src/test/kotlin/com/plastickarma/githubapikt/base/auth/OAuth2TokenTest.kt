package com.plastickarma.githubapikt.base.auth

import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.httpGet
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class OAuth2TokenTest {

    @Test
    fun `requestTransformer adds oauth header`() {
        val request = "https://gitgub.com".httpGet()
        val transformedRequest = OAuth2Token("mytoken").invoke(Noop).invoke(request)
        assertThat(transformedRequest.headers[Headers.AUTHORIZATION])
            .containsExactly("token mytoken")
    }
}
