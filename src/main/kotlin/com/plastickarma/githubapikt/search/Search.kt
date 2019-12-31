package com.plastickarma.githubapikt.search

import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.http.HttpContext
import com.plastickarma.githubapikt.pagination.nextLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

/**
 * Search API for GitHub.
 * @param type the type of entity to search n GitHub (e.g. issue or code)
 * @param query the search query to use (e.g. label:good-first-issue)
 * @param scope Coroutine context
 * @param httpContext Http request context
 * @return a [ReceiveChannel] that generates items of the given search type.
 * @see [https://developer.github.com/v3/search]
 */
@ExperimentalCoroutinesApi
fun <T : Any> GitHubAPIContext.search(
    type: SearchType<T>,
    query: String,
    scope: CoroutineScope,
    httpContext: HttpContext<List<T>> = type.httpContext()
): ReceiveChannel<T> = scope.produce {
    var queryParameters = listOf("q" to query)
    var currentUrl = type.url
    while (true) {
        val request = httpContext.httpGET(currentUrl, queryParameters)
        val (_, response, result) = httpContext.dispatchRequest(request)

        result.get().forEach { send(it) }

        val linkHeader = response.nextLink() ?: return@produce

        currentUrl = linkHeader.url
        queryParameters = emptyList()
    }
}

/**
 * Search API for GitHub.
 * @param type the type of entity to search n GitHub (e.g. issue or code)
 * @param query the search query to use (e.g. label:good-first-issue)
 * @param httpContext Http request context
 * @return a [ReceiveChannel] that generates items of the given search type.
 * @see [https://developer.github.com/v3/search]
 */
@ExperimentalCoroutinesApi
fun <T : Any> CoroutineScope.search(
    type: SearchType<T>,
    context: GitHubAPIContext,
    httpContext: HttpContext<List<T>> = type.httpContext(),
    query: String
) =
    context.search(type, query, this, httpContext)
