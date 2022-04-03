package net.stckoverflw.pluginjam.action.impl.global

import net.axay.kspigot.runnables.taskRunLater
import net.stckoverflw.pluginjam.action.Action

class WaitAction(
    private val ticks: Long
) : Action() {

    override fun execute(): Action {
        taskRunLater(ticks) {
            println("completed")
            complete()
        }
        return this
    }
}
