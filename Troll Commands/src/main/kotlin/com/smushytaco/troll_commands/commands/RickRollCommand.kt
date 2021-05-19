package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
object RickRollCommand: AbstractTrollCommand("rickroll", null, TrollCommands.RICK_ROLL) {
    override fun condition() = TrollCommands.config.canBeRickRolled
    init {
        trollCommands.add(this)
    }
}