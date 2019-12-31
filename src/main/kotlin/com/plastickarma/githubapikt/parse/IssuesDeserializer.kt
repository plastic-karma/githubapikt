package com.plastickarma.githubapikt.parse

import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.plastickarma.githubapikt.model.Issue

/**
 * [ResponseDeserializable], that extracts a list of issues from a search response.
 */
class IssuesDeserializer : ResponseDeserializable<List<Issue>> {
    override fun deserialize(response: Response): List<Issue> {
        return Klaxon()
            .parse<IssueContainer>(response.data.inputStream().bufferedReader())
            ?.items
            .orEmpty()
    }
}

/**
 * Helper class to deserialize the JSON structure that contains a list of issues.
 */
private data class IssueContainer(val items: List<Issue>)
