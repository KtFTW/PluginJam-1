package net.stckoverflw.pluginjam.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.util.resetWorld

class ResetCommand {

    val command = command("reset") {
        requiresPermission("pluginjam.reset")
        runs {
            resetWorld("pluginjam")
        }
    }
}
