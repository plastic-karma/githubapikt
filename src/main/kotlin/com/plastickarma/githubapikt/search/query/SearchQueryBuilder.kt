package com.plastickarma.githubapikt.search.query

import java.time.Clock

/**
 * Builder to construct search queries for github search.
 * @see [https://developer.github.com/v3/search/#constructing-a-search-query]
 */
@SearchQuery
class SearchQueryBuilder(private val clock: Clock = Clock.systemDefaultZone()) {

    private val labels: MutableList<String> = emptyList<String>().toMutableList()

    private val languages: MutableList<String> = emptyList<String>().toMutableList()

    private var createdDateBuilder: DateBuilder? = null

    private var updatedDateBuilder: DateBuilder? = null

    private var sortQuery: String? = null

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
        createdDateBuilder = createDateBuilder(dateBlock)
    }

    /**
     * Adds search parameters for updated date.
     */
    fun updated(dateBlock: DateBuilder.() -> Unit) {
        updatedDateBuilder = createDateBuilder(dateBlock)
    }

    /**
     * Entry point to add sort query parameter.
     */
    val sortBy: SortByBuilder
        get() {
            return SortByBuilder { query ->
                sortQuery = query
            }
        }

    private fun createDateBuilder(dateBlock: DateBuilder.() -> Unit) =
        DateBuilder(clock).also(dateBlock)

    /**
     * Creates the search query string.
     */
    fun build(): String = listOf(
        labels.labelParameter(),
        languages.languageParameter(),
        state.stateParameter(),
        createdDateBuilder.dateParameter("created"),
        updatedDateBuilder.dateParameter("updated"),
        sortQuery.orEmpty()
    ).filter { it.isNotEmpty() }.joinToString("+")
}

/**
 * Entry point for creating SearchQueryBuilders.
 */
fun searchFor(body: SearchQueryBuilder.() -> Unit): SearchQueryBuilder {
    return SearchQueryBuilder().apply(body)
}
