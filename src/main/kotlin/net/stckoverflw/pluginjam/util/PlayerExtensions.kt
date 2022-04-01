package net.stckoverflw.pluginjam.util

import net.axay.kspigot.extensions.bukkit.title
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

fun Player.sendMini(message: String) = sendMessage(message.deserializeMini())

fun String.deserializeMini() = MiniMessage.miniMessage().deserialize(this)

fun Player.teleportAsyncBlind(location: Location) {
	addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, Int.MAX_VALUE, 255, false, false, false))
	title(
		"<orange>Du wirst gerade teleportiert!</orange>".deserializeMini(),
		Component.empty(),
		Duration.ofMillis(50),
		Duration.ofMinutes(1),
		Duration.ofMillis(50)
	)
	teleportAsync(location).whenComplete { result, error ->
		if (result) {
			removePotionEffect(PotionEffectType.BLINDNESS)
			emptyTitle()
		}
	}
}

fun Player.emptyTitle() = title(
	Component.empty(),
	Component.empty(),
	Duration.ofMillis(1),
	Duration.ofMillis(1),
	Duration.ofMillis(1)
)
