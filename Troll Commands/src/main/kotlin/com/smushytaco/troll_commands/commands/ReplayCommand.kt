package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
object ReplayCommand: AbstractTrollCommand("replay", null, TrollCommands.REPLAY) {
    override fun condition() = TrollCommands.config.canBeReplayed
    init {
        trollCommands.add(this)
    }
}