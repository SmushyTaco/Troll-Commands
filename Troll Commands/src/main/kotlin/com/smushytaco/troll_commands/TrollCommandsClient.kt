package com.smushytaco.troll_commands
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexFormat
import com.smushytaco.troll_commands.commands.base.CustomSoundInstance
import com.smushytaco.troll_commands.commands.base.TrollCommand.Companion.resetAllExcept
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
object TrollCommandsClient: ClientModInitializer {
    val TROLL_RENDER_PIPELINE: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.MATRICES_COLOR_SNIPPET)
        .withLocation(Identifier.of(TrollCommands.MOD_ID, "pipeline/troll"))
        .withVertexShader("core/position_tex")
        .withFragmentShader("core/position_tex")
        .withSampler("Sampler0")
        .withDepthWrite(false)
        .withColorWrite(true, false)
        .withBlend(BlendFunction.TRANSLUCENT)
        .withVertexFormat(VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.TRIANGLES)
        .build())
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
        HudLayerRegistrationCallback.EVENT.register(HudLayerRegistrationCallback { layeredDrawer ->
            layeredDrawer.attachLayerAfter(IdentifiedLayer.DEBUG, Identifier.of(TrollCommands.MOD_ID, "image")) { context, _ ->
                TrollCommands.trollCommands.forEach {
                    it.command(context.matrices)
                }
            }
        })
    }
}