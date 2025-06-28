package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.extensions.error
import org.piotrwyrw.redkit.service.ProjectManager
import org.piotrwyrw.redkit.extensions.success
import org.piotrwyrw.redkit.service.StateManager

@Controller("init", "Create a new project", "/rk init <name>")
class InitCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.size != 1)
            return ExecutionStatus.DISPLAY_USAGE

        if (!ProjectManager.instance.createProject(args[0])) {
            sender.error("Could not create project")
            return ExecutionStatus.DONE
        }

        sender.success("Created project &7${args[0]}")

        StateManager.instance[sender].attachProject(args[0])

        return ExecutionStatus.DONE
    }
}