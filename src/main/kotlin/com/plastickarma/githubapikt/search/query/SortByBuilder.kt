package com.plastickarma.githubapikt.search.query

/**
 * Type to sort by (e.g. interactions, comments, stars)
 * @param query Query string to use in Github API.
 */
enum class SortByType(val query: String) {

    interactions("interactions")
}

/**
 * Builder class to enable sorting in search query DSL.
 */
class SortByBuilder(private val queryCallback: (String) -> Unit) {

    /**
     * Adds sort parameter to have search results with most sort types first.
     */
    infix fun most(type: SortByType) {
        sendQuery(type, "desc")
    }

    /**
     * Adds sort parameter to have search results with fewest sort types first.
     */
    infix fun fewest(type: SortByType) {
        sendQuery(type, "asc")
    }

    private fun sendQuery(type: SortByType, suffix: String) {
        queryCallback("sort:${type.query}-$suffix")
    }
}
