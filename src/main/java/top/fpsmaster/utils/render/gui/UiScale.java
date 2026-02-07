package top.fpsmaster.utils.render.gui;

public final class UiScale {
    private static boolean active;
    private static float scale = 1.0f;

    private UiScale() {
    }

    public static void begin(float scaleFactor) {
        scale = scaleFactor <= 0 ? 1.0f : scaleFactor;
        active = true;
    }

    public static void end() {
        active = false;
        scale = 1.0f;
    }

    public static boolean isActive() {
        return active;
    }

    public static float getScale() {
        return scale;
    }

    public static float scale(float value) {
        return active ? value * scale : value;
    }

    public static int scale(int value) {
        return active ? Math.round(value * scale) : value;
    }
}
