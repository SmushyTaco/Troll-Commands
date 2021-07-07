package com.smushytaco.troll_commands.configuration_support
import com.smushytaco.troll_commands.TrollCommands
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
@Config(name = TrollCommands.MOD_ID)
class ModConfiguration: ConfigData {
    @Comment("Default value is yes. If set to yes you'll be able to be jumpscared. If set to no you won't be able to.")
    val canBeJumpscared = true
    @Comment("Default value is yes. If set to yes you'll be able to be hot dogged. If set to no you won't be able to.")
    val canBeHotDogged = true
    @Comment("Default value is yes. If set to yes you'll be able to be rick rolled. If set to no you won't be able to.")
    val canBeRickRolled = true
    @Comment("Default value is yes. If set to yes you'll be able to be among used. If set to no you won't be able to.")
    val canBeAmongUsed = true
    @Comment("Default value is yes. If set to yes you'll be able to be pumpkined. If set to no you won't be able to.")
    val canBePumpkined = true
    @Comment("Default value is yes. If set to yes you'll be able to be replayed. If set to no you won't be able to.")
    val canBeReplayed = true
    @Comment("Default value is yes. If set to yes you'll be able to be crashed. If set to no you won't be able to.")
    val canBeCrashed = true
}