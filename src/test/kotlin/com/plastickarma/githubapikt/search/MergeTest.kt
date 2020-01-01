package com.plastickarma.githubapikt.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class MergeTest {

    @Test
    fun `merge combines elements from all given channels into one`() = runBlockingTest {
        val merged = merge(this, listOf(
            this.produce {
                send(1)
                send(2)
            },
            this.produce {
                send(3)
                send(4)
            },
            this.produce {
                send(5)
                send(6)
            })
        ).toList()
        assertThat(merged).containsExactly(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun `merge works with empty channels`() = runBlockingTest {
        val merged = merge(this, listOf(
            this.produce {
                send(1)
                send(2)
            },
            this.produce {
            },
            this.produce {
                send(5)
                send(6)
            })
        ).toList()
        assertThat(merged).containsExactly(1, 2, 5, 6)
    }

    @Test
    fun `merge works with no channels`() = runBlockingTest {
        val merged = merge<Int>(this, emptyList()).toList()
        assertThat(merged).isEmpty()
    }
}
