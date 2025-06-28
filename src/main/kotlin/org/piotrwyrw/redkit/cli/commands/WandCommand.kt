package org.piotrwyrw.redkit.cli.commands

import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.cli.RequiresProject
import org.piotrwyrw.redkit.extensions.color
import org.piotrwyrw.redkit.service.StateManager

@Controller("wand", "Manage the RedKit wand", "/rk wand (enable | disable | toggle)")
@RequiresProject
class WandCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.isEmpty()) {
            val item = ItemStack(Material.BLAZE_ROD)
            val meta = item.itemMeta
            meta?.setDisplayName("&8» &cRedKit Wand &8«".color())
            item.itemMeta = meta
            (sender as Player).inventory.addItem(item)
            StateManager.instance[sender].enableWand()
            return ExecutionStatus.DONE
        }

        if (args.size != 1)
            return ExecutionStatus.DISPLAY_USAGE

        if (args.first() == "enable") {
            StateManager.instance[sender].enableWand()
            return ExecutionStatus.DONE
        }

        if (args.first() == "disable") {
            StateManager.instance[sender].disableWand()
            return ExecutionStatus.DONE
        }

        if (args.first() == "toggle") {
            StateManager.instance[sender].toggleWand()
            return ExecutionStatus.DONE
        }

        return ExecutionStatus.DISPLAY_USAGE
    }
}