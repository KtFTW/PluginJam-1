package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
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
        count = Random.Default.nextInt(onlinePlayers.size * 12, onlinePlayers.size * 24)
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Wir brauchen auch noch ein wenig Holz.", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("Am besten bringt ihr mir einfach $count Eichenholz-logs", "<blue>Dorfbewohner</blue>", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
