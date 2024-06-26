package net.minusmc.minusbounce.features.module.modules.combat.killaura.blocking

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.util.BlockPos
import net.minusmc.minusbounce.features.module.modules.combat.killaura.KillAuraBlocking

class NewNCPBlocking: KillAuraBlocking("NewNCP") {
    override fun onPreAttack(){
        val slot = mc.thePlayer.inventory.currentItem
        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(slot % 8 + 1))
        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(slot))
        blockingStatus = false
    }

    override fun onPostAttack(){
        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f))
        blockingStatus = true
    }
}