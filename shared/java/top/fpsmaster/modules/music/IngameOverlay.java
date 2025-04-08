package top.fpsmaster.modules.music;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.impl.interfaces.MusicOverlay;
import top.fpsmaster.font.impl.UFontRenderer;
import top.fpsmaster.modules.music.MusicPlayer;
import top.fpsmaster.modules.music.netease.Music;
import top.fpsmaster.utils.Utility;
import top.fpsmaster.utils.math.animation.AnimationUtils;
import top.fpsmaster.utils.render.Render2DUtils;

import java.awt.Color;

public class IngameOverlay {
    private static float songProgress = 0f;
    private static Double[] smoothCurve = new Double[0];

    public static void onRender() {
        if (MusicPlayer.playList.getCurrent() != -1) {
            ScaledResolution sr = new ScaledResolution(Utility.mc);
            if (MusicPlayer.getCurve().length !=0) {
                float width = Math.max((sr.getScaledWidth() / 2f - 100) / MusicPlayer.getCurve().length, 1f);
                float x = 0f;

                if (smoothCurve.length != MusicPlayer.getCurve().length) {
                    smoothCurve = new Double[MusicPlayer.getCurve().length];
                }

                for (int i = 0; i < MusicPlayer.getCurve().length; i++) {
                    smoothCurve[i] = AnimationUtils.base(smoothCurve[i], MusicPlayer.getCurve()[i], 0.15);
                }

                double[] curve = MusicPlayer.getCurve();
                int fftSize = 1024;
                double sampleRate = 44100.0;
                double[] frequencies = new double[fftSize / 2];
                for (int i = 0; i < frequencies.length; i++) {
                    frequencies[i] = i * sampleRate / fftSize;
                }

                float screenWidth = sr.getScaledWidth();
                float screenHeight = sr.getScaledHeight();

                for (int i = 0; i < curve.length; i++) {
                    double freq = frequencies[i];
                    double magnitude = Math.min(Math.max(curve[i], 0.0), 1.0);

                    float xPos = (float) (freq / 22050.0 * screenWidth);
                    float height = (float) (magnitude * 100f * MusicOverlay.amplitude.value.floatValue());

                    Render2DUtils.drawRect(
                            xPos,
                            screenHeight - height,
                            2f,
                            height,
                            MusicOverlay.color.getRGB()
                    );
                }
            }
        }
    }

    public static void drawSong(float x, float y, float width, float height) {
        ScaledResolution sr = new ScaledResolution(Utility.mc);
        Music current = (Music) MusicPlayer.playList.musics.get(MusicPlayer.playList.getCurrent());
        UFontRenderer s18 = FPSMaster.fontManager.s18;

        Render2DUtils.drawOptimizedRoundedRect(x, y, width, height, new Color(0, 0, 0, 180));
        Render2DUtils.drawOptimizedRoundedRect(x, y, songProgress, height, MusicOverlay.progressColor.getColor());

        songProgress = (float) AnimationUtils.base((double) songProgress, 6 + (width - 6) * MusicPlayer.curPlayProgress, 0.1);

        Render2DUtils.drawImage(
                new ResourceLocation("music/netease/" + current.id),
                x + 5,
                y + 5,
                height - 10,
                height - 10,
                -1
        );

        FPSMaster.fontManager.s18.drawString(
                current.name,
                x + 40,
                y + 6,
                FPSMaster.theme.getTextColorTitle().getRGB()
        );

        FPSMaster.fontManager.s16.drawString(
                current.author,
                x + 40,
                y + 18,
                FPSMaster.theme.getTextColorDescription().getRGB()
        );
    }
}
