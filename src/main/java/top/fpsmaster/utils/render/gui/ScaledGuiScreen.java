package top.fpsmaster.utils.render.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import top.fpsmaster.features.impl.interfaces.ClientSettings;

import java.io.IOException;

public class ScaledGuiScreen extends GuiScreen {
    private static final class ClickEvent {
        private final int x;
        private final int y;
        private final int button;
        private boolean consumed;

        private ClickEvent(int x, int y, int button) {
            this.x = x;
            this.y = y;
            this.button = button;
        }
    }

    public float scaleFactor = 1.0f;
    public float guiWidth;
    public float guiHeight;
    private int vanillaScaleFactor = 1;
    private ClickEvent pendingClick;
    private static ScaledGuiScreen activeScreen;

    public static ScaledGuiScreen getActiveScreen() {
        return activeScreen;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        refreshScaleAndMetrics();
        float effectiveScale = scaleFactor;
        UiScale.begin(effectiveScale);
        GL11.glPushMatrix();
        try {
            activeScreen = this;
            GL11.glScalef(1f / vanillaScaleFactor, 1f / vanillaScaleFactor, 1f);
            int rawMouseX = (int) (Mouse.getX() / scaleFactor);
            int rawMouseY = (int) ((Minecraft.getMinecraft().displayHeight - Mouse.getY() - 1) / scaleFactor);
            super.drawScreen(rawMouseX, rawMouseY, partialTicks);
            render(rawMouseX, rawMouseY, partialTicks);
            pendingClick = null;
        } finally {
            activeScreen = null;
            GL11.glPopMatrix();
            UiScale.end();
        }
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        refreshScaleAndMetrics();
    }

    @Override
    public void initGui() {
        refreshScaleAndMetrics();
        super.initGui();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        pendingClick = new ClickEvent(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        refreshScaleAndMetrics();
        super.handleMouseInput();
    }

    private void refreshScaleAndMetrics() {
        scaleFactor = (float) ClientSettings.getUiScale();
        if (scaleFactor <= 0) {
            scaleFactor = 1.0f;
        }
        updateBaseMetrics();
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

    protected boolean consumeClick(float x, float y, float width, float height, int button) {
        if (pendingClick == null || pendingClick.consumed || pendingClick.button != button) {
            return false;
        }
        if (pendingClick.x < x || pendingClick.x > x + width || pendingClick.y < y || pendingClick.y > y + height) {
            return false;
        }
        pendingClick.consumed = true;
        return true;
    }

    protected boolean hasPendingClick(int button) {
        return pendingClick != null && !pendingClick.consumed && pendingClick.button == button;
    }

    protected int getPendingClickX() {
        return pendingClick == null ? 0 : pendingClick.x;
    }

    protected int getPendingClickY() {
        return pendingClick == null ? 0 : pendingClick.y;
    }

    protected int getPendingClickButton() {
        return pendingClick == null ? -1 : pendingClick.button;
    }

    protected void consumePendingClick() {
        if (pendingClick != null) {
            pendingClick.consumed = true;
        }
    }

    public boolean hasClickEventThisFrame() {
        return pendingClick != null;
    }

    public int peekPendingClickX() {
        return pendingClick == null ? 0 : pendingClick.x;
    }

    public int peekPendingClickY() {
        return pendingClick == null ? 0 : pendingClick.y;
    }

    public static final class ConsumedClick {
        public final int x;
        public final int y;
        public final int button;

        public ConsumedClick(int x, int y, int button) {
            this.x = x;
            this.y = y;
            this.button = button;
        }
    }

    public ConsumedClick consumeClickInBounds(float x, float y, float width, float height) {
        if (pendingClick == null || pendingClick.consumed) {
            return null;
        }
        if (pendingClick.x < x || pendingClick.x > x + width || pendingClick.y < y || pendingClick.y > y + height) {
            return null;
        }
        pendingClick.consumed = true;
        return new ConsumedClick(pendingClick.x, pendingClick.y, pendingClick.button);
    }
}
