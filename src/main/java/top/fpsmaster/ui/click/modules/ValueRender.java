package top.fpsmaster.ui.click.modules;

import top.fpsmaster.features.manager.Module;

public abstract class ValueRender {
    public Module mod;
    public float height = 0f;

    public abstract void render(float x, float y, float width, float height, float mouseX, float mouseY, boolean custom);

    public abstract void keyTyped(char typedChar, int keyCode);
}



