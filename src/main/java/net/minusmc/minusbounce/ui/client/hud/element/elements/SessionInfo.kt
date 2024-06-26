package net.minusmc.minusbounce.ui.client.hud.element.elements

import net.minusmc.minusbounce.MinusBounce
import net.minusmc.minusbounce.ui.client.hud.element.Border
import net.minusmc.minusbounce.ui.client.hud.element.Element
import net.minusmc.minusbounce.ui.client.hud.element.ElementInfo
import net.minusmc.minusbounce.ui.client.hud.element.Side
import net.minusmc.minusbounce.ui.font.Fonts
import net.minusmc.minusbounce.utils.render.RenderUtils
import net.minusmc.minusbounce.utils.*
import net.minusmc.minusbounce.value.BoolValue
import net.minusmc.minusbounce.value.FloatValue
import net.minusmc.minusbounce.value.FontValue
import net.minusmc.minusbounce.value.IntegerValue
import java.awt.Color

@ElementInfo(name = "SessionInfo") 
class SessionInfo(x: Double = 15.0, y: Double = 10.0, scale: Float = 1F, side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)) : Element(x, y, scale, side) {
    override fun drawElement(): Border {
        RenderUtils.drawRoundedRect(0f, 0f, 150f, 60f, 4f, Color(250, 249, 246, 60).rgb)
        Fonts.fontLexend50.drawCenteredString("Session info", 75f, 6f, Color.WHITE.rgb)

        Fonts.fontLexend35.drawStringWithShadow("Session time", 6f, 25f, Color.WHITE.rgb)
        val time = StringUtils.getFormatTime(MinusBounce.sessionManager.timePlayed)
        Fonts.fontLexend35.drawStringWithShadow(time, 144f - Fonts.fontLexend35.getStringWidth(time), 25f, Color.WHITE.rgb)

        Fonts.fontLexend35.drawStringWithShadow("Server", 6f, 35f, Color.WHITE.rgb)
        Fonts.fontLexend35.drawStringWithShadow(ServerUtils.remoteIp, 144f - Fonts.fontLexend35.getStringWidth(ServerUtils.remoteIp), 35f, Color.WHITE.rgb)

        Fonts.fontLexend35.drawStringWithShadow("Kills", 6f, 45f, Color.WHITE.rgb)
        val kills = MinusBounce.sessionManager.kills.toString()
        Fonts.fontLexend35.drawStringWithShadow(kills, 144f - Fonts.fontLexend35.getStringWidth(kills), 45f, Color.WHITE.rgb)


        return Border(0f, 0f, 150f, 60f)
    }
}