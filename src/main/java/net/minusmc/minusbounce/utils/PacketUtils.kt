/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils

import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minusmc.minusbounce.event.*
import net.minusmc.minusbounce.utils.timer.MSTimer

object PacketUtils : MinecraftInstance(), Listenable {
    val packetList = arrayListOf<Packet<*>>()
    var inBound = 0
    var outBound = 0
    var avgInBound = 0
    var avgOutBound = 0

    private val packetTimer = MSTimer()
    private val wdTimer = MSTimer()
    private var transCount = 0
    private var wdVL = 0

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet.javaClass.getSimpleName().startsWith("C"))
            outBound++ 
        else if (packet.javaClass.getSimpleName().startsWith("S"))
            inBound++

        if (packet is S32PacketConfirmTransaction && !isInventoryAction(packet.actionNumber)) {
            transCount++
        }
    }

    fun sendPacketNoEvent(packet: Packet<*>){
        packetList.add(packet)
        mc.netHandler.addToSendQueue(packet)
    }

    fun sendPacket(packet: Packet<*>) {
        mc.netHandler.addToSendQueue(packet)
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        packetList.clear()
    }

    @EventTarget
    fun onTick(event: TickEvent?) {
        if (packetTimer.hasTimePassed(1000L)) {
            avgInBound = inBound
            avgOutBound = outBound
            outBound = 0
            inBound = outBound
            packetTimer.reset()
        }
        if (mc.thePlayer == null || mc.theWorld == null) {
            wdVL = 0
            transCount = 0
            wdTimer.reset()
        } else if (wdTimer.hasTimePassed(100L)) {
            wdVL += if (transCount > 0) 1 else -1
            transCount = 0
            if (wdVL > 10) wdVL = 10
            if (wdVL < 0) wdVL = 0
            wdTimer.reset()
        }
    }

    private fun isInventoryAction(action: Short): Boolean = action > 0 && action < 100

    val isWatchdogActive: Boolean
        get() = wdVL >= 8

    override fun handleEvents() = true
}