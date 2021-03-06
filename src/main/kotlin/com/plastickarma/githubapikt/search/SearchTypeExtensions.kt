package com.plastickarma.githubapikt.search

import com.plastickarma.githubapikt.base.BASE_URL
import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.http.DefaultHttpContext
import com.plastickarma.githubapikt.http.HttpContext
import com.plastickarma.githubapikt.search.query.SearchQueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Returns the URL to the search endpoint for the given search type.
 */
val SearchType<*>.url: String
    get() = "$BASE_URL${this.api}"

/**
 * Extension function to create a [HttpContext] from a given [SearchType].
 */
fun <T : Any> SearchType<T>.httpContext() = DefaultHttpContext(this.deserializer)

/**
 * @see [search]
 */
@ExperimentalCoroutinesApi
inline fun <T : Any> SearchType<T>.search(
    apiContext: GitHubAPIContext,
    scope: CoroutineScope,
    httpContext: HttpContext<List<T>> = this.httpContext(),
    queryBuilder: SearchQueryBuilder = SearchQueryBuilder(),
    crossinline query: SearchQueryBuilder.() -> Unit
): ReceiveChannel<T> {
    return apiContext.search(this, scope, httpContext, queryBuilder, query)
}
