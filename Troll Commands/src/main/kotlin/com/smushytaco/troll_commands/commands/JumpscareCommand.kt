package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
object JumpscareCommand: AbstractTrollCommand("jumpscare", "textures/jumpscare_command/jumpscare.png", TrollCommands.JUMPSCARE) {
    override fun condition() = TrollCommands.config.canBeJumpscared
    init {
        trollCommands.add(this)
    }
}