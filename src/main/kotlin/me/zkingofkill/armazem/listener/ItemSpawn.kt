package me.zkingofkill.armazem.listener

import com.intellectualcrafters.plot.api.PlotAPI
import me.zkingofkill.armazem.drops
import me.zkingofkill.armazem.models.Item
import me.zkingofkill.armazem.models.SelableItem
import me.zkingofkill.armazem.models.User
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent

class ItemSpawn : Listener {

    @EventHandler
    fun onJoin(event: ItemSpawnEvent) {
        var defaultLocation = event.location
        val plot = PlotAPI().getPlot(defaultLocation)
        if (plot != null && plot.hasOwner()) {
            if (event.entity.itemStack != null) {
                val itemStack = event.entity.itemStack
                var user = User.get(Bukkit.getOfflinePlayer(plot.owner).name)
                for(location in arrayListOf(defaultLocation, defaultLocation.add(0.0,0.0,1.0),
                    defaultLocation.subtract(0.0,0.0,1.0))) {
                    ArrayList(drops).forEach { drop ->
                        if ((drop.location.blockX == location.blockX && drop.location.blockY == location.blockY &&
                                    drop.location.blockZ == location.blockZ)
                            && (drop.drop.durability == itemStack.durability && drop.drop.type == itemStack.type)
                        ) {
                            user = User.get(drop.player.name)
                            drops.remove(drop)
                        }
                    }
                }
                Item.list.forEach {
                    if (it.item.type == itemStack.type && it.item.durability == itemStack.durability) {
                        user.addItem(SelableItem(it.id, itemStack.amount))
                        event.isCancelled = true
                    }
                }
            }
        }
    }
}