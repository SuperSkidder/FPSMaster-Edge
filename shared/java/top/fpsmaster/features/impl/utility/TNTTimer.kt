package top.fpsmaster.features.impl.utility

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.item.EntityTNTPrimed
import org.lwjgl.opengl.GL11
import top.fpsmaster.features.manager.Category
import top.fpsmaster.features.manager.Module
import top.fpsmaster.features.settings.impl.BooleanSetting
import top.fpsmaster.features.settings.impl.NumberSetting
import top.fpsmaster.interfaces.ProviderManager
import top.fpsmaster.wrapper.entities.EntityTNTPrimedUtil
import java.awt.Color
import java.text.DecimalFormat

class TNTTimer : Module("TNTTimer", Category.Utility) {
    init {
        addSettings(duration)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    companion object {
        @JvmField
        var using = false
        var duration = NumberSetting("Duration", 4, 1, 10, 0.1)

        @JvmStatic
        fun doRender(entity: EntityTNTPrimed) {
            if (!using) return
            val mc = Minecraft.getMinecraft()
            GL11.glPushMatrix()
            GL11.glEnable(3042)
            GL11.glDisable(2929)
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GlStateManager.enableBlend()
            GL11.glBlendFunc(770, 771)
            GL11.glDisable(3553)
            val partialTicks = ProviderManager.timerProvider.getRenderPartialTicks()
            val x =
                entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks.toDouble() - ProviderManager.renderManagerProvider.renderPosX()
            val y =
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks.toDouble() - ProviderManager.renderManagerProvider.renderPosY()
            val z =
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks.toDouble() - ProviderManager.renderManagerProvider.renderPosZ()
            var scale = 0.065f
            GlStateManager.translate(
                x.toFloat(),
                y.toFloat() + entity.height + 0.5f - entity.height / 2.0f,
                z.toFloat()
            )
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            GL11.glScalef(-scale / 2, -scale, -scale)
            val xLeft = -10.0
            val xRight = 10.0
            val yUp = -20.0
            val yDown = -10.0
            drawRect(xLeft.toFloat(), yUp.toFloat(), xRight.toFloat(), yDown.toFloat(), Color(0, 0, 0, 100).rgb)
            drawTime(entity)
            GL11.glEnable(3553)
            GL11.glEnable(2929)
            GlStateManager.disableBlend()
            GL11.glDisable(3042)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            GL11.glNormal3f(1.0f, 1.0f, 1.0f)
            GL11.glPopMatrix()
        }

        private fun drawTime(entity: EntityTNTPrimed) {
            val width = ProviderManager.mcProvider.getFontRenderer().getStringWidth("0.00").toFloat() / 2.0f + 6.0f
            GlStateManager.disableDepth()
            GlStateManager.disableBlend()
            GlStateManager.disableLighting()
            val df = DecimalFormat("0.00")
            var color = Color(255, 255, 255)
            var time = EntityTNTPrimedUtil.getFuse(entity) / 20.0 + duration.value.toFloat() - 4
            if (time < 2.5) {
                color = Color(255, 255, 0)
            }
            if (time < 1.0) {
                color = Color(255, 0, 0)
            }
            ProviderManager.mcProvider.getFontRenderer().drawStringWithShadow(
                df.format(time),
                -width + 5,
                -20f,
                color.rgb
            )
            GlStateManager.enableLighting()
            GlStateManager.enableBlend()
            GlStateManager.enableDepth()
        }

        fun drawRect(g: Float, h: Float, i: Float, j: Float, col1: Int) {
            val f = (col1 shr 24 and 0xFF).toFloat() / 255.0f
            val f1 = (col1 shr 16 and 0xFF).toFloat() / 255.0f
            val f2 = (col1 shr 8 and 0xFF).toFloat() / 255.0f
            val f3 = (col1 and 0xFF).toFloat() / 255.0f
            GL11.glEnable(3042)
            GL11.glDisable(3553)
            GL11.glBlendFunc(770, 771)
            GL11.glEnable(2848)
            GL11.glPushMatrix()
            GL11.glColor4f(f1, f2, f3, f)
            GL11.glBegin(7)
            GL11.glVertex2d(i.toDouble(), h.toDouble())
            GL11.glVertex2d(g.toDouble(), h.toDouble())
            GL11.glVertex2d(g.toDouble(), j.toDouble())
            GL11.glVertex2d(i.toDouble(), j.toDouble())
            GL11.glEnd()
            GL11.glPopMatrix()
            GL11.glEnable(3553)
            GL11.glDisable(3042)
            GL11.glDisable(2848)
        }
    }
}
