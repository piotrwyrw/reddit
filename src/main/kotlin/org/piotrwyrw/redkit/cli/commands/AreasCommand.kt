package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.cli.RequiresProject
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.service.ProjectManager

@RequiresProject
@Controller("areas", "List all existing areas in the attached project", "/rk areas")
class AreasCommand : Command {
    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        val project = (ProjectManager.instance.projectOf(sender) ?: return ExecutionStatus.DONE).project

        if (project.areas.isEmpty()) {
            sender.info("This project does not contain area definitions.")
            return ExecutionStatus.DONE
        }

        project.areas.forEach { area ->
            sender.info("Area &b${area.key}&7: X: &b${area.value.from.x}&7, Y: &b${area.value.from.y}&7, Z: &b${area.value.from.z} &8-- &7X: &b${area.value.to.x}&7, Y: &b${area.value.to.y}&7, Z: &b${area.value.to.z}")
        }

        return ExecutionStatus.DONE
    }
}