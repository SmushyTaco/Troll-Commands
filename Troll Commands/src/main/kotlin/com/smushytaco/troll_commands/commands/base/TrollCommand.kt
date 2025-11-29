package com.smushytaco.troll_commands.commands.base
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.smushytaco.troll_commands.TrollCommands
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
open class TrollCommand private constructor(private val command: String, val condition: BooleanReturn, private val imagePaths: Array<String>?, val sound: SoundEvent? = null, private val isSubCommand: Boolean) : Command<CommandSourceStack> {
    constructor(command: String, condition: BooleanReturn, imagePaths: Array<String>?, sound: SoundEvent? = null) : this(command, condition, imagePaths, sound, false)
    companion object {
        private const val ARGUMENT_KEY = "name"
        fun TrollCommand.resetAllExcept() {
            TrollCommands.trollCommands.forEach {
                if (it != this) it.isBeingTrolled = false
            }
        }
    }
    var isBeingTrolled = false
        set(value) {
            field = value
            if (!imagePaths.isNullOrEmpty()) currentImage = imagePaths.random()
        }
    val emptyPayload = EmptyPayload(ResourceLocation.fromNamespaceAndPath(TrollCommands.MOD_ID, "${command}_boolean"))
    lateinit var soundInstance: CustomSoundInstance
    private val asSubCommand
        get() = TrollCommand(command, condition, imagePaths, sound, true)
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(Commands.literal(command).executes(this).then(Commands.argument(ARGUMENT_KEY, EntityArgument.players()).requires { serverCommandSource -> serverCommandSource.hasPermission(2) }.executes(asSubCommand)))
    }
    override fun run(context: CommandContext<CommandSourceStack>): Int {
        val players = if (isSubCommand) EntityArgument.getPlayers(context, ARGUMENT_KEY).distinct() else listOf(context.source.player)
        players.forEach {
            ServerPlayNetworking.send(it, emptyPayload)
        }
        return 0
    }
    private var currentImage: String? = null
    open fun command(context: GuiGraphics) {
        if ((isBeingTrolled && !condition() || !isBeingTrolled) && Minecraft.getInstance().soundManager.isActive(soundInstance)) {
            Minecraft.getInstance().soundManager.stop(soundInstance)
        }
        if (!isBeingTrolled || !condition() || Minecraft.getInstance().player == null) return
        if (!imagePaths.isNullOrEmpty()) {
            currentImage?.let {
                val width = Minecraft.getInstance().window.guiScaledWidth
                val height = Minecraft.getInstance().window.guiScaledHeight
                context.blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TrollCommands.MOD_ID, it), 0, 0, 0F, 0F, width, height, width, height)
            }
        }
        if (sound != null && !Minecraft.getInstance().soundManager.isActive(soundInstance)) {
            Minecraft.getInstance().soundManager.play(soundInstance)
        }
    }
}