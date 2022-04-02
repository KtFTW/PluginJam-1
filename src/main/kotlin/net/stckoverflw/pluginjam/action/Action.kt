package net.stckoverflw.pluginjam.action

abstract class Action {
    private var completeCallback: (() -> Unit)? = null
    abstract fun execute(): Action

    fun whenComplete(callback: () -> Unit) {
        completeCallback = callback
    }

    open fun complete() {
        completeCallback?.invoke()
    }
}
