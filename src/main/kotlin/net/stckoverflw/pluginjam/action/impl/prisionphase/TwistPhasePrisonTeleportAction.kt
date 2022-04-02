package net.stckoverflw.pluginjam.action.impl.prisionphase

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import org.bukkit.Location

class TwistPhasePrisonTeleportAction(
    private val gamemaster: GamemasterEntity,
    private val location: Location,
    private val gamemasterLocation: Location
) : Action() {

    override fun execute(): Action {
        println("prison teleport")
        onlinePlayers.forEach { it.teleport(location) }
        println("teleported")
        gamemaster.walkTo(gamemasterLocation) {
            println("walked")
            complete()
            println("completed")
        }
        return this
    }
}
