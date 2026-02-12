package top.fpsmaster.ui.common.control;

import top.fpsmaster.utils.render.gui.ScaledGuiScreen;

public interface UiControl {
    void render(float x, float y, float width, float height, float mouseX, float mouseY);

    default void renderInScreen(ScaledGuiScreen screen, float x, float y, float width, float height, float mouseX, float mouseY) {
        render(x, y, width, height, mouseX, mouseY);
        ScaledGuiScreen.ConsumedClick click = screen.consumeClickInBounds(x, y, width, height);
        if (click != null) {
            mouseClicked(click.x, click.y, click.button);
        }
    }

    default void renderInActiveScreen(float x, float y, float width, float height, float mouseX, float mouseY) {
        ScaledGuiScreen screen = ScaledGuiScreen.getActiveScreen();
        if (screen == null) {
            render(x, y, width, height, mouseX, mouseY);
            return;
        }
        renderInScreen(screen, x, y, width, height, mouseX, mouseY);
    }

    default void mouseClicked(float mouseX, float mouseY, int button) {
    }

    default void keyTyped(char typedChar, int keyCode) {
    }
}
