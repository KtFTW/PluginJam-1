package net.stckoverflw.pluginjam.task.impl.killentity

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.KillEntityTask
import net.stckoverflw.pluginjam.util.Conversation
import org.bukkit.entity.EntityType
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class KillPillagersTask : KillEntityTask() {

    override fun start() {
        entities[EntityType.PILLAGER] = Random.Default.nextInt(onlinePlayers.size * 4, onlinePlayers.size * 8)

        // TODO: spawn pillagers, give equiqment, etc.

        super.start()
    }

    override fun stop() {
        super.stop()
    }

    override fun introduce() {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("", "Gamemaster", 3.seconds)
            .addMessage("", "Gamemaster", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
