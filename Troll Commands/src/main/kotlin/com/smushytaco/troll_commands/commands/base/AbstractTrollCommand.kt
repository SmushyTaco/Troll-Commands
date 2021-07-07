package com.smushytaco.troll_commands.commands.base
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.brigadier.Command
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
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
abstract class AbstractTrollCommand(val command: String, private val imagePaths: Array<String>?, val sound: SoundEvent? = null) : Command<ServerCommandSource> {
    companion object {
        val trollCommands = arrayListOf<AbstractTrollCommand>()
        fun AbstractTrollCommand.resetAllExcept() {
            trollCommands.forEach {
                if (it != this) it.isBeingTrolled = false
            }
        }
    }
    abstract fun condition(): Boolean
    var isBeingTrolled = false
    val packetIdentifier = Identifier(TrollCommands.MOD_ID, "${command}_boolean")
    lateinit var soundInstance: CustomSoundInstance
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        ServerPlayNetworking.send(context.source.player, packetIdentifier, PacketByteBufs.empty())
        return 0
    }
    fun command(matrixStack: MatrixStack) {
//        MinecraftClient.getInstance().networkHandler?.connection?.disconnect(TranslatableText("multiplayer.disconnect.flying"))
        if ((isBeingTrolled && !condition() || !isBeingTrolled) && MinecraftClient.getInstance().soundManager.isPlaying(soundInstance)) {
            MinecraftClient.getInstance().soundManager.stop(soundInstance)
        }
        if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return
        if (!imagePaths.isNullOrEmpty()) {
            val image = Identifier(TrollCommands.MOD_ID, imagePaths.random())
            val width = MinecraftClient.getInstance().window.scaledWidth.toFloat()
            val height = MinecraftClient.getInstance().window.scaledHeight.toFloat()
            RenderSystem.setShaderTexture(0, image)
            RenderSystem.setShader { GameRenderer.getPositionTexShader() }
            val bufferBuilder = Tessellator.getInstance().buffer
            bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
            bufferBuilder.vertex(matrixStack.peek().model, 0.0F, height, -0.90F).texture(0.0F, 1.0F).next()
            bufferBuilder.vertex(matrixStack.peek().model, width, height, -0.90F).texture(1.0F, 1.0F).next()
            bufferBuilder.vertex(matrixStack.peek().model, width, 0.0F, -0.90F).texture(1.0F, 0.0F).next()
            bufferBuilder.vertex(matrixStack.peek().model, 0.0F, 0.0F, -0.90F).texture(0.0F, 0.0F).next()
            bufferBuilder.end()
            BufferRenderer.draw(bufferBuilder)
        }
        if (sound != null && !MinecraftClient.getInstance().soundManager.isPlaying(soundInstance)) {
            MinecraftClient.getInstance().soundManager.play(soundInstance)
        }
    }
}