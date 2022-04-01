package net.stckoverflw.pluginjam.action.impl.startingphase

import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity

class StartingPhaseEndAction(
    private val gamemasterEntity: GamemasterEntity
) :
    Action() {

    override fun execute(): Action {

        return this
    }
}
