package org.piotrwyrw.redkit.state

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.piotrwyrw.redkit.RedKit
import org.piotrwyrw.redkit.extensions.*
import java.util.*

data class State(
    val player: UUID,
    var project: String? = null,
    var areaFrom: Location? = null,
    var areaTo: Location? = null,
    var wandEnabled: Boolean = false
) {

    private var task: BukkitTask? = null

    private fun player() = Bukkit.getServer().getPlayer(player)!!

    fun enableWand() {
        player().success("Wand is now active. Right click two blocks to set area edges. Left click to toggle area visualisation.")
        wandEnabled = true
    }

    fun disableWand() {
        player().success("Wand is now disabled")
        wandEnabled = false
    }

    fun toggleWand() {
        if (wandEnabled)
            disableWand()
        else
            enableWand()
    }

    fun showArea() {
        if (task != null)
            return

        player().success("Showing current area preview")

        val runnable = object : BukkitRunnable() {
            override fun run() {
                if (isAreaComplete())
                    areaFrom!!.particleBox(areaTo!!, if (isOwnerInArea()) Color.LIME else Color.RED)
            }
        }

        task = runnable.runTaskTimer(RedKit.getInstance(), 0, 1)
    }

    fun hideArea() {
        if (task != null)
            player().success("Disabling area preview")

        task?.cancel()
        task = null
    }

    fun toggleArea() {
        if (task == null)
            showArea()
        else
            hideArea()
    }

    fun setFirstAreaPoint(point: Location) {
        player().success("Set first point of the area")
        areaFrom = point.round()
    }

    fun setSecondAreaPoint(point: Location) {
        player().success("Set second point of the area")
        areaTo = point.round()
    }

    fun setNextAreaPoint(point: Location) {
        if (point == areaTo)
            return

        if (isAreaComplete())
            clearArea()

        if (areaFrom == null) {
            setFirstAreaPoint(point)
            return
        }

        if (point == areaFrom)
            return

        setSecondAreaPoint(point)
    }

    fun clearArea() {
        areaFrom = null
        areaTo = null
    }

    fun attachProject(project: String) {
        this.project = project
        this.player().success("Attached to project &7$project")
        enableWand()
    }

    fun isAreaComplete() = areaFrom != null && areaTo != null

    fun isOwnerInArea(): Boolean {
        if (!isAreaComplete())
            return false

        val min = minimum(areaFrom!!, areaTo!!)
        val max = maximum(areaFrom!!, areaTo!!)
        val p = player().location

        return p.x in min.x..max.x && p.y in min.y..max.y && p.z in min.z..max.z
    }

}