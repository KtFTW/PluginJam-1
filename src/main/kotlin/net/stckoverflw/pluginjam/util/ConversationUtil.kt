package net.stckoverflw.pluginjam.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.stckoverflw.pluginjam.DevcordJamPlugin
import org.bukkit.Sound
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class ConversationMessage(
    val message: String,
    val who: String?,
    val delay: Duration,
    val makeSound: Boolean,
)

class Conversation(private val plugin: DevcordJamPlugin) {
    private val messages = arrayListOf<ConversationMessage>()

    fun addMessage(
        text: String,
        who: String? = null,
        delay: Duration = 3.seconds,
        makeSound: Boolean = true
    ): Conversation {
        messages.add(
            ConversationMessage(
                message = text,
                who = who,
                delay = delay,
                makeSound = makeSound
            )
        )
        return this
    }

    fun start(): CompletableFuture<Unit> {
        val future = CompletableFuture<Unit>()
        plugin.defaultScope.launch {
            val iterator = messages.asIterable().iterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                if (current.who != null) {
                    broadcastMini("<#FF5733>${current.who}</#FF5733><gray>: <white>${current.message}")
                } else {
                    broadcastMini(current.message)
                }
                if (current.makeSound && current.who != null) {
                    pluginJamPlayers.forEach {
                        it.playSound(it.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)
                    }
                }
                delay(current.delay)
            }
        }.invokeOnCompletion {
            future.complete(Unit)
        }
        return future
    }
}
