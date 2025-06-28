package org.piotrwyrw.redkit.extensions

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.piotrwyrw.redkit.RedKit
import java.io.File

fun String.color() = ChatColor.translateAlternateColorCodes('&', this)

private fun Player.send(value: String) {
    sendMessage("&b&lRK &8» &r$value".color())
}

fun Player.error(value: String) {
    send("&c$value")
}

fun CommandSender.error(value: String) = (this as Player).error(value)

fun Player.success(value: String) {
    send("&a$value")
}

fun CommandSender.success(value: String) = (this as Player).success(value)

fun Player.info(value: String) {
    send("&7$value")
}

fun CommandSender.info(value: String) = (this as Player).info(value)

fun String.inDataFolder() = File(RedKit.Companion.getInstance().dataFolder, this)
