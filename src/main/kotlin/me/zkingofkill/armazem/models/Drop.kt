package me.zkingofkill.armazem.models

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class Drop(val player: Player, val location: Location, val drop:ItemStack)