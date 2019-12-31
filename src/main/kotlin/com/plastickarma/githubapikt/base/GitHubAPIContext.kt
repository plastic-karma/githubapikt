package com.plastickarma.githubapikt.base

import com.github.kittinunf.fuel.core.FuelManager
import com.plastickarma.githubapikt.base.auth.Authorization
import com.plastickarma.githubapikt.base.auth.NoAuth
import com.plastickarma.githubapikt.http.SearchQueryEncoder

/**
 * Context object which takes care of initialization and authorization.
 */
class GitHubAPIContext(authorization: Authorization = NoAuth) {

    init {
        FuelManager.instance
            .addRequestInterceptor(SearchQueryEncoder)
            .addRequestInterceptor(authorization)
    }
}
