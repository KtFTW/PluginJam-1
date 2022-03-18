package org.example.exampleplugin.listener

import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerJoinEvent

class ExampleListener {

    fun register() = listen<PlayerJoinEvent> {
        it.player.sendMessage("Hello, world!")
    }
}
