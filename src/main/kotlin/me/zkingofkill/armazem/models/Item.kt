package me.zkingofkill.armazem.models

import me.zkingofkill.armazem.Main
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import utils.ItemStackBuilder

data class Item(
    val id: Int,
    val item: ItemStack,
    val unitPrice: Double,
    val itemRow: Int,
    val itemCollunm: Int
) {



    companion object {
        var list = arrayListOf<Item>()
        fun init() {
            val config = Main.singleton.config
            for (item in config.getConfigurationSection("drops").getKeys(false)) {
                val unitPrice = config.getDouble("drops.$item.price")
                val itm = config.getString("drops.$item.item")

                val args = itm.split(":")
                val itemId =  if(args.size == 2) args[0].toInt() else itm.toInt()
                val itemDate = if(args.size == 2) args[1].toInt() else 0
                val itemStack = ItemStackBuilder(Material.getMaterial(itemId))
                    .setDurability(itemDate).build()
                val itemRow = config.getInt("drops.$item.position.row")
                val itemCollunm = config.getInt("drops.$item.position.collunm")
                list.add(Item(itemId, itemStack, unitPrice, itemRow, itemCollunm))
            }
        }
        fun byId(id: Int): Item {
            return list.find { it.id == id }!!
        }
    }
}