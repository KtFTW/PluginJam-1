package net.stckoverflw.pluginjam.action.impl.global

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.util.mini
import net.stckoverflw.pluginjam.util.pluginJamPlayers
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
                pluginJamPlayers.forEach {
                    it.sendActionBar(mini("<green><tr:walking_follow>"))
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
