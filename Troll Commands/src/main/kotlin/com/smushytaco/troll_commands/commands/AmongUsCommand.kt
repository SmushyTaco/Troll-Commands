package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
object AmongUsCommand: AbstractTrollCommand("amongus", arrayOf("textures/amongus_command/among_us.png")) {
    override fun condition() = TrollCommands.config.canBeAmongUsed
    init {
        trollCommands.add(this)
    }
}