package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.extensions.geometry.plus
import net.axay.kspigot.extensions.geometry.vecY
import net.axay.kspigot.extensions.geometry.withWorld
import net.axay.kspigot.runnables.task
import net.axay.kspigot.structures.MaterialCircle
import net.axay.kspigot.utils.editMeta
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.broadcastMini
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.event.Listener
import kotlin.random.Random

object EndPhase : GamePhase(null), ListenerHolder {
    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()
    private val fireworkLocations = mutableListOf<Location>()

    override fun start() {
        val world = Bukkit.getWorld("pluginjam")!!
        pluginJamPlayers.forEach {
            it.teleportAsyncBlind(world.spawnLocation.plus(vecY(1)))
            it.playSound(it.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
            // TODO: Timer
        }

        broadcastMini("<i><tr:end_broadcast>")

        MaterialCircle(10, Material.AIR).edgeStructure.structureData
            .map { it.location.withWorld(world).plus(world.spawnLocation.toVector().plus(vecY(1))) }
            .filter { it.block.type.hardness <= 0.2F && it.block.location.plus(vecY(1)).block.type.hardness <= 0.2F }
            .forEach {
                world.spawnEntity(it, EntityType.VILLAGER)
            }
        fireworkLocations.apply {
            add(positionsConfig.getLocation("end_firework_0"))
            add(positionsConfig.getLocation("end_firework_1"))
            add(positionsConfig.getLocation("end_firework_2"))
            add(positionsConfig.getLocation("end_firework_3"))
            add(positionsConfig.getLocation("end_firework_4"))
        }

        fireworkLocations.forEach {
            it.block.type = Material.LIGHTNING_ROD
        }

        val howOften = 25L

        task(
            sync = false,
            howOften = howOften,
            period = 20L,
        ) {
            task(sync = true) {
                fireworkLocations.forEach {
                    val firework = world.spawnEntity(it.plus(vecY(1)), EntityType.FIREWORK, true) as Firework
                    firework.editMeta {
                        power = Random.nextInt(1, 2)
                        addEffect(
                            FireworkEffect.builder().withFlicker().withColor(Color.GREEN).trail(true).withFade(
                                listOf(Color.BLUE, Color.RED, Color.YELLOW)
                            ).build()
                        )
                    }
                }
            }
        }
        task(
            sync = false,
            delay = 20L * (howOften + 1),
        ) {
            task(sync = true) {
                GamePhaseManager.nextPhase()
            }
        }
    }

    override fun end() {
        fireworkLocations.forEach {
            it.block.type = Material.AIR
        }
    }
}
