package org.piotrwyrw.redkit.cli

import org.bukkit.command.CommandSender

enum class ExecutionStatus {
    DONE,
    DISPLAY_USAGE
}

interface Command {

    fun manualEntry(): String = "No manual entry for this command"

    fun run(label: String, sender: CommandSender, args: List<String>): ExecutionStatus

}