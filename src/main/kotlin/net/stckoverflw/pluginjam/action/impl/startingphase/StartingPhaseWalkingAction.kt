package net.stckoverflw.pluginjam.action.impl.startingphase

import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import org.bukkit.Location

class StartingPhaseWalkingAction(
    private val gamemasterEntity: GamemasterEntity,
    private val location: Location
) :
    Action() {

    override fun execute(): Action {
        gamemasterEntity.walkTo(location) {
            complete()
        }
        return this
    }
}
