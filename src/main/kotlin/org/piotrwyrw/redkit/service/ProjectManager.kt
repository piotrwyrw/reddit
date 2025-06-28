package org.piotrwyrw.redkit.service

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.piotrwyrw.redkit.error
import org.piotrwyrw.redkit.extensions.inDataFolder
import org.piotrwyrw.redkit.log
import org.piotrwyrw.redkit.model.Project
import org.piotrwyrw.redkit.model.ProjectStats
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

class NamedProject(val name: String, val project: Project)

infix fun NamedProject.saveUpon(lambda: (Project) -> Unit) {
    lambda(this.project)
    ProjectManager.instance.saveProject(this.name)
}

class ProjectManager {
    companion object {
        const val PROJECT_DIR = "projects"
        val PROJECT_NAME_PATTERN = "[a-zA-Z\\-_]+".toRegex()

        val instance = ProjectManager()
    }

    private val projects = mutableMapOf<String, Project>()

    operator fun get(name: String): NamedProject? {
        return NamedProject(name, projects[name] ?: return null)
    }

    fun listAllProjects(): List<ProjectStats> = projects.map {
        ProjectStats(it.key, it.value.flow.size, it.value.areas.size)
    }

    fun ensureProjectsDir() {
        val dir = PROJECT_DIR.inDataFolder()
        if (!dir.exists())
            dir.mkdirs()
    }

    fun createProject(name: String): Boolean {
        if (!name.matches(PROJECT_NAME_PATTERN))
            return false

        if (projects.containsKey(name))
            return false

        val project = Project()
        project.flow = mutableListOf()
        project.areas = mutableMapOf()
        projects[name] = project
        saveProject(name)

        return true
    }

    fun saveProject(name: String, project: Project) {
        val yaml = Yaml(DumperOptions().apply { isPrettyFlow = true })
        val file = "$PROJECT_DIR/$name.yaml".inDataFolder()
        file.writeBytes(yaml.dumpAsMap(project).toByteArray())
    }

    fun saveProject(name: String) {
        val project = projects[name] ?: return
        saveProject(name, project)
    }

    fun reloadProjects(): Boolean {
        projects.clear()
        val yaml = Yaml(Constructor(Project::class.java, LoaderOptions()))
        val dir = PROJECT_DIR.inDataFolder()
        if (!dir.isDirectory) {
            error("Could not find RedKit project directory.")
            return false
        }
        var errors = 0
        val files = dir.listFiles {
            it.isFile && (it.name.endsWith(".yaml") || it.name.endsWith(".yml"))
                    && it.nameWithoutExtension.matches(PROJECT_NAME_PATTERN)
        }
        if (files == null || files.isEmpty()) {
            log("No projects to load.")
            return true
        }
        files.forEach {
            val stream = it.inputStream()
            try {
                val project = yaml.load<Project>(stream)
                projects[it.nameWithoutExtension] = project
            } catch (e: Exception) {
                error("Could not load project from file  \"${it.path}\": ${e.message}")
                errors++
            }
        }
        log("Successfully loaded ${projects.size} projects${if (errors > 0) " - Failed to load $errors projects." else "."}")
        return true
    }

    fun projectOf(player: Player): NamedProject? {
        val project = StateManager.instance[player].project ?: return null
        return this[project]!!
    }

    fun projectOf(sender: CommandSender): NamedProject? = projectOf(sender as Player)

}