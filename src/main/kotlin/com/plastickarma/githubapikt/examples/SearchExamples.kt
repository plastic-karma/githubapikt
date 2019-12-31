package com.plastickarma.githubapikt.examples

import com.plastickarma.githubapikt.base.GitHubAPIContext
import com.plastickarma.githubapikt.search.ISSUES
import com.plastickarma.githubapikt.search.search
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope

@ExperimentalCoroutinesApi
suspend fun main(): Unit = coroutineScope {

    // search on coroutine scope
    search(
        type = ISSUES,
        context = GitHubAPIContext()) { }
    .consumeEach { println(it) }

    // search on API context
    GitHubAPIContext().search(
        type = ISSUES,
        scope = this) { }
    .consumeEach { println(it) }

    // search on type
    ISSUES.search(
        apiContext = GitHubAPIContext(),
        scope = this) { }
    .consumeEach { println(it) }
}
