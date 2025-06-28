package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.service.CommandManager

@Controller("help", "Display a help page", "/rk help")
class HelpCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.isNotEmpty())
            return ExecutionStatus.DISPLAY_USAGE

        CommandManager.instance.availableCommands().forEach { cmd ->
            sender.info("&b${cmd.commandController.usage}&7 -- ${cmd.commandController.description}")
        }

        return ExecutionStatus.DONE
    }
}