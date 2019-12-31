package com.plastickarma.githubapikt.search

import com.plastickarma.githubapikt.base.BASE_URL
import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.http.DefaultHttpContext
import com.plastickarma.githubapikt.http.HttpContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Returns the URL to the search endpoint for the given search type.
 */
val SearchType<*>.url: String
    get() = "$BASE_URL${this.api}"

fun <T : Any> SearchType<T>.httpContext() = DefaultHttpContext(this.deserializer)

/**
 * @see [search]
 */
@ExperimentalCoroutinesApi
fun <T : Any> SearchType<T>.search(
    apiContext: GitHubAPIContext,
    query: String,
    scope: CoroutineScope,
    httpContext: HttpContext<List<T>> = this.httpContext()
): ReceiveChannel<T> {
    return apiContext.search(this, query, scope, httpContext)
}
