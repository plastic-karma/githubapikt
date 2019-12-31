package com.plastickarma.githubapikt.search

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.plastickarma.githubapikt.model.Issue
import com.plastickarma.githubapikt.parse.IssuesDeserializer

/**
 * Type of search that can be performed against GitHub API.
 * @see [https://developer.github.com/v3/search/]
 */
sealed class SearchType<T>(val api: String, val deserializer: ResponseDeserializable<List<T>>)

/**
 * Issue Search.
 * @see [https://developer.github.com/v3/search/#search-issues-and-pull-requests]
 */
object ISSUES : SearchType<Issue>("search/issues", IssuesDeserializer())
