package me.zkingofkill.armazem

import inventory.InventoryManager
import me.zkingofkill.armazem.command.WarehouseCommand
import me.zkingofkill.armazem.data.Mysql
import me.zkingofkill.armazem.listener.BlockBreak
import me.zkingofkill.armazem.listener.ItemSpawn
import me.zkingofkill.armazem.models.Item
import me.zkingofkill.armazem.models.User
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Main : JavaPlugin() {

    lateinit var mysql: Mysql

    lateinit var inventoryManager: InventoryManager
    lateinit var economy: Economy

    companion object {
        lateinit var singleton: Main
    }

    override fun onEnable() {
        singleton = this
        mysql = Mysql()
        config.options().copyDefaults(true)
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
        }
        mysql.init()
        inventoryManager = InventoryManager(this)
        inventoryManager.init()
        Item.init()
        server.pluginManager.registerEvents(BlockBreak(), this)
        server.pluginManager.registerEvents(ItemSpawn(), this)
        User.delayedSaveAll()

        if (Bukkit.getServer().pluginManager.getPlugin("Vault") != null) {
            val rsp =
                Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)
            economy = rsp.provider
        }

        getCommand("armazem").executor = WarehouseCommand()

    }

    override fun onDisable() {
        User.saveAll()
    }

}