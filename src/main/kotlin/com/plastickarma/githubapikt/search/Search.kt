package com.plastickarma.githubapikt.search

import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.http.HttpContext
import com.plastickarma.githubapikt.pagination.nextLink
import com.plastickarma.githubapikt.search.query.SearchQueryBuilder
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
inline fun <T : Any> GitHubAPIContext.search(
    type: SearchType<T>,
    scope: CoroutineScope,
    httpContext: HttpContext<List<T>> = type.httpContext(),
    queryBuilder: SearchQueryBuilder = SearchQueryBuilder(),
    crossinline query: SearchQueryBuilder.() -> Unit
): ReceiveChannel<T> = scope.produce {
    var queryParameters = listOf("q" to queryBuilder.also(query).build())
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
inline fun <T : Any> CoroutineScope.search(
    type: SearchType<T>,
    context: GitHubAPIContext,
    httpContext: HttpContext<List<T>> = type.httpContext(),
    queryBuilder: SearchQueryBuilder = SearchQueryBuilder(),
    crossinline query: SearchQueryBuilder.() -> Unit
) =
    context.search(type, this, httpContext, queryBuilder, query)

/**
 * Search API for Github. Makes a request for each search query.
 * @param type the type of entity to search n GitHub (e.g. issue or code)
 * @param httpContext Http request context
 * @return a [ReceiveChannel] that generates items of the given search type.
 * @see [https://developer.github.com/v3/search]
 */
@ExperimentalCoroutinesApi
fun <T : Any> List<SearchQueryBuilder>.searchAll(
    type: SearchType<T>,
    scope: CoroutineScope,
    context: GitHubAPIContext,
    httpContext: HttpContext<List<T>> = type.httpContext()
) = merge(scope, this.map { context.search(type, scope, httpContext, it) { } })
