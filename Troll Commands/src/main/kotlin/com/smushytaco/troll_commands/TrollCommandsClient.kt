package com.smushytaco.troll_commands
import com.smushytaco.troll_commands.commands.base.CustomSoundInstance
import com.smushytaco.troll_commands.commands.base.TrollCommand.Companion.resetAllExcept
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
object TrollCommandsClient: ClientModInitializer {
    override fun onInitializeClient() {
        TrollCommands.trollCommands.forEach {
            it.soundInstance = CustomSoundInstance(it.sound)
            ClientPlayNetworking.registerGlobalReceiver(it.emptyPayload.id) { _, _ ->
                it.isBeingTrolled = !it.isBeingTrolled
                if (it.isBeingTrolled) {
                    it.resetAllExcept()
                } else if (!it.isBeingTrolled && MinecraftClient.getInstance().soundManager.isPlaying(it.soundInstance)) {
                    MinecraftClient.getInstance().soundManager.stop(it.soundInstance)
                }
            }
        }
        HudElementRegistry.addLast(Identifier.of(TrollCommands.MOD_ID, "image")) { context, _ ->
            TrollCommands.trollCommands.forEach {
                it.command(context)
            }
        }
    }
}