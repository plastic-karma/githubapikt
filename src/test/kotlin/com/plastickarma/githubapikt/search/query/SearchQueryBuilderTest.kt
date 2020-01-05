package com.plastickarma.githubapikt.search.query

import com.plastickarma.githubapikt.search.query.SortByType.interactions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class SearchQueryBuilderTest {

    @Test
    fun `search with single label`() {
        val query = buildSearchQuery {
            label("good-first-issue")
        }
        assertThat(query).isEqualTo("label:good-first-issue")
    }

    @Test
    fun `search with multiple label`() {
        val query = buildSearchQuery {
            label("good-first-issue")
            label("help-wanted")
        }
        assertThat(query).isEqualTo("label:good-first-issue label:help-wanted")
    }

    @Test
    fun `search with single language`() {
        val query = buildSearchQuery {
            language("kotlin")
        }
        assertThat(query).isEqualTo("language:kotlin")
    }

    @Test
    fun `search with multiple languages`() {
        val query = buildSearchQuery {
            language("kotlin")
            language("python")
        }
        assertThat(query).isEqualTo("language:kotlin language:python")
    }

    @Test
    fun `search with state`() {
        val query = buildSearchQuery {
            state = "open"
        }
        assertThat(query).isEqualTo("state:open")
    }

    @Test
    fun `search with created date`() {
        val query = buildSearchQuery {
            created { last(90.days()) }
        }
        assertThat(query).isEqualTo("created:>1969-10-03")
    }

    @Test
    fun `search with update date`() {
        val query = buildSearchQuery {
            updated { last(90.days()) }
        }
        assertThat(query).isEqualTo("updated:>1969-10-03")
    }

    @Test
    fun `search with multiple parameters`() {
        val query = buildSearchQuery {
            language("kotlin")
            label("good-first-issue")
            label("help-wanted")
            updated { last(90.days()) }
            created { last(60.days()) }
            sortBy most interactions
            state = "open"
        }
        assertThat(query).isEqualTo("label:good-first-issue label:help-wanted+language:kotlin+state:open+created:>1969-11-02+updated:>1969-10-03+sort:interactions-desc")
    }

    private fun buildSearchQuery(builder: SearchQueryBuilder.() -> Unit): String {
        return SearchQueryBuilder(Clock.fixed(Instant.ofEpochMilli(0), ZoneOffset.UTC)).also(builder).build()
    }
}
