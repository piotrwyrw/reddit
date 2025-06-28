package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.service.ProjectManager

@Controller("projects", "List existing projects", "/rk projects")
class ProjectsCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        if (args.isNotEmpty())
            return ExecutionStatus.DISPLAY_USAGE

        val projects = ProjectManager.instance.listAllProjects()

        if (projects.isEmpty()) {
            sender.info("There are no projects.")
            return ExecutionStatus.DONE
        }

        projects.forEach {
            sender.info("Project &b${it.name}&7 with &b${it.flowSize}&7 flow node(s) and &b${it.areaCount}&7 area(s).")
        }

        return ExecutionStatus.DONE
    }
}