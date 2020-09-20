package me.zkingofkill.armazem.command

import me.zkingofkill.armazem.Main
import me.zkingofkill.armazem.guis.WarehouseGUI
import me.zkingofkill.armazem.models.User
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class
WarehouseCommand : CommandExecutor {
    private val cf = Main.singleton.config

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            WarehouseGUI(sender).open()
            return true
        } else {
            if (args[0].equals("amigos", true)) {
                val user = User.get(sender.name)
                val friends = user.friends.joinToString(separator = "§8, §7")
                sender.sendMessage("§aAmigos: §7$friends")
                return true
            }
            if (args[0].equals("add", true)) {
                if (args.size >= 2) {
                    if (Bukkit.getOfflinePlayer(args[1]) != null) {
                        val user = User.get(sender.name)
                        val target = Bukkit.getOfflinePlayer(args[1])
                        user.friends.add(target.name)
                        sender.sendMessage(
                            cf.getString("messages.friendAdded")
                                .replace("&", "§")
                                .replace("%name%", target.name)
                        )
                    } else {
                        sender.sendMessage(
                            cf.getString("messages.playerNotFound")
                                .replace("&", "§")
                        )
                    }
                } else {
                    sender.sendMessage("§cUse /armazem add <jogador>")
                }
                return true
            }
            if (args[0].equals("rem", true)) {
                if (args.size >= 2) {
                    if (Bukkit.getOfflinePlayer(args[1]) != null) {
                        val user = User.get(sender.name)
                        val target = Bukkit.getOfflinePlayer(args[1])
                        if(user.friends.contains(target.name)) {
                            user.friends.remove(target.name)
                            sender.sendMessage(
                                cf.getString("messages.friendRem")
                                    .replace("&", "§")
                                    .replace("%name%", target.name)
                            )
                        }else{
                            sender.sendMessage(
                                cf.getString("messages.friendNotFound")
                                    .replace("&", "§")
                            )
                        }
                    } else {
                        sender.sendMessage(
                            cf.getString("messages.playerNotFound")
                                .replace("&", "§")
                        )
                    }
                } else {
                    sender.sendMessage("§cUse /armazem rem <jogador>")
                }
                return true
            }
        }
        if(Bukkit.getOfflinePlayer(args[0]) != null){
            val target = Bukkit.getOfflinePlayer(args[0])
            val targetUser = User.get(target.name)
            if (sender.hasPermission("armazemplots.staff") || targetUser.friends.contains(sender.name)) {
                WarehouseGUI(sender, targetUser).open()
            } else {
                sender.sendMessage(
                    cf.getString("messages.noPermission").replace("&", "§")
                )
            }
        }

        return false
    }
}