package net.stckoverflw.pluginjam.entities

import com.destroystokyo.paper.event.entity.EntityPathfindEvent
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.geometry.blockLoc
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.mini
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.VillagerCareerChangeEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class GamemasterEntity : ListenerHolder {
    private var bukkitEntity: Villager? = null
    override val listeners: MutableList<Listener> = mutableListOf()
    private var isPathFinding = false
    var interactCallback: (() -> Unit)? = null

    private var isSpawned: Boolean = false

    fun spawnEntity(location: Location) {
        if (isSpawned) return
        isSpawned = true
        bukkitEntity = location.world.spawnEntity(location, EntityType.VILLAGER) as Villager
        bukkitEntity !!.apply {
            customName(mini("<blue>Gamemaster"))
            profession = Villager.Profession.LIBRARIAN
            isCustomNameVisible = true
            setAI(false)
        }

        addListener(
            listen<PlayerInteractAtEntityEvent> {
                if (it.rightClicked != bukkitEntity) return@listen
                it.isCancelled = true
                interactCallback?.invoke()
            }
        )

        addListener(
            listen<EntityDamageEvent> {
                if (it.entity != bukkitEntity) return@listen
                if (it.cause == DamageCause.VOID) return@listen
                it.isCancelled = true
            }
        )

        addListener(
            listen<VillagerCareerChangeEvent> {
                if (it.entity != bukkitEntity) return@listen
                it.isCancelled = true
            }
        )

        addListener(
            listen<EntityPathfindEvent> {
                if (it.entity != bukkitEntity) return@listen
                if (! isPathFinding) return@listen
                it.isCancelled = true
            }
        )
    }

    fun teleport(location: Location) {
        bukkitEntity?.teleport(location)
    }

    fun walkTo(location: Location, callback: () -> Unit) {
//        bukkitEntity?.setAI(true)
        bukkitEntity?.
        bukkitEntity?.pathfinder?.setCanPassDoors(true)
        bukkitEntity?.pathfinder?.setCanOpenDoors(true)
        bukkitEntity?.pathfinder?.setCanFloat(false)
        broadcast("pathfinding started: ${bukkitEntity?.pathfinder?.moveTo(location)}")

//        Bukkit.getMobGoals().addGoal(bukkitEntity, Int.MAX_VALUE, )
//        Bukkit.getMobGoals().addGoal<Creature>(bukkitEntity!!, Int.MAX_VALUE, PaperVanillaGoal<Villager>(Goal))
//        Bukkit.getMobGoals().addGoal<Creature>(bukkitEntity!!, Int.MAX_VALUE, PaperVanillaGoal<Villager>(Vani))

        isPathFinding = true

        DevcordJamPlugin.instance.defaultScope.launch {
            while (true) {
                delay(200)
                if (bukkitEntity?.pathfinder?.currentPath?.finalPoint?.blockLoc == location.blockLoc) {
                    callback.invoke()
//                    bukkitEntity?.setAI(false)
                    isPathFinding = false
                    this.cancel()
                }
            }
        }
//            .invokeOnCompletion {
//            bukkitEntity?.setAI(false)
//            when (it) {
//                null, is CancellationException -> { // finish
//                    if (it is CancellationException) it.printStackTrace()
//                    callback.invoke()
//                }
//                else -> { // failed
//                }
//            }
//        }
    }

    fun despawn() {
        if (bukkitEntity != null) {
            bukkitEntity !!.remove()
        }

        unegisterAllListeners()
    }
}
