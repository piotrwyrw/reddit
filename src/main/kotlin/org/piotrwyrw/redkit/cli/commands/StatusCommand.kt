package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.service.StateManager

@Controller("status", "Display your personal RedKit status", "/rk status")
class StatusCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.isNotEmpty())
            return ExecutionStatus.DISPLAY_USAGE

        val state = StateManager.Companion.instance[sender]

        if (state.project == null) {
            sender.info("You do not have an active project.")
            return ExecutionStatus.DONE
        }

        sender.info("Active project: &b${state.project}")

        return ExecutionStatus.DONE
    }
}