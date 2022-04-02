package net.stckoverflw.pluginjam.action.impl.global

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.extensions.bukkit.actionBar
import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import org.bukkit.Location

class WalkingAction(
    private val gamemasterEntity: GamemasterEntity,
    private val location: Location,
) :
    Action() {

    override fun execute(): Action {
        val job = DevcordJamPlugin.instance.defaultScope.launch {
            while (true) {
                delay(500)
                onlinePlayers.forEach {
                    it.actionBar("Folge dem Dorfbewohner!")
                }
            }
        }
        gamemasterEntity.walkTo(location) {
            job.cancel()
            complete()
        }
        return this
    }
}
