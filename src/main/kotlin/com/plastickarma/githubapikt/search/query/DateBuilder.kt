package com.plastickarma.githubapikt.search.query

import java.time.Clock
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * DSL builder class for dates in search queries.
 * Currently supports last(month()) and last(X.months()).
 * @see [https://help.github.com/en/github/searching-for-information-on-github/searching-issues-and-pull-requests#search-by-when-an-issue-or-pull-request-was-created-or-last-updated]
 */
class DateBuilder(private val clock: Clock = Clock.systemDefaultZone()) {

    private var date = ""

    /**
     * Adds a date and assumes that all issues on this date our younger should be included.
     * @param date Date to start search from.
     */
    fun last(date: DateString) {
        this.date = ">${date.date}"
    }

    fun month(noOfMonth: Long = 1): DateString {
        return DateString(DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(clock.zone)
            .format(Instant
                .now(clock)
                .minus(30 * noOfMonth, ChronoUnit.DAYS)))
    }

    fun Int.months(): DateString = month(this.toLong())

    /**
     * Return the date portion of a temporal query parameter.
     */
    fun build(): String = date
}
