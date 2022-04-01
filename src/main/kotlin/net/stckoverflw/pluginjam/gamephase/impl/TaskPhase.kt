package net.stckoverflw.pluginjam.gamephase.impl

import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult

object TaskPhase : GamePhase(TwistPhase) {

    private val tasks = listOf<Task>()

    private val taskResults = mutableMapOf<Task, TaskResult>()

    override fun start() {
        tasks.forEach {
            taskResults[it] = TaskResult.WAITING
        }
        findNewTask()
    }

    private fun findNewTask(): Boolean {
        val newTask = tasks.find { taskResults[it] == TaskResult.WAITING } ?: return false
        taskResults[newTask] = TaskResult.ACTIVE
        newTask.start()
        return true
    }

    override fun end() {
    }

    fun taskDone(result: TaskResult) {
        if (GamePhaseManager.activeGamePhase is TaskPhase) {
            val activeTask = tasks.find { taskResults[it] == TaskResult.ACTIVE } ?: error("No active task")
            taskResults[activeTask] = result
            if (!findNewTask()) {
                GamePhaseManager.nextPhase()
            }
        } else {
            error("taskDone method was called while TaskPhase is not active")
        }
    }
}
