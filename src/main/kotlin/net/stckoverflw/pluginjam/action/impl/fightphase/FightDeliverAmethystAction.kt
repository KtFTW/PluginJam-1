package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.broadcastMini
import net.stckoverflw.pluginjam.util.reset
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.player.PlayerInteractEntityEvent

class FightDeliverAmethystAction : Action() {

    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    private lateinit var interactListener: SingleListener<PlayerInteractEntityEvent>
    private val gamemaster: GamemasterEntity = GamemasterEntity(false)

    override fun execute(): Action {
        val world = Bukkit.getWorld("world")!!
        world.entities.forEach {
            if (it.type != EntityType.ARROW) return@forEach

            it.remove()
        }

        gamemaster.spawnEntity(positionsConfig.getLocation("prison_gamemaster_2"))

        onlinePlayers.forEach {
            it.teleportAsync(positionsConfig.getLocation("prison_spawn_2"))
        }

        broadcastMini("<i>Gebt dem Dorfbewohner den Amethyst.")

        interactListener = listen {
            if (it.rightClicked != gamemaster.bukkitEntity) return@listen
            if (it.player.inventory.itemInMainHand.type != Material.AMETHYST_SHARD) return@listen
            it.isCancelled = true
            it.player.inventory.clear()
            Conversation(DevcordJamPlugin.instance)
                .addMessage(
                    "Wunderbar, nun haben wir beide Kristalle zusammen und können verhindern, dass die <red>Leviatanen</red> beide kombinieren.",
                    "Dorfbewohner"
                )
                .start()
                .whenComplete { _, _ ->
                    onlinePlayers.forEach { player ->
                        player.reset()
                    }
                    complete()
                }
        }

        return this
    }

    override fun complete() {
        interactListener.unregister()
        sync {
            gamemaster.despawn()
        }
        super.complete()
    }
}
