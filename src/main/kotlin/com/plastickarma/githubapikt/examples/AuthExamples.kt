package com.plastickarma.githubapikt.examples

import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.base.auth.NoAuth
import com.plastickarma.githubapikt.base.auth.OAuth2Token
import com.plastickarma.githubapikt.search.ISSUES
import com.plastickarma.githubapikt.search.search
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope

/**
 * Examples on how to use authorization.
 */
@ExperimentalCoroutinesApi
suspend fun main(): Unit = coroutineScope {

    val token = "<YOUR TOKEN HERE>"
    GitHubAPIContext(OAuth2Token(token)).search(
        ISSUES, this) { }
        .consumeEach { println(it) }

    GitHubAPIContext(NoAuth).search(
        ISSUES, this) { }
        .consumeEach { println(it) }
}
