package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.extensions.color
import org.piotrwyrw.redkit.extensions.error
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.service.CommandManager

@Controller("man", "Display manual entry for a subcommand", "/rk man <command>")
class ManualCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.size != 1)
            return ExecutionStatus.DISPLAY_USAGE

        val cmd = CommandManager.instance[args[0]]
        if (cmd == null) {
            sender.error("This subcommand could not be found.")
            return ExecutionStatus.DONE
        }

        val man = cmd.command.manualEntry()
        sender.info("&8---&7 Manual entry for &b/rk ${args[0]}&8 ---")
        sender.sendMessage("&7$man".color())
        sender.info("&8---&7 Manual entry for &b/rk ${args[0]}&8 ---")
        return ExecutionStatus.DONE
    }
}