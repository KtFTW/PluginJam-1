package net.stckoverflw.pluginjam.util

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team
import org.bukkit.scoreboard.Scoreboard as BukkitScoreboard

fun scoreboard(
    scoreboard: BukkitScoreboard = Bukkit.getScoreboardManager().newScoreboard,
    function: Scoreboard.() -> Unit
): Scoreboard {
    return Scoreboard(scoreboard).apply(function)
}

class Scoreboard(private val scoreboard: BukkitScoreboard = Bukkit.getScoreboardManager().newScoreboard) {
    val entries = hashMapOf<Int, Component>()
    var displayName: Component = Component.text("Scoreboard")
    private val objective: Objective = scoreboard.registerNewObjective("sidebar", "dummy", displayName)
        .apply { displaySlot = DisplaySlot.SIDEBAR }

    fun set(slot: Int, component: Component) {
        entries[slot] = component
        update()
    }

    fun set(slot: Int, string: String) {
        entries[slot] = mini(string)
        update()
    }

    fun update() {
        objective.displayName(displayName)
        entries.forEach { setEntry(it.key, it.value) }
    }

    private fun remove(scoreboard: BukkitScoreboard, slot: Int) {
        scoreboard.resetScores(createName(slot))
    }

    private fun createName(slot: Int): String {
        if (slot > 21) throw IllegalArgumentException("slot is greater than 21")
        return ChatColor.values()[slot].toString() + " " + ChatColor.values()[slot + 1]
    }

    private fun setEntry(slot: Int, component: Component) {
        createOrGetTeam(slot).prefix(component)
    }

    private fun createOrGetTeam(slot: Int): Team {
        val entry = createName(slot)
        var team = scoreboard.getEntryTeam(entry)
        if (team == null) {
            team = scoreboard.registerNewTeam("sidebar-$slot")
            team.addEntry(entry)
            scoreboard.getObjective("sidebar") !!.getScore(entry).score = slot
        }
        return team
    }

    fun applyScoreboard(player: Player) {
        player.scoreboard = scoreboard
    }

    fun removeScoreboard() {
        scoreboard.getObjective("sidebar")?.unregister()
    }
}
