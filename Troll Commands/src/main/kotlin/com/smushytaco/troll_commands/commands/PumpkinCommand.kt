package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
object PumpkinCommand: AbstractTrollCommand("pumpkin", null, TrollCommands.PUMPKIN) {
    override fun condition() = TrollCommands.config.canBePumpkined
    init {
        trollCommands.add(this)
    }
}