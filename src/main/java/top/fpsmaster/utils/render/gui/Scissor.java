package top.fpsmaster.utils.render.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import top.fpsmaster.utils.render.gui.UiScale;

public class Scissor {
    public static void apply(float x, float y, float width, float height) {
        float scale = UiScale.isActive() ? UiScale.getScale() : 1.0f;
        applyScaled(x, y, width, height, scale);
    }

    public static void apply(float x, float y, float width, float height, float scaleFactor) {
        applyScaled(x, y, width, height, scaleFactor);
    }

    public static void apply(float x, float y, float width, float height, int scaleFactor) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            return;
        }
        width *= 1f / scaleFactor * 2;
        height *= 1f / scaleFactor * 2;
        y *= 1f / scaleFactor * 2;
        x *= 1f / scaleFactor * 2;
        GL11.glScissor(
                (int) (x * Minecraft.getMinecraft().displayWidth / Minecraft.getMinecraft().currentScreen.width),
                (int) (Minecraft.getMinecraft().displayHeight - (y + height) * Minecraft.getMinecraft().displayHeight / Minecraft.getMinecraft().currentScreen.height),
                (int) (width * Minecraft.getMinecraft().displayWidth / Minecraft.getMinecraft().currentScreen.width),
                (int) (height * Minecraft.getMinecraft().displayHeight / Minecraft.getMinecraft().currentScreen.height)
        );
    }

    private static void applyScaled(float x, float y, float width, float height, float scaleFactor) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            return;
        }
        int displayHeight = Minecraft.getMinecraft().displayHeight;
        int sx = Math.round(x * scaleFactor);
        int sy = Math.round(y * scaleFactor);
        int sw = Math.round(width * scaleFactor);
        int sh = Math.round(height * scaleFactor);
        GL11.glScissor(
                sx,
                displayHeight - (sy + sh),
                sw,
                sh
        );
    }
}
