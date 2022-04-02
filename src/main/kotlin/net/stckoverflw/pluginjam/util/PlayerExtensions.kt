package net.stckoverflw.pluginjam.util

import net.axay.kspigot.extensions.bukkit.title
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

fun Player.sendMini(message: String) = sendMessage(message.deserializeMini())

fun String.deserializeMini() = MiniMessage.miniMessage().deserialize(this)

fun Player.teleportAsyncBlind(location: Location) {
    addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, Int.MAX_VALUE, 255, false, false, false))
    teleportAsync(location).whenComplete { result, _ ->
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

fun Player.smeltItemInHand() {
    val item = inventory.itemInMainHand
    val result = Bukkit.recipeIterator().asSequence().filterIsInstance<FurnaceRecipe>().firstOrNull {
        it.input.type == item.type
    }?.result ?: return
    inventory.setItemInMainHand(ItemStack(result.type, inventory.itemInMainHand.amount))
}

fun Player.reset() {
    inventory.clear()
    activePotionEffects.forEach {
        removePotionEffect(it.type)
    }
    health = 20.0
    foodLevel = 20
    level = 0
    exp = 0f
}

fun Iterable<Player>.reset() {
    forEach { it.reset() }
}
