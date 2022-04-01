package net.stckoverflw.pluginjam.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.minecraft.commands.CommandSourceStack

class ExampleCommand {

    val command: LiteralArgumentBuilder<CommandSourceStack> = command("example") {
        runs {
            player.sendMessage("Hello, world!")
        }
    }
}
