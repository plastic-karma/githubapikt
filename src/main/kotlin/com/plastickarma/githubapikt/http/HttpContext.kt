package com.plastickarma.githubapikt.http

import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseResultOf

/**
 * Abstraction over HTTP related operations.
 * @param T: Type of the result for HTTP requests.
 */
interface HttpContext<T : Any> {

    /**
     * Creates a HTTP GET request.
     * @param url: URL for this GET request.
     * @param parameters: List of query parameters that will be attached to the URL.
     * @return a Fuel [Request] object
     */
    fun httpGET(url: String, parameters: Parameters): Request

    /**
     * Dispatches a [Request] and returns the result when done.
     * Function will suspend, when waiting for the result of HTTP request.
     * @param request the request to dispatch.
     * @return result of the request.
     */
    suspend fun dispatchRequest(request: Request): ResponseResultOf<T>
}
