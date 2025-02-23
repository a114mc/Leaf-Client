package net.nonemc.leaf.features.module.modules.player.nofalls.normal

import net.nonemc.leaf.event.PacketEvent
import net.nonemc.leaf.features.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer

class VanillaNofall : NoFallMode("Vanilla") {
    override fun onPacket(event: PacketEvent) {
        if(event.packet is C03PacketPlayer && mc.thePlayer.fallDistance > 2.5) event.packet.onGround = true
    }
}