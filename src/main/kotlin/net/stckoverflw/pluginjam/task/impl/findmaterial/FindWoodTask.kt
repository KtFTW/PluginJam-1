package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
import org.bukkit.Material
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class FindWoodTask: FindMaterialTask() {

    override fun start() {
        materials[Material.OAK_WOOD] = Random.Default.nextInt(onlinePlayers.size * 12, onlinePlayers.size * 24)

        super.start()
    }

    override fun stop() {
        super.stop()
    }

    override fun introduce() {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Wir brauchen auch noch ein wenig Holz.", "Gamemaster", 3.seconds)
            .addMessage("Am besten bringt ihr mir einfach Eichenholz", "Gamemaster", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
