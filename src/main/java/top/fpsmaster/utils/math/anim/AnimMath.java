package top.fpsmaster.utils.math.anim;

import net.minecraft.client.Minecraft;

public final class AnimMath {
    private AnimMath() {
    }

    private static float debugFPS() {
        int fps = Minecraft.getDebugFPS();
        return fps <= 5 ? 60f : (float) fps;
    }

    public static double base(double current, double target, double speed) {
        double next = current + (target - current) * speed / (debugFPS() / 60f);
        return Double.isNaN(next) ? 0.0 : next;
    }
}
