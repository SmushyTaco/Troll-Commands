package com.smushytaco.troll_commands
import com.smushytaco.troll_commands.commands.*
import com.smushytaco.troll_commands.commands.base.AbstractTrollCommand
import com.smushytaco.troll_commands.configuration_support.ModConfiguration
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
object TrollCommands : ModInitializer {
    const val MOD_ID = "troll_commands"
    lateinit var config: ModConfiguration
        private set
    private val JUMPSCARE_IDENTIFIER = Identifier(MOD_ID, "jumpscare")
    val JUMPSCARE = SoundEvent(JUMPSCARE_IDENTIFIER)
    private val RICK_ROLL_IDENTIFIER = Identifier(MOD_ID, "rick_roll")
    val RICK_ROLL = SoundEvent(RICK_ROLL_IDENTIFIER)
    private val PUMPKIN_IDENTIFIER = Identifier(MOD_ID, "pumpkin")
    val PUMPKIN = SoundEvent(PUMPKIN_IDENTIFIER)
    private val REPLAY_IDENTIFIER = Identifier(MOD_ID, "replay")
    val REPLAY = SoundEvent(REPLAY_IDENTIFIER)
    override fun onInitialize() {
        Registry.register(Registry.SOUND_EVENT, JUMPSCARE_IDENTIFIER, JUMPSCARE)
        Registry.register(Registry.SOUND_EVENT, RICK_ROLL_IDENTIFIER, RICK_ROLL)
        Registry.register(Registry.SOUND_EVENT, PUMPKIN_IDENTIFIER, PUMPKIN)
        Registry.register(Registry.SOUND_EVENT, REPLAY_IDENTIFIER, REPLAY)
        AutoConfig.register(ModConfiguration::class.java) { definition: Config, configClass: Class<ModConfiguration> ->
            GsonConfigSerializer(definition, configClass)
        }
        config = AutoConfig.getConfigHolder(ModConfiguration::class.java).config
        JumpscareCommand
        HotDogCommand
        RickRollCommand
        AmongUsCommand
        PumpkinCommand
        ReplayCommand
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _ ->
            AbstractTrollCommand.trollCommands.forEach {
                dispatcher.register(CommandManager.literal(it.command).executes(it))
            }
        })
    }
}