package net.stckoverflw.pluginjam.listener

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerJoinEvent

class ExampleListener {

    val listener: SingleListener<PlayerJoinEvent> = listen {
        it.player.sendMessage("Hello, world!")
    }
}
