package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import org.bukkit.Material
import org.bukkit.event.player.PlayerAttemptPickupItemEvent

class FightPhaseCollectAmethystAction : Action() {
    private var listener: SingleListener<PlayerAttemptPickupItemEvent>? = null

    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Du hast alle Monster besiegt. Hole dir jetzt den Amethyst!</i>")
            .start()

        listener = listen { event ->
            if (event.item.itemStack.type != Material.AMETHYST_SHARD) return@listen

            complete()
        }

        return this
    }

    override fun complete() {
        listener?.unregister()
        super.complete()
    }
}
