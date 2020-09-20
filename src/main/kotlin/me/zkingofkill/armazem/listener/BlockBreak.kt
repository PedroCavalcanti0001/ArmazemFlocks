package me.zkingofkill.armazem.listener

import com.intellectualcrafters.plot.api.PlotAPI
import me.zkingofkill.armazem.Main
import me.zkingofkill.armazem.drops
import me.zkingofkill.armazem.models.Drop
import me.zkingofkill.armazem.models.Item
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreak : Listener {

    @EventHandler
    fun onJoin(event: BlockBreakEvent) {
        if (event.isCancelled) return
        val location = event.block.location
        val plot = PlotAPI().getPlot(location)
        if (plot != null && plot.hasOwner()) {
            val player = event.player
            event.block.getDrops(player.itemInHand).forEach { itemStack ->
                Item.list.forEach {
                    if (it.item.type == itemStack.type && it.item.durability == itemStack.durability) {
                        val drop = Drop(player = player, location = location, drop = itemStack)
                        drops.add(drop)
                        Bukkit.getScheduler().runTaskLater(Main.singleton, {
                            drops.removeIf { drops.contains(drop) }
                        }, 25)
                    }
                }
            }
        }
    }
}