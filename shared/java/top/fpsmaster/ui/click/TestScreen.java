package top.fpsmaster.ui.click;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.utils.render.Render2DUtils;

import java.awt.*;

public class TestScreen extends GuiScreen {
    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        int factor = sr.getScaleFactor();
        int realWidth = sr.getScaledWidth() * sr.getScaleFactor() / 2;
        int realHeight = sr.getScaledHeight() * sr.getScaleFactor() / 2;
        int realMouseX = mouseX * factor / 2;
        int realMouseY = mouseY * factor / 2;

        GL11.glScaled((double) 1 / sr.getScaleFactor() * 2.0, (double) 1 / sr.getScaleFactor() * 2.0, 1.0);

        if (Render2DUtils.isHovered(10, 10, realWidth - 20, 100, mouseX * factor / 2, mouseY * factor / 2)) {
            Render2DUtils.drawRect(10, 10, realWidth - 20, 100, Color.RED);
        } else {
            Render2DUtils.drawRect(10, 10, realWidth - 20, 100, -1);
        }
    }
}
