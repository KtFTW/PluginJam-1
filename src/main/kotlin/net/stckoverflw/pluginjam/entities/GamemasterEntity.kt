package net.stckoverflw.pluginjam.entities

import net.axay.kspigot.event.listen
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.mini
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

class GamemasterEntity(private var interactCallback: () -> Unit) : ListenerHolder {
    private var bukkitEntity: Villager? = null
    override val listeners: MutableList<Listener> = mutableListOf()

    fun spawnEntity(location: Location) {
        bukkitEntity = location.world.spawnEntity(location, EntityType.VILLAGER) as Villager
        bukkitEntity !!.apply {
            customName(mini("<blue>Gamemaster"))
            profession = Villager.Profession.LIBRARIAN
            isCustomNameVisible = true
            setAI(false)
        }

        addListener(
            listen<PlayerInteractEntityEvent> {
                if (it.rightClicked != bukkitEntity) return@listen
                it.isCancelled = true
                interactCallback.invoke()
            }
        )

        addListener(
            listen<EntityDamageEvent> {
                if (it.entity != bukkitEntity) return@listen
                it.isCancelled = true
            }
        )
    }

    fun teleport(location: Location) {
        bukkitEntity?.teleport(location)
    }

    // TODO Pathfinding

    fun despawn() {
        if (bukkitEntity != null) {
            bukkitEntity !!.remove()
        }

        unegisterAllListeners()
    }
}
