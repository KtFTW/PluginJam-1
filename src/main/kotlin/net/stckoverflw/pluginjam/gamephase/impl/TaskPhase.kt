package net.stckoverflw.pluginjam.gamephase.impl

import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult
import net.stckoverflw.pluginjam.task.impl.findmaterial.FindFoodTask
import net.stckoverflw.pluginjam.task.impl.findmaterial.FindOresTask
import net.stckoverflw.pluginjam.task.impl.findmaterial.FindWoodTask
import net.stckoverflw.pluginjam.task.impl.killentity.KillPillagersTask

object TaskPhase : GamePhase(TwistPhase) {

    private val tasks = listOf<Task>(
        FindWoodTask(),
        FindOresTask(),
        FindFoodTask(),
        KillPillagersTask()
    )

    private val taskResults = mutableMapOf<Task, TaskResult>()
    private val gamemaster: GamemasterEntity = GamemasterEntity(true)

    override fun start() {
        tasks.forEach {
            taskResults[it] = TaskResult.WAITING
        }
        findNewTask()
    }

    private fun findNewTask(): Boolean {
        val newTask = tasks.find { taskResults[it] == TaskResult.WAITING } ?: return false
        taskResults[newTask] = TaskResult.ACTIVE
        newTask.introduce()
        return true
    }

    override fun end() {
    }

    fun taskDone(result: TaskResult) {
        if (GamePhaseManager.activeGamePhase is TaskPhase) {
            val activeTask = tasks.find { taskResults[it] == TaskResult.ACTIVE } ?: error("No active task")
            taskResults[activeTask] = result
            activeTask.stop()
            if (!findNewTask()) {
                GamePhaseManager.nextPhase()
            }
        } else {
            error("taskDone method was called while TaskPhase is not active")
        }
    }
}
