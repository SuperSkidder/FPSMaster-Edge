package top.fpsmaster.utils.render.draw;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import top.fpsmaster.utils.render.gui.UiScale;
import top.fpsmaster.utils.render.state.Alpha;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Circles {
    private Circles() {
    }

    public static void fill(float cx, float cy, float radius, Color color) {
        fill(cx, cy, radius, color.getRGB());
    }

    public static void fill(float cx, float cy, float radius, int color) {
        cx = UiScale.scale(cx);
        cy = UiScale.scale(cy);
        radius = UiScale.scale(radius);
        int segments = segments(radius);
        boolean alphaTest = glIsEnabled(GL_ALPHA_TEST);
        boolean depthTest = glIsEnabled(GL_DEPTH_TEST);
        boolean cullFace = glIsEnabled(GL_CULL_FACE);
        if (alphaTest) {
            glDisable(GL_ALPHA_TEST);
        }
        if (depthTest) {
            glDisable(GL_DEPTH_TEST);
        }
        if (cullFace) {
            glDisable(GL_CULL_FACE);
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glColor(color);
        glBegin(GL11.GL_TRIANGLE_FAN);
        glVertex2d(cx, cy);
        for (int i = 0; i <= segments; i++) {
            double angle = Math.PI * 2.0 * i / segments;
            glVertex2d(cx + Math.cos(angle) * radius, cy + Math.sin(angle) * radius);
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        if (cullFace) {
            glEnable(GL_CULL_FACE);
        }
        if (depthTest) {
            glEnable(GL_DEPTH_TEST);
        }
        if (alphaTest) {
            glEnable(GL_ALPHA_TEST);
        }
    }

    public static void outline(float cx, float cy, float radius, float lineWidth, Color color) {
        outline(cx, cy, radius, lineWidth, color.getRGB());
    }

    public static void outline(float cx, float cy, float radius, float lineWidth, int color) {
        cx = UiScale.scale(cx);
        cy = UiScale.scale(cy);
        radius = UiScale.scale(radius);
        lineWidth = UiScale.scale(lineWidth);
        int segments = segments(radius);
        boolean alphaTest = glIsEnabled(GL_ALPHA_TEST);
        boolean depthTest = glIsEnabled(GL_DEPTH_TEST);
        boolean cullFace = glIsEnabled(GL_CULL_FACE);
        if (alphaTest) {
            glDisable(GL_ALPHA_TEST);
        }
        if (depthTest) {
            glDisable(GL_DEPTH_TEST);
        }
        if (cullFace) {
            glDisable(GL_CULL_FACE);
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(Math.max(1f, lineWidth));
        glColor(color);
        glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < segments; i++) {
            double angle = Math.PI * 2.0 * i / segments;
            glVertex2d(cx + Math.cos(angle) * radius, cy + Math.sin(angle) * radius);
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        if (cullFace) {
            glEnable(GL_CULL_FACE);
        }
        if (depthTest) {
            glEnable(GL_DEPTH_TEST);
        }
        if (alphaTest) {
            glEnable(GL_ALPHA_TEST);
        }
    }

    private static int segments(float radius) {
        return Math.max(24, Math.min(360, (int) (radius * 4f)));
    }

    private static void glColor(int color) {
        Color c = Colors.toColor(Alpha.apply(color));
        GL11.glColor4f(
                c.getRed() / 255.0F,
                c.getGreen() / 255.0F,
                c.getBlue() / 255.0F,
                c.getAlpha() / 255.0F
        );
    }
}
