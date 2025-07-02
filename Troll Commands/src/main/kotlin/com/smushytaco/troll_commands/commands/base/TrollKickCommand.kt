package com.smushytaco.troll_commands.commands.base
import kotlinx.coroutines.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.sound.SoundEvent
import net.minecraft.text.Text
class TrollKickCommand(command: String, condition: BooleanReturn, imagePaths: Array<String>?, sound: SoundEvent? = null): TrollCommand(command, condition, imagePaths, sound) {
    private val disconnectJob
        get() = CoroutineScope(Dispatchers.Default).launch {
            if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return@launch
            delay(1000)
            if (!isBeingTrolled || !condition() || MinecraftClient.getInstance().player == null) return@launch
            isBeingTrolled = false
            MinecraftClient.getInstance().networkHandler?.connection?.disconnect(Text.translatable("multiplayer.disconnect.flying"))
        }
    private lateinit var cachedDisconnectJob: Job
    override fun command(context: DrawContext) {
        super.command(context)
        if ((!::cachedDisconnectJob.isInitialized || !cachedDisconnectJob.isActive) && MinecraftClient.getInstance().player != null && condition() && isBeingTrolled) {
            cachedDisconnectJob = disconnectJob
        }
    }
}