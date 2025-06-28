package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.extensions.error
import org.piotrwyrw.redkit.service.ProjectManager
import org.piotrwyrw.redkit.service.StateManager
import org.piotrwyrw.redkit.extensions.success

@Controller("attach", "Attach to an existing project", "/rk attach <name>")
class AttachCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.size != 1)
            return ExecutionStatus.DISPLAY_USAGE

        if (ProjectManager.instance[args[0]] == null) {
            sender.error("The requested project does not exist.")
            return ExecutionStatus.DONE
        }

        StateManager.instance[sender].attachProject(args[0])

        return ExecutionStatus.DONE
    }
}