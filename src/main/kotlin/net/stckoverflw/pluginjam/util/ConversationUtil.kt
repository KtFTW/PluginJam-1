package net.stckoverflw.pluginjam.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import org.bukkit.Sound
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


data class ConversationMessage(
	val message: String,
	val delay: Duration = 3.seconds,
	val makeSound: Boolean = true
)

class Conversation(private val plugin: DevcordJamPlugin) {
	private val messages = arrayListOf<ConversationMessage>()

	fun addMessage(text: String, delay: Duration = 3.seconds, makeSound: Boolean = true): Conversation {
		messages.add(
			ConversationMessage(
			message = text,
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
				broadcastMini(current.message)
				if (current.makeSound) {
					onlinePlayers.forEach {
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
