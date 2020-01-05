package com.plastickarma.githubapikt.search.query

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SortByBuilderTest {

    @Test
    fun `sortby most interactions returns descending order`() {
        var queryResult: String? = null
        val sortBy = SortByBuilder { query ->
            queryResult = query
        }

        sortBy most Interactions

        assertThat(queryResult).isEqualTo("sort:interactions-desc")
    }

    @Test
    fun `sortby fewest interactions returns descending order`() {
        var queryResult: String? = null
        val sortBy = SortByBuilder { query ->
            queryResult = query
        }

        sortBy fewest Interactions

        assertThat(queryResult).isEqualTo("sort:interactions-asc")
    }
}
