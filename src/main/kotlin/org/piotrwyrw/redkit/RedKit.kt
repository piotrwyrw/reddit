package org.piotrwyrw.redkit

import org.bukkit.plugin.java.JavaPlugin
import org.piotrwyrw.redkit.cli.RootCommand
import org.piotrwyrw.redkit.listeners.Listeners
import org.piotrwyrw.redkit.service.CommandManager
import org.piotrwyrw.redkit.service.ProjectManager

class RedKit : JavaPlugin() {

    companion object {
        private var instance: RedKit? = null
        fun getInstance(): RedKit {
            if (instance == null)
                throw RuntimeException("Plugin instance is not initialized yet")
            return instance!!
        }
    }

    override fun onEnable() {
        instance = this

        CommandManager.instance.loadCommandHandlers()
        getCommand("rk")!!.setExecutor(RootCommand())

        dataFolder.mkdirs()
        ProjectManager.instance.ensureProjectsDir()

        ProjectManager.instance.reloadProjects()

        server.pluginManager.registerEvents(Listeners(), this)

        log("RedKit is ready to go")
    }

    override fun onDisable() {
    }

}
