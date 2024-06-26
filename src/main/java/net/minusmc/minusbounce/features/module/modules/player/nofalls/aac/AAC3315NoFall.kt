package net.minusmc.minusbounce.features.module.modules.player.nofalls.aac

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minusmc.minusbounce.features.module.modules.player.nofalls.NoFallMode

class AAC3315NoFall: NoFallMode("AAC3.3.15") {
	override fun onUpdate() {
		if (mc.thePlayer.fallDistance > 2) {
            if (!mc.isIntegratedServerRunning) {
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, false))
            }
            mc.thePlayer.fallDistance = -9999f
        }
	}
}