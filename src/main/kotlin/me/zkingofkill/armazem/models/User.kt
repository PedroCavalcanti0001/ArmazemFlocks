package me.zkingofkill.armazem.models

import com.google.gson.annotations.SerializedName
import me.zkingofkill.armazem.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

data class User(
    @SerializedName("player")
    var player: String,
    @SerializedName("items")
    var items: ArrayList<SelableItem> = arrayListOf(),
    @SerializedName("friends")
    var friends:ArrayList<String> = arrayListOf()

) {
    fun addItem(item: SelableItem){
        val search = items.find { it.id == item.id }
        if(search == null){
            items.add(item)
        }else{
            search.amount = search.amount + item.amount
        }
    }

    fun itemById(id:Int):SelableItem{
        var search = items.find { it.id == id }
        return search ?: SelableItem(id, 0)
    }


    companion object {
        val list = arrayListOf<User>()

        fun get(player: String): User {
            lateinit var user: User
            val search = list.firstOrNull { it.player == player }
            user = search ?: Main.singleton.mysql.getUser(player)
            list.add(user)
            return user
        }

        fun saveAll() {
            for (it in ArrayList<User>(list)) {
                Main.singleton.mysql.upsertUser(it)
                if (Bukkit.getPlayer(it.player) == null) {
                    list.remove(it)
                }
            }
        }


        fun delayedSaveAll() {
            object : BukkitRunnable() {
                override fun run() {
                    try {
                        saveAll()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        println("[ArmazemFlocks] dados salvos com sucesso.")
                    }

                }
            }.runTaskTimerAsynchronously(Main.singleton, 20 * 60 * 5, 20 * 60 * 5)
        }

    }
}
