package com.smushytaco.troll_commands.commands
import com.smushytaco.troll_commands.TrollCommands
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
object HotDogCommand: AbstractTrollCommand("hotdog", "textures/hotdog_command/hot_dog.png") {
    override fun condition() = TrollCommands.config.canBeHotDogged
    init {
        trollCommands.add(this)
    }
}