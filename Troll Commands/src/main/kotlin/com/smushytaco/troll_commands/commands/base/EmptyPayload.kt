package com.smushytaco.troll_commands.commands.base
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
class EmptyPayload(private val identifier: Identifier) : CustomPayload {
    override fun getId(): CustomPayload.Id<EmptyPayload> = CustomPayload.Id(identifier)
    fun register() { PayloadTypeRegistry.playS2C().register(id, PacketCodec.unit(this)) }
}