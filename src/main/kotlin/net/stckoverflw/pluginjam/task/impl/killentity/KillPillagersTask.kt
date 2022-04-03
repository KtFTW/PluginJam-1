package net.stckoverflw.pluginjam.task.impl.killentity

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.KillEntityTask
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import org.bukkit.entity.EntityType
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class KillPillagersTask : KillEntityTask() {

    override fun start() {
        entities[EntityType.PILLAGER] = Random.Default.nextInt(pluginJamPlayers.size * 4, pluginJamPlayers.size * 8)

        // TODO: spawn pillagers, give equiqment, etc.

        super.start()
    }

    override fun stop() {
        super.stop()
    }

    override fun introduce() {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("", "<blue>Dorfbewohner</blue>", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
