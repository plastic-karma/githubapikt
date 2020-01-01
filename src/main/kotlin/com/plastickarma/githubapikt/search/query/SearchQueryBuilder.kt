package com.plastickarma.githubapikt.search.query

import java.time.Clock

/**
 * Builder to construct search queries for github search.
 * @see [https://developer.github.com/v3/search/#constructing-a-search-query]
 */
class SearchQueryBuilder(private val clock: Clock = Clock.systemDefaultZone()) {

    private val labels: MutableList<String> = emptyList<String>().toMutableList()

    private val languages: MutableList<String> = emptyList<String>().toMutableList()

    private var createdDateBuilder: DateBuilder? = null

    private var updatedDateBuilder: DateBuilder? = null

    /**
     * State parameter (for issues).
     */
    var state: String = ""

    /**
     * Adds a label to the search query.
     */
    fun label(label: String) {
        labels.add(label)
    }

    private fun labels(): String = labels.joinToString(" ") { "label:$it" }

    /**
     * Adds a (programming)language to the search query.
     */
    fun language(language: String) {
        languages.add(language)
    }

    /**
     * Adds search parameters for created date.
     */
    fun created(dateBlock: DateBuilder.() -> Unit) {
        createdDateBuilder = DateBuilder(clock).also(dateBlock)
    }

    /**
     * Adds search parameters for updated date.
     */
    fun updated(dateBlock: DateBuilder.() -> Unit) {
        updatedDateBuilder = DateBuilder(clock).also(dateBlock)
    }

    private fun languages(): String = languages.joinToString(" ") { "language:$it" }

    private fun state(): String = if (state.isNotEmpty()) "state:$state" else ""

    private fun createdDate(): String =
        createdDateBuilder?.let { "created:${it.build()}" }.orEmpty()

    private fun updatedDate(): String =
        updatedDateBuilder?.let { "updated:${it.build()}" }.orEmpty()

    /**
     * Creates the search query string.
     */
    fun build(): String = listOf(
        labels(),
        languages(),
        state(),
        createdDate(),
        updatedDate()
    ).filter { it.isNotEmpty() }.joinToString("+")
}
