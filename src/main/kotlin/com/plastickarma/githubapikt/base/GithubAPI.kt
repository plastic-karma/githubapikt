package com.plastickarma.githubapikt.base

import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet

/**
 * URL for all requests to GitHub API
 */
const val BASE_URL = "https://api.github.com/"

/**
 * Creates a GET request, that has all needed headers for GitHub API.
 */
fun String.githubGET(parameters: Parameters = emptyList()): Request {
    return this
        .httpGet(parameters)
        .header("Accept", "application/vnd.github.v3+json")
}
