package com.plastickarma.githubapikt.http

import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.coroutines.awaitResponseResult
import com.plastickarma.githubapikt.base.githubGET

/**
 * Default HTTP context.
 */
class DefaultHttpContext<T : Any>(private val deserializer: ResponseDeserializable<T>) : HttpContext<T> {
    override fun httpGET(url: String, parameters: Parameters): Request {
        return url.githubGET(parameters)
    }

    override suspend fun dispatchRequest(request: Request): ResponseResultOf<T> {
        return request.awaitResponseResult(deserializer)
    }
}
