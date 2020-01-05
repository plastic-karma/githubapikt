package com.plastickarma.githubapikt.search.query

/**
 * Creates a language parameter from the given list of languages.
 */
fun List<String>.languageParameter(): String = this.joinToString(" ") { "language:$it" }

/**
 * Creates a label parameter from the given list of languages.
 */
fun List<String>.labelParameter(): String = this.joinToString(" ") { "label:$it" }

/**
 * Creates a date parameter from the given date builder.
 */
fun DateBuilder?.dateParameter(prefix: String) = this?.let { "$prefix:${it.build()}" }.orEmpty()

/**
 * Creates a state parameter from the given state string.
 */
fun String.stateParameter() = if (this.isNotEmpty()) "state:$this" else ""
