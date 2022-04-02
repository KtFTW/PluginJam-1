package net.stckoverflw.pluginjam.task.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.gamephase.impl.TaskPhase
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult
import net.stckoverflw.pluginjam.util.ListenerHolder
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

abstract class FindMaterialTask : Task(), ListenerHolder {

    override val listeners: MutableList<Listener> = mutableListOf()
    protected val materials = hashMapOf<Material, Int>()

    private fun foundMaterial(material: Material, amount: Int) {
        if (!materials.containsKey(material)) {
            return
        }
        materials[material] = materials[material]!! - amount
        if ((materials[material] ?: 1) <= 0) {
            materials.remove(material)
        }

        if (materials.isEmpty()) {
            TaskPhase.taskDone(TaskResult.SUCCESS)
        }
    }

    override fun start() {
        addListener(
            listen<PlayerInteractEntityEvent> {
                if (GamePhaseManager.activeGamePhase !is TaskPhase) return@listen
                it.player.inventory.contents?.filterNotNull()?.forEach { itemStack ->
                    foundMaterial(itemStack.type, itemStack.amount)
                }
            }
        )
    }

    override fun stop() {
        listeners.forEach {
            it.unregister()
        }
    }
}
