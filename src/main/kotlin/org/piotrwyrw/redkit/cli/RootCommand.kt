package org.piotrwyrw.redkit.cli

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.piotrwyrw.redkit.extensions.error
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.service.CommandManager
import org.piotrwyrw.redkit.service.StateManager

class RootCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String?>
    ): Boolean {
        if (args.isEmpty()) {
            sender.info("/rk { ${CommandManager.instance.availableCommandLabels().joinToString(" | ")} }")
            return true
        }

        if (sender !is Player) {
            sender.error("Only players can use RedKit commands.")
            return true
        }

        val cmd = CommandManager.instance[args[0]!!]

        if (cmd == null) {
            sender.error("Unknown command: ${args[0]}")
            return true
        }

        if (cmd.requiresProject && StateManager.instance[sender].project == null) {
            sender.error("You need to attach a project before running this command.")
            return true
        }

        val status = cmd.command.run(args[0]!!, sender, args.drop(1).filterNotNull())
        if (status == ExecutionStatus.DISPLAY_USAGE)
            sender.error(cmd.commandController.description + " -- " + cmd.commandController.usage)

        return true
    }
}