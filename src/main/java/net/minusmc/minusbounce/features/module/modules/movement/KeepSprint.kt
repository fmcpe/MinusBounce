/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.module.modules.movement

import net.minusmc.minusbounce.event.EventTarget
import net.minusmc.minusbounce.event.KnockBackEvent
import net.minusmc.minusbounce.features.module.Module
import net.minusmc.minusbounce.features.module.ModuleCategory
import net.minusmc.minusbounce.features.module.ModuleInfo

@ModuleInfo(name = "KeepSprint", spacedName = "Keep Sprint", description = "Keep you sprint", category = ModuleCategory.MOVEMENT)
class KeepSprint: Module(){
    @EventTarget
    fun onKB(event: KnockBackEvent){
        event.cancelEvent()
    }
}
