package com.plastickarma.githubapikt.pagination

import com.github.kittinunf.fuel.core.Response

/**
 * Gets the link that contains the next set of results.
 */
fun Response.nextLink(): LinkHeader? {
    return this.headers["Link"]
        .flatMap { parseLinkHeader(it) }
        .find { it.rel == Relation.NEXT }
}
