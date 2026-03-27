package com.smushytaco.troll_commands.commands.base
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import kotlin.time.Duration.Companion.milliseconds

class TrollKickCommand(command: String, condition: BooleanReturn, imagePaths: Array<String>?, sound: SoundEvent? = null): TrollCommand(command, condition, imagePaths, sound) {
    private val disconnectJob
        get() = CoroutineScope(Dispatchers.Default).launch {
            if (!isBeingTrolled || !condition() || Minecraft.getInstance().player == null) return@launch
            delay(1000L.milliseconds)
            if (!isBeingTrolled || !condition() || Minecraft.getInstance().player == null) return@launch
            isBeingTrolled = false
            Minecraft.getInstance().connection?.connection?.disconnect(Component.translatable("multiplayer.disconnect.flying"))
        }
    private lateinit var cachedDisconnectJob: Job
    override fun command(context: GuiGraphicsExtractor) {
        super.command(context)
        if ((!::cachedDisconnectJob.isInitialized || !cachedDisconnectJob.isActive) && Minecraft.getInstance().player != null && condition() && isBeingTrolled) {
            cachedDisconnectJob = disconnectJob
        }
    }
}