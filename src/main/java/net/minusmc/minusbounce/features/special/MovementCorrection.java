/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.features.special;

import net.minusmc.minusbounce.MinusBounce;
import net.minusmc.minusbounce.features.module.modules.world.Scaffold;
import net.minusmc.minusbounce.features.module.modules.combat.KillAura;
import net.minusmc.minusbounce.event.*;
import net.minusmc.minusbounce.utils.*;
import net.minecraft.util.MathHelper;

import static net.minusmc.minusbounce.utils.RotationUtils.targetRotation;

public final class MovementCorrection extends MinecraftInstance implements Listenable {

    @EventTarget
    public void onInput(final MoveInputEvent event) {
        if (targetRotation != null){
            final float forward = event.getForward();
            final float strafe = event.getStrafe();

            final float rotationOffset = (float) Math.toRadians(mc.thePlayer.rotationYaw - targetRotation.getYaw());
            final float cosValue = MathHelper.cos(rotationOffset);
            final float sinValue = MathHelper.sin(rotationOffset);

            event.setForward(Math.round(forward * cosValue + strafe * sinValue));
            event.setStrafe(Math.round(strafe * cosValue - forward * sinValue));
        }
    }

    @EventTarget 
    public void onJump(final JumpEvent event) {
        if(targetRotation != null) event.setYaw(targetRotation.getYaw());
    }

    @EventTarget 
    public void onStrafe(final StrafeEvent event) {
        if(targetRotation != null) event.setYaw(targetRotation.getYaw());
    }

    @Override 
    public boolean handleEvents() {
        final Scaffold scaffold = MinusBounce.moduleManager.getModule(Scaffold.class);
        final KillAura killAura = MinusBounce.moduleManager.getModule(KillAura.class);

        if (killAura.getState() && !killAura.getMovementCorrection().get()) return false;
        if (scaffold.getState() && !scaffold.getMovementCorrection().get()) return false;

        return true;
    }
}