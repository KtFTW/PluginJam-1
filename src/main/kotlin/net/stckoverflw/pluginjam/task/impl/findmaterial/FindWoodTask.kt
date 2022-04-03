package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import org.bukkit.Material
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class FindWoodTask : FindMaterialTask() {

    private var count: Int = 10

    override fun start() {
        materials[Material.OAK_LOG] = count

        super.start()
    }

    override fun introduce() {
        count = Random.Default.nextInt(pluginJamPlayers.size * 12, pluginJamPlayers.size * 24)
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Wir brauchen auch noch ein wenig Holz.", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("Am besten bringt ihr mir einfach $count Eichenholz-logs", "<blue>Dorfbewohner</blue>", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
