package com.plastickarma.githubapikt.model

import com.beust.klaxon.Json

/**
 * Represents an Issue on GitHub.
 */
data class Issue(
    val id: Int,
    val title: String,
    @Json(name = "html_url")
    val htmlUrl: String,
    val url: String
)
