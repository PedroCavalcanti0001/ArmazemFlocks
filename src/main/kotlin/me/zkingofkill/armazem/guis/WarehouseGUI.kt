package me.zkingofkill.armazem.guis

import inventory.ClickableItem
import inventory.SmartInventory
import inventory.content.InventoryContents
import inventory.content.InventoryProvider
import me.zkingofkill.armazem.Main
import me.zkingofkill.armazem.models.Item
import me.zkingofkill.armazem.models.User
import me.zkingofkill.armazem.utils.extra.formatMoney
import org.bukkit.Material
import org.bukkit.entity.Player
import utils.ItemStackBuilder
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList


class WarehouseGUI(val player: Player, val user: User = User.get(player.name)) : InventoryProvider {
    var page = 1
    private val cf = Main.singleton.config
    private val title = cf.getString("guis.warehouse.title").replace("&", "§")
        .replace("{page}", page.toString())

    private val rows = cf.getInt("guis.warehouse.rows")

    private var inventory: SmartInventory = SmartInventory.builder()
        .size(rows, 9)
        .title(title)
        .provider(this)
        .build()

    override fun init(player: Player, contents: InventoryContents) {
        val items = user.items
        val allAmount = items.sumBy { it.amount }
        val allTotal = items.sumByDouble { Item.byId(it.id).unitPrice * it.amount }
        var formatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("pt", "BR")))
        val sellAllLore = ArrayList<String>(cf.getStringList("guis.warehouse.buttons.sellAll.item.lore"))

        if (!player.hasPermission("armazemplots.vendertodos")) {
            sellAllLore.add(" ")
            sellAllLore.add("§cSem permissão.")
        }
        contents.set(cf.getInt("guis.warehouse.buttons.sellAll.position.row"),
            cf.getInt("guis.warehouse.buttons.sellAll.position.colunm"),
            ClickableItem.of(
                ItemStackBuilder(
                    Material.getMaterial(
                        cf.getInt("guis.warehouse.buttons.sellAll.item.id")
                    )
                )
                    .setName(cf.getString("guis.warehouse.buttons.sellAll.item.name").replace("&", "§"))
                    .setDurability(cf.getInt("guis.warehouse.buttons.sellAll.item.date"))
                    .setLore(sellAllLore
                        .map { it.replace("&", "§") }
                        .map { it.replace("%total%", allTotal.formatMoney()) }
                        .map { it.replace("%amount%", allAmount.toString()) })
                    .build()
            ) {
                if (player.hasPermission("armazemplots.vendertodos")) {
                    if(allTotal > 0) {
                        player.sendMessage(
                            cf.getString("messages.dropsSold")
                                .replace("&", "§")
                                .replace("%total%", allTotal.formatMoney())
                                .replace("%amount%", allAmount.toString())
                        )
                        user.items.forEach { it.amount = 0 }
                        Main.singleton.economy.depositPlayer(player, allTotal)
                        open()
                    }else{
                        player.sendMessage(
                            cf.getString("messages.noItemsToSell")
                                .replace("&", "§")
                        )
                    }
                }
            })

        var limit = 10
        for ((index, item) in Item.list.withIndex()) {
            val selableItem = user.itemById(item.id)
            val total = selableItem.amount * item.unitPrice
            val itemStack = ItemStackBuilder(item.item.clone()).addLore(
                " ",
                "§aQuantidade: §f${selableItem.amount}",
                "§aValor total: §f${total.formatMoney()}",
                " ",
                "§aClique para vender!"
            ).build()
            contents.set(
                item.itemRow,
                item.itemCollunm,
                ClickableItem.of(itemStack) {
                    if (total > 0) {
                        player.sendMessage(
                            cf.getString("messages.dropSold")
                                .replace("&", "§")
                                .replace("%itemName%", item.item.type.name)
                                .replace("%total%", total.formatMoney())
                                .replace("%amount%", selableItem.amount.toString())
                        )
                        Main.singleton.economy.depositPlayer(player, total)
                        selableItem.amount = 0
                        open()
                    } else {
                        player.sendMessage(
                            cf.getString("messages.noItemsToSell")
                                .replace("&", "§")
                        )
                    }
                })
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {

    }

    fun open() {
        inventory.open(player)
    }

    fun close() {
        inventory.close(player)
    }

}