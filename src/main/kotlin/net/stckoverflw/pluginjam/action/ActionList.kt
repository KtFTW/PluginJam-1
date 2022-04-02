package net.stckoverflw.pluginjam.action

import java.util.LinkedList
import java.util.Queue

class ActionList {
    private val actionQueue: Queue<Action> = LinkedList()
    private var callback: (() -> Unit)? = null

    fun add(action: Action): ActionList {
        actionQueue.add(action)
        return this
    }

    fun start(): ActionList {
        execute()
        return this
    }

    private fun execute() {
        val action = actionQueue.poll() ?: return finish()
        action
            .execute()
            .whenComplete {
                execute()
            }
    }

    private fun finish() {
        callback?.invoke()
    }

    fun whenComplete(function: () -> Unit): ActionList {
        callback = function
        return this
    }
}
