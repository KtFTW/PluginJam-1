package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import org.bukkit.Location

class TwistPhasePrisonTeleportAction(
    private val gamemaster: GamemasterEntity,
    private val location: Location,
    private val gamemasterLocation: Location
) : Action() {

    override fun execute(): Action {
        pluginJamPlayers.forEach { it.teleport(location) }
        gamemaster.walkTo(gamemasterLocation) {
            complete()
        }
        return this
    }
}
