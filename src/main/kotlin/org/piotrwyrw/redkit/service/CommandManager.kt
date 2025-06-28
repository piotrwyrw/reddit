package org.piotrwyrw.redkit.service

import org.bukkit.command.CommandSender
import org.piotrwyrw.redkit.cli.Command
import org.piotrwyrw.redkit.cli.Controller
import org.piotrwyrw.redkit.cli.RequiresProject
import org.piotrwyrw.redkit.log
import org.reflections.Reflections

data class CommandWrapper(val command: Command, val requiresProject: Boolean, val commandController: Controller)

class CommandManager {
    companion object {
        val instance = CommandManager()
    }

    private val commands = mutableMapOf<String, CommandWrapper>()

    operator fun get(name: String) = commands[name]

    fun availableCommandLabels() = commands.keys.toList()

    fun availableCommands() = commands.values.toList()

    fun loadCommandHandlers() {
        val classes = Reflections("org.piotrwyrw.redkit.cli.commands").getTypesAnnotatedWith(Controller::class.java)
        classes.forEach { clazz ->
            val annotation = clazz.getAnnotation(Controller::class.java)!!
            try {
                clazz.getDeclaredMethod("run", String::class.java, CommandSender::class.java, List::class.java) == null
            } catch (_: Exception) {
                error("Could not register command")
            }
            commands[annotation.label] = CommandWrapper(
                clazz.getConstructor().newInstance() as Command,
                (clazz.getAnnotation(RequiresProject::class.java) != null),
                annotation
            )
            log("Registered RedKit Command: ${annotation.label}")
        }
    }
}