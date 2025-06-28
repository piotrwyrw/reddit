package org.piotrwyrw.redkit.compiler.trigger.triggers

import org.piotrwyrw.redkit.compiler.CommandBlock
import org.piotrwyrw.redkit.compiler.CommandBlockType
import org.piotrwyrw.redkit.compiler.commandBlock
import org.piotrwyrw.redkit.compiler.trigger.Trigger
import org.piotrwyrw.redkit.compiler.trigger.TriggerController
import org.piotrwyrw.redkit.model.Area
import org.piotrwyrw.redkit.model.Project
import org.piotrwyrw.redkit.model.TriggerCause

enum class EnterAreaDataSubjectType {
    PLAYER,
    ENTITY,
    RANDOM
}

data class EnterAreaData(val area: Area, val subject: EnterAreaDataSubjectType = EnterAreaDataSubjectType.PLAYER)

@TriggerController(TriggerCause.ENTER_AREA)
class EnterAreaTrigger : Trigger<EnterAreaData> {
    override fun parse(
        project: Project,
        options: Map<String, Object>
    ): EnterAreaData {
        val areaName = options["area"]
        if (options.size !in 1..2 || areaName == null)
            throw RuntimeException("Invalid trigger options: Expected 'area' key.")

        if (areaName !is String)
            throw RuntimeException("Invalid trigger options: Expected 'area' key to be the area name.")

        val area = project.areas[areaName]
        if (area == null)
            throw RuntimeException("Invalid trigger options: The requested area '$areaName' does not exist.")

        var subjectStr = options["subject"]

        if (options.size == 2 && subjectStr == null)
            throw RuntimeException("Invalid trigger options: Expected 'subject'")

        var subject: EnterAreaDataSubjectType = EnterAreaDataSubjectType.PLAYER

        if (subjectStr != null)
            subject = EnterAreaDataSubjectType.valueOf(subjectStr as String)

        return EnterAreaData(area)
    }

    override fun compile(
        project: Project,
        data: EnterAreaData
    ): CommandBlock? {
        return commandBlock {
            type = CommandBlockType.CONTINUOUS_ENTRY
            command = "execute if entity @a[${data.area.selectorArea()}]"
        }
    }
}