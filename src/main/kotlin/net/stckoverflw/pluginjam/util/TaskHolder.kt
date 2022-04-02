package net.stckoverflw.pluginjam.util

import org.bukkit.scheduler.BukkitRunnable

interface TaskHolder {
    val tasks: MutableList<BukkitRunnable>

    fun addTask(task: BukkitRunnable) {
        tasks.add(task)
    }

    fun removeTask(task: BukkitRunnable) {
        tasks.remove(task)
    }

    fun removeAllTasks() {
        tasks.forEach { it.cancel() }
    }
}
