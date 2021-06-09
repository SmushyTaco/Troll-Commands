package com.smushytaco.troll_commands.commands.base
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.smushytaco.troll_commands.TrollCommands
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
abstract class AbstractTrollCommand(val command: String, private val imagePath: String?, val sound: SoundEvent? = null) : Command<ServerCommandSource> {
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
    private val image = Identifier(TrollCommands.MOD_ID, imagePath ?: "")
    val packetIdentifier = Identifier(TrollCommands.MOD_ID, "${command}_boolean")
    lateinit var soundInstance: CustomSoundInstance
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        ServerPlayNetworking.send(context.source.player, packetIdentifier, PacketByteBufs.empty())
        return 0
    }
    fun command(matrixStack: MatrixStack) {
        if ((isBeingTrolled && !condition() || !isBeingTrolled) && MinecraftClient.getInstance().soundManager.isPlaying(soundInstance)) {
            MinecraftClient.getInstance().soundManager.stop(soundInstance)
        }
        if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return
        if (imagePath != null) {
            val width = MinecraftClient.getInstance().window.scaledWidth
            val height = MinecraftClient.getInstance().window.scaledHeight
            val textureSize = if (width > height) width else height
            RenderSystem.setShaderTexture(0, image)
            DrawableHelper.drawTexture(matrixStack, 0, 0, 0.0F, 0.0F, width, height, textureSize, textureSize)
        }
        if (sound != null && !MinecraftClient.getInstance().soundManager.isPlaying(soundInstance)) {
            MinecraftClient.getInstance().soundManager.play(soundInstance)
        }
    }
}