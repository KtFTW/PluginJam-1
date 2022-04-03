package net.stckoverflw.pluginjam.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager

class SkipPhaseCommand {

    val command = command("skip-phase") {
        requiresPermission("pluginjam.skip-phase")
        runs {
            GamePhaseManager.nextPhase()
        }
    }
}
