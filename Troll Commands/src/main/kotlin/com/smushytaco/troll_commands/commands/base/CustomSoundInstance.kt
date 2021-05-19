package com.smushytaco.troll_commands.commands.base
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.MovingSoundInstance
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
@Environment(EnvType.CLIENT)
class CustomSoundInstance(sound: SoundEvent?): MovingSoundInstance(sound ?: SoundEvents.BLOCK_SAND_BREAK, SoundCategory.MASTER) {
    override fun tick() {
        val player = MinecraftClient.getInstance().player ?: return
        x = player.x
        y = player.y
        z = player.z
    }
}