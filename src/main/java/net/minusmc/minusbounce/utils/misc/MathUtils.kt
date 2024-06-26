/*
 * MinusBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/MinusMC/MinusBounce
 */
package net.minusmc.minusbounce.utils.misc

import java.math.BigDecimal
import kotlin.math.PI

/**
 * Utils for math
 */

object MathUtils {

    /**
     * Round number
     */

    fun round(number: Float, scale: Int): BigDecimal {
        val bd = BigDecimal(number.toString())
        return bd.setScale(scale, 4)
    }
    fun round(number: Float): BigDecimal = round(number, 2)

    fun round(number: Double, scale: Int) = round(number.toFloat(), 2)
    fun round(number: Double): BigDecimal = round(number.toFloat(), 2)

    /**
     * Wrapper to convert degrees to radians
     */
    fun toRadians(deg: Double): Double = deg / 180.0 * PI
    fun toRadians(deg: Float): Float = deg / 180f * PI.toFloat()


    /**
     * Wrapper to convert radians to degrees
     */
    fun toDegrees(rad: Double): Double = rad * 180.0 / PI
    fun toDegrees(rad: Float): Float = rad * 180f / PI.toFloat()

    /**
     * Wrapper to check mouseX and mouseY in rect
     */

    fun isHovering(mouseX: Number, mouseY: Number, x: Number, x2: Number, y: Number, y2: Number): Boolean {
        return isHovering(mouseX.toInt(), mouseY.toInt(), x.toInt(), x2.toInt(), y.toInt(), y2.toInt())
    }

    fun isHovering(mouseX: Int, mouseY: Int, x: Int, x2: Int, y: Int, y2: Int): Boolean {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2
    }

}