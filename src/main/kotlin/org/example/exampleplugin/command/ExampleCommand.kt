package org.example.exampleplugin.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs

class ExampleCommand {

    fun register() = command("example") {
        runs {
            player.sendMessage("Hello, world!")
        }
    }
}
