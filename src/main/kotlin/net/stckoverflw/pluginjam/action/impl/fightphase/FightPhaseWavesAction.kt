package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.listen
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.main.KSpigotMainInstance
import net.axay.kspigot.runnables.sync
import net.kyori.adventure.bossbar.BossBar
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.mini
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.setOpenIfDoor
import org.bukkit.Difficulty
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.max
import kotlin.math.min

class FightPhaseWavesAction : Action(), ListenerHolder {
    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig

    override val listeners = mutableListOf<Listener>()
    private var totalWaves: Int = max(2, min(pluginJamPlayers.size, 4))
    private var currentWave: Int = 1
    private val bossbar: BossBar = BossBar.bossBar(
        mini("<green> Wave <red>$currentWave<gray>/<red>$totalWaves"),
        0f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS
    )

    override fun execute(): Action {
        positionsConfig.getLocation("fight_door").block.setOpenIfDoor(false)
        val spawnPosition = positionsConfig.getLocation("fight_pillager_spawn")
        pluginJamPlayers.forEach {
            giveItems(it)
            it.showBossBar(bossbar)
        }

        sync {
            KSpigotMainInstance.server.worlds.forEach {
                it.difficulty = Difficulty.EASY
            }
            spawnPillagers(spawnPosition)
        }

        var count = 0

        addListener(
            listen<BlockBreakEvent>(EventPriority.HIGHEST) {
                it.isCancelled = true
            }
        )

        addListener(
            listen<PlayerDeathEvent> {
                it.player.reset()
                giveItems(it.player)
                it.player.teleport(positionsConfig.getLocation("fight_parkour_end"))
                it.isCancelled = true
            }
        )

        addListener(
            listen<EntityDamageByEntityEvent>(EventPriority.HIGHEST) { event ->
                event.isCancelled = event.entity is Player && event.damager is Player
            }
        )

        addListener(
            listen<EntityDeathEvent> { event ->
                val entity = event.entity

                if (entity.type != EntityType.PILLAGER) return@listen

                ArrayList(event.drops)
                    .forEach { item ->
                        event.drops.remove(item)
                    }

                count += 1

                if (count == 10) {
                    count = 0
                    if (currentWave == totalWaves) {
                        complete()
                        return@listen
                    }
                    currentWave += 1
                    spawnPillagers(spawnPosition)
                    updateBossbar()
                }
            }
        )

        addListener(
            listen<PlayerDropItemEvent> {
                it.isCancelled = true
            }
        )

        addListener(
            listen<PlayerInteractEvent>(EventPriority.HIGHEST) { event ->
                val type = event.clickedBlock?.type ?: return@listen
                if (type != Material.SPRUCE_DOOR) return@listen

                event.isCancelled = true
            }
        )

        addListener(
            listen<PlayerInteractAtEntityEvent>(EventPriority.HIGHEST) { event ->
                if (event.rightClicked is ItemFrame) event.isCancelled = false
            }
        )

        addListener(
            listen<PlayerQuitEvent> {
                it.player.hideBossBar(bossbar)
            }
        )

        return this
    }

    private fun updateBossbar() {
        bossbar.progress((currentWave - 1).toFloat() / totalWaves.toFloat())
        bossbar.name(mini("<green> Wave <red>$currentWave<gray>/<red>$totalWaves"))
    }

    private fun giveItems(player: Player) {
        player.inventory.apply {
            clear()
            addItem(
                ItemStack(Material.DIAMOND_SWORD),
                itemStack(Material.BOW) {
                    meta {
                        addEnchant(Enchantment.ARROW_INFINITE, 1, false)
                    }
                },
                ItemStack(Material.ARROW, 1),
                ItemStack(Material.GOLDEN_APPLE)
            )

            setItemInOffHand(ItemStack(Material.SHIELD))
            helmet = ItemStack(Material.IRON_HELMET)
            chestplate = ItemStack(Material.IRON_CHESTPLATE)
            leggings = ItemStack(Material.IRON_LEGGINGS)
            boots = ItemStack(Material.IRON_BOOTS)
        }
    }

    private fun spawnPillagers(location: Location) {
        for (i in 1..10) {
            location.world.spawnEntity(location, EntityType.PILLAGER)
        }
    }

    override fun complete() {
        unregisterAllListeners()
        pluginJamPlayers.forEach {
            it.inventory.clear()
            it.reset()
            it.hideBossBar(bossbar)
        }
        super.complete()
    }
}
