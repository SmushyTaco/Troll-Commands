package com.smushytaco.troll_commands.commands.base
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.smushytaco.troll_commands.TrollCommands
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
open class TrollCommand private constructor(private val command: String, val condition: BooleanReturn, private val imagePaths: Array<String>?, val sound: SoundEvent? = null, private val isSubCommand: Boolean) : Command<ServerCommandSource> {
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
    val packetIdentifier = Identifier(TrollCommands.MOD_ID, "${command}_boolean")
    lateinit var soundInstance: CustomSoundInstance
    private val asSubCommand
        get() = TrollCommand(command, condition, imagePaths, sound, true)
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal(command).executes(this).then(CommandManager.argument(ARGUMENT_KEY, EntityArgumentType.players()).requires { serverCommandSource -> serverCommandSource.hasPermissionLevel(2) }.executes(asSubCommand)))
    }
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        val players = if (isSubCommand) EntityArgumentType.getPlayers(context, ARGUMENT_KEY).distinct() else listOf(context.source.player)
        players.forEach {
            ServerPlayNetworking.send(it, packetIdentifier, PacketByteBufs.empty())
        }
        return 0
    }
    private var currentImage: String? = null
    open fun command(matrixStack: MatrixStack) {
        if ((isBeingTrolled && !condition() || !isBeingTrolled) && MinecraftClient.getInstance().soundManager.isPlaying(soundInstance)) {
            MinecraftClient.getInstance().soundManager.stop(soundInstance)
        }
        if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return
        if (!imagePaths.isNullOrEmpty() && currentImage != null) {
            val image = Identifier(TrollCommands.MOD_ID, currentImage)
            val width = MinecraftClient.getInstance().window.scaledWidth.toFloat()
            val height = MinecraftClient.getInstance().window.scaledHeight.toFloat()
            RenderSystem.setShaderTexture(0, image)
            RenderSystem.setShader { GameRenderer.getPositionTexShader() }
            val bufferBuilder = Tessellator.getInstance().buffer
            bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
            bufferBuilder.vertex(matrixStack.peek().positionMatrix, 0.0F, height, -0.90F).texture(0.0F, 1.0F).next()
            bufferBuilder.vertex(matrixStack.peek().positionMatrix, width, height, -0.90F).texture(1.0F, 1.0F).next()
            bufferBuilder.vertex(matrixStack.peek().positionMatrix, width, 0.0F, -0.90F).texture(1.0F, 0.0F).next()
            bufferBuilder.vertex(matrixStack.peek().positionMatrix, 0.0F, 0.0F, -0.90F).texture(0.0F, 0.0F).next()
            bufferBuilder.end()
            BufferRenderer.draw(bufferBuilder)
        }
        if (sound != null && !MinecraftClient.getInstance().soundManager.isPlaying(soundInstance)) {
            MinecraftClient.getInstance().soundManager.play(soundInstance)
        }
    }
}