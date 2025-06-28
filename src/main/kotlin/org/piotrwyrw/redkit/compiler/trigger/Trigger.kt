package org.piotrwyrw.redkit.compiler.trigger

import org.piotrwyrw.redkit.compiler.CommandBlock
import org.piotrwyrw.redkit.model.Project

interface Trigger<T> {

    fun parse(project: Project, options: Map<String, Object>): T

    fun compile(project: Project, data: T): CommandBlock?

}