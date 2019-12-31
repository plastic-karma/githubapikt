package com.plastickarma.githubapikt.base.auth

import com.github.kittinunf.fuel.httpGet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NoAuthTest {

    @Test
    fun `noauth does not add headers`() {
        val request = "https://gitgub.com".httpGet()
        val transformedRequest = NoAuth.invoke(Noop).invoke(request)
        assertThat(transformedRequest.headers).isEmpty()
    }
}
