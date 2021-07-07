package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollKickCommand
object CrashCommand: AbstractTrollKickCommand("crash", arrayOf("textures/crash_command/anus.jpg", "textures/crash_command/burned.jpg", "textures/crash_command/dog.jpg", "textures/crash_command/dog2.jpg", "textures/crash_command/dog3.jpg", "textures/crash_command/furries.jpeg", "textures/crash_command/goat.jpg", "textures/crash_command/hotdog.png", "textures/crash_command/sock.jpg")) {
    override fun condition() = TrollCommands.config.canBeCrashed
    init {
        trollCommands.add(this)
    }
}