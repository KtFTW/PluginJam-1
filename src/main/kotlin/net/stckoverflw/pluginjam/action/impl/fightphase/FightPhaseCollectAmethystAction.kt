package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.setOpenIfDoor
import org.bukkit.Material
import org.bukkit.event.player.PlayerAttemptPickupItemEvent

class FightPhaseCollectAmethystAction : Action() {
    private var listener: SingleListener<PlayerAttemptPickupItemEvent>? = null
    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig

    override fun execute(): Action {
        positionsConfig.getLocation("fight_door").block.setOpenIfDoor(true)
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i><tr:fight_collect></i>")
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
