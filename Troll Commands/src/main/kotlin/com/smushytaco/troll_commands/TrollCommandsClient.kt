package com.smushytaco.troll_commands
import com.smushytaco.troll_commands.commands.base.CustomSoundInstance
import com.smushytaco.troll_commands.commands.base.TrollCommand.Companion.resetAllExcept
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
object TrollCommandsClient: ClientModInitializer {
    override fun onInitializeClient() {
        TrollCommands.trollCommands.forEach {
            it.soundInstance = CustomSoundInstance(it.sound)
            ClientPlayNetworking.registerGlobalReceiver(it.emptyPayload.type()) { _, _ ->
                it.isBeingTrolled = !it.isBeingTrolled
                if (it.isBeingTrolled) {
                    it.resetAllExcept()
                } else if (!it.isBeingTrolled && Minecraft.getInstance().soundManager.isActive(it.soundInstance)) {
                    Minecraft.getInstance().soundManager.stop(it.soundInstance)
                }
            }
        }
        HudElementRegistry.addLast(ResourceLocation.fromNamespaceAndPath(TrollCommands.MOD_ID, "image")) { context, _ ->
            TrollCommands.trollCommands.forEach {
                it.command(context)
            }
        }
    }
}