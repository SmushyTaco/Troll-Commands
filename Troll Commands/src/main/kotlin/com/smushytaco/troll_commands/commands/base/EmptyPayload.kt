package com.smushytaco.troll_commands.commands.base
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
class EmptyPayload(private val identifier: ResourceLocation) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<EmptyPayload> = CustomPacketPayload.Type(identifier)
    fun register() { PayloadTypeRegistry.playS2C().register(type(), StreamCodec.composite(ByteBufCodecs.STRING_UTF8, { it.identifier.toString() }) { EmptyPayload(
        ResourceLocation.parse(it)) }) }
}