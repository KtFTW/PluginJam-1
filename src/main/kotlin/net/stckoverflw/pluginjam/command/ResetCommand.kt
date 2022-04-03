package net.stckoverflw.pluginjam.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.util.resetWorld

class ResetCommand {

    val command = command("reset") {
        runs {
            resetWorld("pluginjam")
        }
    }
}
