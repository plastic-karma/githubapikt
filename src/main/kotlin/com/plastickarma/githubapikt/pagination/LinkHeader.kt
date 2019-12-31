package com.plastickarma.githubapikt.pagination

/**
 * Represents a link header that is used in pagination.
 * @see [https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Link]
 */
data class LinkHeader(
    val url: String,
    val rel: Relation
)

/**
 * Relations that a link can represent.
 */
enum class Relation {
    NEXT,
    PREV,
    LAST,
    UNKNOWN
}
