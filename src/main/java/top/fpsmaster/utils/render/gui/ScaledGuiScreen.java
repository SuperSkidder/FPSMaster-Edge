package top.fpsmaster.utils.render.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import top.fpsmaster.features.impl.interfaces.ClientSettings;

import java.io.IOException;

public class ScaledGuiScreen extends GuiScreen {
    public float scaleFactor = 1.0f;
    public float guiWidth;
    public float guiHeight;
    private int vanillaScaleFactor = 1;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateBaseMetrics();
        scaleFactor = (float) ClientSettings.getUiScale();
        if (scaleFactor <= 0) {
            scaleFactor = 1.0f;
        }
        float effectiveScale = scaleFactor;
        UiScale.begin(effectiveScale);
        GL11.glPushMatrix();
        GL11.glScalef(1f / vanillaScaleFactor, 1f / vanillaScaleFactor, 1f);
        int rawMouseX = (int) (Mouse.getX() / scaleFactor);
        int rawMouseY = (int) ((Minecraft.getMinecraft().displayHeight - Mouse.getY() - 1) / scaleFactor);
        super.drawScreen(rawMouseX, rawMouseY, partialTicks);
        render(rawMouseX, rawMouseY, partialTicks);
        GL11.glPopMatrix();
        UiScale.end();
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        updateBaseMetrics();
    }

    @Override
    public void initGui() {
        updateBaseMetrics();
        super.initGui();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        onClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        updateBaseMetrics();
        super.handleMouseInput();
    }

    private void updateBaseMetrics() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        vanillaScaleFactor = sr.getScaleFactor();
        guiWidth = mc.displayWidth / scaleFactor;
        guiHeight = mc.displayHeight / scaleFactor;
        width = (int) guiWidth;
        height = (int) guiHeight;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {

    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {

    }
}
