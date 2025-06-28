package org.piotrwyrw.redkit.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.piotrwyrw.redkit.service.StateManager

class Listeners : Listener {

    @EventHandler
    fun rightClickBlock(event: PlayerInteractEvent) {
        val state = StateManager.instance[event.player]

        if (state.project == null)
            return

        val inventory = event.player.inventory

        if (inventory.getItem(inventory.heldItemSlot)?.type != Material.BLAZE_ROD || !state.wandEnabled)
            return

        if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) {
            state.toggleArea()
            event.isCancelled = true
            return
        }

        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return

        val point = event.clickedBlock!!.location

        state.setNextAreaPoint(point)
    }
}