package org.piotrwyrw.redkit.service

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.piotrwyrw.redkit.state.State
import java.util.*

class StateManager {
    companion object {
        val instance = StateManager()
    }

    private val data = mutableMapOf<UUID, State>()

    operator fun get(player: Player): State {
        if (data[player.uniqueId] == null)
            data[player.uniqueId] = State(player.uniqueId)

        return data[player.uniqueId]!!
    }

    operator fun get(sender: CommandSender) = get(sender as Player)

}