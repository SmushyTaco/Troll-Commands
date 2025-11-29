package com.smushytaco.troll_commands
import io.wispforest.owo.config.annotation.Config
import io.wispforest.owo.config.annotation.Modmenu
@Modmenu(modId = TrollCommands.MOD_ID)
@Config(name = TrollCommands.MOD_ID, wrapperName = "ModConfig")
@Suppress("UNUSED")
class ModConfiguration {
    @JvmField
    var canBeJumpscared = true
    @JvmField
    var canBeHotDogged = true
    @JvmField
    var canBeRickRolled = true
    @JvmField
    var canBeAmongUsed = true
    @JvmField
    var canBePumpkined = true
    @JvmField
    var canBeReplayed = true
    @JvmField
    var canBeCrashed = true
}