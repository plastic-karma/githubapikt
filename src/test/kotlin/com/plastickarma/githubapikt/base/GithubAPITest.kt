package com.plastickarma.githubapikt.base

import org.assertj.core.api.AssertionsForInterfaceTypes.*
import org.junit.jupiter.api.Test

class GithubAPITest {

    @Test
    fun `githubGET() uses string as url`() {
        val url = "http://github.com"
        val request = url.githubGET()
        assertThat(request.url.toExternalForm()).isEqualTo(url)
    }

    @Test
    fun `githubGET() adds github json header`() {
        val url = "http://github.com"
        val request = url.githubGET()
        assertThat(request.headers).containsEntry("Accept", listOf("application/vnd.github.v3+json"))
    }

    @Test
    fun `githubGET() adds parameters to url`() {
        val url = "http://github.com"
        val request = url.githubGET(listOf("a" to "b", "c" to "d"))
        assertThat(request.parameters).containsExactly("a" to "b", "c" to "d")
    }
}
