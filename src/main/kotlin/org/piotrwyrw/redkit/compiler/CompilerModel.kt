package org.piotrwyrw.redkit.compiler

enum class CommandBlockFlowType {
    IMPULSE,
    CHAIN,
    REPEAT
}

data class CommandBlockType(val flowType: CommandBlockFlowType, val conditional: Boolean, val needsRedstone: Boolean) {
    companion object {
        val SINGLE_ENTRY = CommandBlockType(CommandBlockFlowType.IMPULSE, conditional = false, needsRedstone = false)
        val CONTINUOUS_ENTRY = CommandBlockType(CommandBlockFlowType.REPEAT, conditional = false, needsRedstone = false)
        val REGULAR_CHAIN = CommandBlockType(CommandBlockFlowType.CHAIN, conditional = true, needsRedstone = false)
    }
}

data class GridLocation(val x: Int, val y: Int, val z: Int)

class CommandBlock {
    lateinit var type: CommandBlockType
    lateinit var command: String
    var next: CommandBlock? = null
    var location: GridLocation? = null

    fun success(config: CommandBlock.() -> Unit) {
        this.next = commandBlock(config)
    }
}

fun commandBlock(config: CommandBlock.() -> Unit): CommandBlock {
    val commandBlock = CommandBlock()
    commandBlock.config()
    return commandBlock
}
