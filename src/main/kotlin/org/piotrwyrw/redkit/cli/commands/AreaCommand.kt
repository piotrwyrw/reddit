package org.piotrwyrw.redkit.cli.commands

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.ExecutionStatus
import org.piotrwyrw.redkit.cli.RequiresProject
import org.piotrwyrw.redkit.extensions.asPoint
import org.piotrwyrw.redkit.extensions.error
import org.piotrwyrw.redkit.extensions.info
import org.piotrwyrw.redkit.extensions.success
import org.piotrwyrw.redkit.model.Area
import org.piotrwyrw.redkit.service.ProjectManager
import org.piotrwyrw.redkit.service.StateManager
import org.piotrwyrw.redkit.service.saveUpon

@RequiresProject
@Controller("area", "Various area utilities", "/rk area ( show | hide | 1 | 2 | commit )")
class AreaCommand : Command {

    override fun run(
        label: String,
        sender: CommandSender,
        args: List<String>
    ): ExecutionStatus {
        val state = StateManager.instance[sender]

        if (args.isEmpty()) {
            if (!state.isAreaComplete()) {
                sender.info("The area is incomplete")
                return ExecutionStatus.DONE
            }

            sender.info("X: &b${state.areaFrom!!.x.toInt()}&7, Y: &b${state.areaFrom!!.y.toInt()}&7, Z: &b${state.areaFrom!!.z.toInt()} &8 --&7 X: &b${state.areaTo!!.x.toInt()}&7, Y: &b${state.areaTo!!.y.toInt()}&7, Z: &b${state.areaTo!!.z.toInt()}")
            return ExecutionStatus.DONE
        }

        if (args.size == 1) {
            if (args[0] == "show") {
                state.showArea()
                return ExecutionStatus.DONE
            }

            if (args[0] == "hide") {
                state.hideArea()
                return ExecutionStatus.DONE
            }

            if (args[0] == "1") {
                state.setFirstAreaPoint((sender as Player).location)
                return ExecutionStatus.DONE
            }

            if (args[0] == "2") {
                state.setSecondAreaPoint((sender as Player).location)
                return ExecutionStatus.DONE
            }

            return ExecutionStatus.DISPLAY_USAGE
        }

        if (args.size == 2) {
            if (args[0] == "commit") {
                ProjectManager.instance.projectOf(sender)!! saveUpon { project ->
                    val state = StateManager.instance[sender]
                    val area = Area(state.areaFrom!!.asPoint(), state.areaTo!!.asPoint())
                    project.areas.put(args[1], area)
                }
                sender.success("Saved Area &7${args[1]}")
                return ExecutionStatus.DONE
            }

            return ExecutionStatus.DISPLAY_USAGE
        }

        return ExecutionStatus.DISPLAY_USAGE
    }

    override fun manualEntry(): String {
        return "&b/rk area show &7-- Show the current area boundary\n" +
                "&b/rk area hide &7-- Hide the current area boundary\n" +
                "&b/rk area 1 &7-- Set the first point of the current area\n" +
                "&b/rk area 2 &7-- Set the second point of the current area\n" +
                "&b/rk area commit <name> &7-- Save the current temporary area to the project\n"
    }
}