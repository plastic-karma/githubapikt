package com.plastickarma.githubapikt.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

/**
 * Merges given channels into one channel.
 * @param scope Coroutine scope
 * @param channels channels that should be merged.
 * @return channel that contains elements from all given channels
 */
@ExperimentalCoroutinesApi
fun <T> merge(scope: CoroutineScope, channels: List<ReceiveChannel<T>>) = scope.produce {
    channels.forEach { channel -> channel.consumeEach { send(it) } }
}
