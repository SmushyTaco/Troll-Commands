package com.smushytaco.troll_commands.commands.base
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
class CustomSoundInstance(sound: SoundEvent?): AbstractTickableSoundInstance(sound ?: SoundEvents.SAND_BREAK, SoundSource.MASTER, RandomSource.create()) {
    override fun tick() {
        val player = Minecraft.getInstance().player ?: return
        x = player.x
        y = player.y
        z = player.z
    }
}