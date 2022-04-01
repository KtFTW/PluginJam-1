package net.stckoverflw.pluginjam.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.stckoverflw.pluginjam.DevcordJamPlugin
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Conversation(private val plugin: DevcordJamPlugin) {
    private val messages = arrayListOf<Pair<String, Duration>>()

    fun addMessage(text: String, delay: Duration = 3.seconds): Conversation {
        messages.add(text to delay)
        return this
    }

    fun start(): CompletableFuture<Unit> {
        val future = CompletableFuture<Unit>()
        plugin.defaultScope.launch {
            val iterator = messages.asIterable().iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                broadcastMini(current.first)
                delay(current.second)
            }
        }.invokeOnCompletion {
            future.complete(Unit)
        }
        return future
    }
}
