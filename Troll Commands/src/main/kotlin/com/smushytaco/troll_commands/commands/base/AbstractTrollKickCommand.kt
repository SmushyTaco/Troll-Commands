package com.smushytaco.troll_commands.commands.base
import kotlinx.coroutines.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvent
import net.minecraft.text.TranslatableText
abstract class AbstractTrollKickCommand(command: String, imagePaths: Array<String>?, sound: SoundEvent? = null): AbstractTrollCommand(command, imagePaths, sound) {
    private val disconnectJob
        get() = CoroutineScope(Dispatchers.Default).launch {
            if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return@launch
            delay(1000)
            if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return@launch
            isBeingTrolled = false
            MinecraftClient.getInstance().networkHandler?.connection?.disconnect(TranslatableText("multiplayer.disconnect.flying"))
        }
    private lateinit var cachedDisconnectJob: Job
    override fun command(matrixStack: MatrixStack) {
        super.command(matrixStack)
        if ((!::cachedDisconnectJob.isInitialized || !cachedDisconnectJob.isActive) && MinecraftClient.getInstance().player != null && condition() && isBeingTrolled) {
            cachedDisconnectJob = disconnectJob
        }
    }
}