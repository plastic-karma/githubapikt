package com.plastickarma.githubapikt.base.auth

import com.github.kittinunf.fuel.core.FoldableRequestInterceptor
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.RequestTransformer

/**
 * Request interceptor for authorization.
 */
abstract class Authorization : FoldableRequestInterceptor {

    override fun invoke(next: RequestTransformer): RequestTransformer {
        return { request -> next(authorize(request)) }
    }

    /**
     * Makes changes to the request, so it is authorized and returns the authorized request.
     */
    abstract fun authorize(originalRequest: Request): Request
}

/**
 * Authorization that leaves the request unauthorized.
 */
object NoAuth : Authorization() {
    override fun authorize(originalRequest: Request) = originalRequest
}
