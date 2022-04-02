package net.stckoverflw.pluginjam.task.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.gamephase.impl.TaskPhase
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult
import net.stckoverflw.pluginjam.util.ListenerHolder
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

abstract class KillEntityTask : Task(), ListenerHolder {

    override val listeners: MutableList<Listener> = mutableListOf()
    protected val entities = hashMapOf<EntityType, Int>()

    private fun killedEntity(entityType: EntityType) {
        if (!entities.containsKey(entityType)) {
            return
        }
        entities[entityType] = entities[entityType]!! - 1
        if ((entities[entityType] ?: 1) <= 0) {
            entities.remove(entityType)
        }

        if (entities.isEmpty()) {
            TaskPhase.taskDone(TaskResult.SUCCESS)
        }
    }

    override fun start() {
        addListener(
            listen<EntityDeathEvent> {
                if (GamePhaseManager.activeGamePhase !is TaskPhase) return@listen
                if (it.entity.killer !is Player) return@listen
                killedEntity(it.entity.type)
            }
        )
    }

    override fun stop() {
        listeners.forEach {
            it.unregister()
        }
    }
}
