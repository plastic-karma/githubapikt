package com.plastickarma.githubapikt.base.auth

import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.Request

/**
 * OAuth 2 token authentication interceptor
 */
class OAuth2Token(private val token: String) : Authorization() {
    override fun authorize(originalRequest: Request): Request {
        return originalRequest.apply {
            headers[Headers.AUTHORIZATION] = "token $token"
        }
    }
}
