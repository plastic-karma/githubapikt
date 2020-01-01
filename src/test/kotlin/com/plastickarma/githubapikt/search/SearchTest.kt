package com.plastickarma.githubapikt.search

import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.result.Result
import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.http.HttpContext
import com.plastickarma.githubapikt.model.Issue
import com.plastickarma.githubapikt.search.query.searchFor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

@kotlinx.coroutines.ExperimentalCoroutinesApi
class SearchTest {

    @Test
    fun `search github issues returns list of issues`() = runBlockingTest {
        val expectedIssues = listOf(1, 2).toIssues()

        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response> {
            every { headers } returns Headers()
        }
        val mockResult = mockk<Result<List<Issue>, FuelError>> {
            every { get() } returns expectedIssues
        }

        val mockHttpContext = object : HttpContext<List<Issue>> {
            override suspend fun dispatchRequest(request: Request) = Triple(mockRequest, mockResponse, mockResult)
            override fun httpGET(url: String, parameters: Parameters) = mockRequest
        }

        val issues = search(
            ISSUES,
            GitHubAPIContext(),
            mockHttpContext) { }
        .toList()

        assertThat(issues).containsExactlyElementsOf(expectedIssues)
    }

    @Test
    fun `search entrypoints for issues yield the same results`() = runBlockingTest {
        val expectedIssues = listOf(1, 2).toIssues()

        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response> {
            every { headers } returns Headers()
        }
        val mockResult = mockk<Result<List<Issue>, FuelError>> {
            every { get() } returns expectedIssues
        }

        val mockHttpContext = object : HttpContext<List<Issue>> {
            override suspend fun dispatchRequest(request: Request) = Triple(mockRequest, mockResponse, mockResult)
            override fun httpGET(url: String, parameters: Parameters) = mockRequest
        }

        val apiContext = GitHubAPIContext()

        assertThat(search(ISSUES, apiContext, mockHttpContext) { }.toList())
            .containsExactlyElementsOf(expectedIssues)

        assertThat(ISSUES.search(apiContext, this, mockHttpContext) { }.toList())
            .containsExactlyElementsOf(expectedIssues)

        assertThat(apiContext.search(ISSUES, this, mockHttpContext) { }.toList())
            .containsExactlyElementsOf(expectedIssues)
    }

    @Test
    fun `search github issues retrieves all paginated results`() = runBlockingTest {
        val firstPage = listOf(1, 2).toIssues()
        val secondPage = listOf(3, 4).toIssues()
        val thirdPage = listOf(5, 6).toIssues()

        val firstRequest = mockk<Request>()
        val secondRequest = mockk<Request>()
        val thirdRequest = mockk<Request>()

        val firstResponse = mockk<Response> {
            every { headers } returns linkHeader("github.com/page1")
        }
        val secondResponse = mockk<Response> {
            every { headers } returns linkHeader("github.com/page2")
        }
        val thirdResponse = mockk<Response> {
            every { headers } returns Headers()
        }

        val mockHttpContext = object : HttpContext<List<Issue>> {
            override suspend fun dispatchRequest(request: Request) = when (request) {
                firstRequest -> Triple(request, firstResponse, firstPage.toResult())
                secondRequest -> Triple(request, secondResponse, secondPage.toResult())
                thirdRequest -> Triple(request, thirdResponse, thirdPage.toResult())
                else -> throw RuntimeException()
            }

            override fun httpGET(url: String, parameters: Parameters) = when (url) {
                ISSUES.url -> firstRequest
                "github.com/page1" -> secondRequest
                "github.com/page2" -> thirdRequest
                else -> throw RuntimeException()
            }
        }

        val issues = search(
            ISSUES,
            GitHubAPIContext(),
            mockHttpContext) { }
            .toList()

        assertThat(issues).containsExactlyElementsOf(firstPage + secondPage + thirdPage)
    }

    @Test
    fun `searchAll github issues returns list of issues`() = runBlockingTest {
        val expectedIssues1 = listOf(1, 2).toIssues()
        val expectedIssues2 = listOf(3, 4).toIssues()

        val mockRequest1 = mockk<Request>()
        val mockRequest2 = mockk<Request>()

        val mockResponse = mockk<Response> {
            every { headers } returns Headers()
        }

        val mockResult1 = mockk<Result<List<Issue>, FuelError>> {
            every { get() } returns expectedIssues1
        }

        val mockResult2 = mockk<Result<List<Issue>, FuelError>> {
            every { get() } returns expectedIssues2
        }

        val mockHttpContext = object : HttpContext<List<Issue>> {
            override suspend fun dispatchRequest(request: Request) = when (request) {
                mockRequest1 -> Triple(mockRequest1, mockResponse, mockResult1)
                mockRequest2 -> Triple(mockRequest2, mockResponse, mockResult2)
                else -> throw RuntimeException("dispatchRequest")
            }

            override fun httpGET(url: String, parameters: Parameters) = when {
                    parameters.contains(Pair("q", "label:good-first-issue")) -> mockRequest1
                    parameters.contains(Pair("q", "label:help-wanted")) -> mockRequest2
                    else -> throw RuntimeException("httpGET")
                }
            }

        val issues = listOf(
            searchFor {
                label("good-first-issue")
            },
            searchFor {
                label("help-wanted")
            }
        ).searchAll(ISSUES, this, GitHubAPIContext(), mockHttpContext).toList()

        assertThat(issues).isEqualTo(expectedIssues1 + expectedIssues2)
    }
}

private fun List<Int>.toIssues() =
    this.map { Issue(it, "issue$it", htmlUrl = "", url = "") }

private fun List<Issue>.toResult() = mockk<Result<List<Issue>, FuelError>> {
    every { get() } returns this@toResult
}

private fun linkHeader(link: String) = Headers().also {
    it["Link"] = listOf("<$link>; rel=\"next\"")
}
