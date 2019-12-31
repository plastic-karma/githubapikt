package com.plastickarma.githubapikt.pagination

private val LINK_REGEX = "<(.+)>.*;.*rel=\"(.*)\"".toRegex()

/**
 * Parses a link header.
 * @see [https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Link]
 */
fun parseLinkHeader(header: String): List<LinkHeader> {
    return header.split(",")
        .flatMap { LINK_REGEX.findAll(it).toList() }
        .filter { matchResult -> matchResult.groupValues.size == 3 }
        .map {
            LinkHeader(
                url = it.groupValues[1],
                rel = when (it.groupValues[2]) {
                    "next" -> Relation.NEXT
                    "last" -> Relation.LAST
                    "prev" -> Relation.PREV
                    else -> Relation.UNKNOWN
                }
            )
        }
}
