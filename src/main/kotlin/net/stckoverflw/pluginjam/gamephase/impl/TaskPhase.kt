package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.impl.taskphase.TaskPhaseEndConversationAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult
import net.stckoverflw.pluginjam.task.impl.findmaterial.FindFoodTask
import net.stckoverflw.pluginjam.task.impl.findmaterial.FindOresTask
import net.stckoverflw.pluginjam.task.impl.findmaterial.FindWoodTask
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.GameMode
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

object TaskPhase : GamePhase(FightPhase), ListenerHolder {
    override val listeners: MutableList<Listener> = mutableListOf()
    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    private val gamemaster: GamemasterEntity = GamemasterEntity(false)
    private val taskResults = mutableMapOf<Task, TaskResult>()
    private val prisonArea = LocationArea(
        positionsConfig.getLocation("prison_0"),
        positionsConfig.getLocation("prison_1")
    )

    private val tasks = listOf<Task>(
        FindWoodTask(),
        FindOresTask(),
        FindFoodTask(),
        //  KillPillagersTask(),
    )

    override fun start() {
        gamemaster.spawnEntity(DevcordJamPlugin.instance.configManager.postionsConfig.getLocation("task_gamemaster"))
        onlinePlayers.forEach {
            it.teleportAsyncBlind(DevcordJamPlugin.instance.configManager.postionsConfig.getLocation("task_start"))
        }
        tasks.forEach {
            taskResults[it] = TaskResult.WAITING
        }
        findNewTask()

        addListener(
            listen<PlayerInteractEvent> {
                if (it.player.gameMode != GameMode.CREATIVE) {
                    it.isCancelled = false
                }
            }
        )

        addListener(
            listen<BlockPlaceEvent> {
                it.isCancelled = prisonArea.isInArea(it.block.location)
            }
        )

        addListener(
            listen<BlockBreakEvent> {
                it.isCancelled = prisonArea.isInArea(it.block.location)
            }
        )
    }

    private fun findNewTask(): Boolean {
        val newTask = tasks.find { taskResults[it] == TaskResult.WAITING } ?: return false
        taskResults[newTask] = TaskResult.ACTIVE
        newTask.introduce()
        return true
    }

    override fun end() {
        onlinePlayers.forEach {
            it.reset()
        }
        gamemaster.despawn()
    }

    fun taskDone(result: TaskResult) {
        if (GamePhaseManager.activeGamePhase is TaskPhase) {
            val activeTask = tasks.find { taskResults[it] == TaskResult.ACTIVE } ?: error("No active task")
            activeTask.stop()
            taskResults[activeTask] = result
            if (!findNewTask()) {
                TaskPhaseEndConversationAction()
                    .execute()
                    .whenComplete {
                        GamePhaseManager.nextPhase()
                    }
            }
        } else {
            error("taskDone method was called while TaskPhase is not active")
        }
    }
}
