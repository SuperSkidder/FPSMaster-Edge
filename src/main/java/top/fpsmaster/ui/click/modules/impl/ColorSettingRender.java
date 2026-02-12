package top.fpsmaster.ui.click.modules.impl;

import top.fpsmaster.utils.render.draw.Images;
import top.fpsmaster.utils.render.draw.Gradients;
import top.fpsmaster.utils.render.draw.Hover;
import top.fpsmaster.utils.render.draw.Rects;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.features.settings.impl.ColorSetting;
import top.fpsmaster.features.settings.impl.utils.CustomColor;
import top.fpsmaster.ui.click.MainPanel;
import top.fpsmaster.ui.click.modules.SettingRender;
import top.fpsmaster.ui.common.binding.ColorSettingBinding;
import top.fpsmaster.utils.math.anim.AnimMath;
import top.fpsmaster.utils.render.gui.UiScale;
import top.fpsmaster.utils.system.OSUtil;
import top.fpsmaster.utils.render.gui.GuiScale;
import top.fpsmaster.utils.render.shader.GradientUtils;
import top.fpsmaster.utils.render.gui.ScaledGuiScreen;

import java.awt.*;
import java.util.Locale;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ColorSettingRender extends SettingRender<ColorSetting> {

    private float aHeight = 0f;
    private boolean expand = false;
    private final ColorSettingBinding binding;

    public ColorSettingRender(Module mod, ColorSetting setting) {
        super(setting);
        this.mod = mod;
        this.binding = new ColorSettingBinding(setting);
    }

    @Override
    public void render(
            float x, float y, float width, float height, float mouseX, float mouseY, boolean custom
    ) {
        float tW = FPSMaster.fontManager.s16.drawString(
                FPSMaster.i18n.get((mod.name + "." + setting.name).toLowerCase(Locale.getDefault())),
                x + 10, y + 3, new Color(162, 162, 162).getRGB()
        );
        Rects.rounded(Math.round(x + tW + 26), Math.round(y + 1), 80, 14, new Color(39, 39, 39));

        CustomColor customColor = binding.get();
        Rects.rounded(Math.round(x + tW + 27), Math.round(y + 2), 12, 12, customColor.getRGB());

        FPSMaster.fontManager.s16.drawString(
                "#" + Integer.toHexString(setting.getRGB()).toUpperCase(Locale.getDefault()),
                x + tW + 44, y + 2, new Color(234, 234, 234).getRGB()
        );

        if (aHeight > 1) {
            if (OSUtil.supportShader()) {
                GradientUtils.applyGradient(
                        x + tW + 26, y + 15, 80f, aHeight, 1f,
                        Color.getHSBColor(customColor.hue, 0.0f, 0f),
                        Color.getHSBColor(customColor.hue, 0f, 1f),
                        Color.getHSBColor(customColor.hue, 1f, 0f),
                        Color.getHSBColor(customColor.hue, 1f, 1f),
                        UiScale.getScale(),
                        () -> Rects.roundedImage(
                                Math.round(x + tW + 26), Math.round(y + 16), 80, Math.round(max(aHeight, 1f)), 4,
                                new Color(255, 255, 255)
                        )
                );
            }else {
                for (int i = 0; i < aHeight; i++) {
                    for (int j = 0; j < 80; j++) {
                        float brightness = 1 - (float) i / aHeight;
                        float saturation = (float) j / 80;
                        Rects.fill(x + tW + 26 + j, y + 16 + i, 1, 1, Color.getHSBColor(customColor.hue, saturation, brightness).getRGB());
                    }
                }
            }

            float saturation = customColor.saturation;
            float brightness = customColor.brightness;

            if (Hover.is(
                    x + tW + 26, y + 16, 80f, 80f, (int) mouseX, (int) mouseY
            ) && Mouse.isButtonDown(0) || MainPanel.dragLock.equals(mod.name + setting.name + 1)) {
                if (MainPanel.dragLock.equals("null") && Mouse.isButtonDown(0)) {
                    MainPanel.dragLock = mod.name + setting.name + 1;
                }
                if (MainPanel.dragLock.equals(mod.name + setting.name + 1)) {
                    saturation = max(min((mouseX - (x + tW + 26)) / 80, 1f), 0f);
                    brightness = max(min(1 - (mouseY - (y + 15)) / 80, 1f), 0f);
                }
            }

            // Draw the selected position circle
            float cX = saturation * 80;
            float cY = (1 - brightness) * aHeight;
            Images.draw(
                    new ResourceLocation("client/gui/settings/values/color.png"),
                    x + tW + 26 + cX - 2.5f, y + 15 + cY - 2.5f, 5f, 5f, -1
            );

            // Hue adjustment
            float hue = customColor.hue;
            Gradients.hue(x + tW + 110, y + 16, 10, aHeight);
            Images.draw(
                    new ResourceLocation("client/gui/settings/values/color.png"),
                    x + tW + 112.5f, y + 14 + aHeight * customColor.hue,
                    5f, 5f, -1
            );

            if (Hover.is(
                    x + tW + 110, y + 16, 10f, aHeight, (int) mouseX, (int) mouseY
            ) && Mouse.isButtonDown(0) || MainPanel.dragLock.equals(mod.name + setting.name + 2)) {
                if (MainPanel.dragLock.equals("null")) {
                    MainPanel.dragLock = mod.name + setting.name + 2;
                }
                if (MainPanel.dragLock.equals(mod.name + setting.name + 2)) {
                    hue = max(min((mouseY - (y + 15)) / aHeight, 1f), 0f);
                }
            }

            // Alpha adjustment
            float alpha = customColor.alpha;
            Images.draw(
                    new ResourceLocation("client/gui/settings/values/alpha.png"),
                    x + tW + 122, y + 16, 10f, aHeight, -1
            );
            if (OSUtil.supportShader()) {
                GradientUtils.drawGradientVertical(
                        x + tW + 122, y + 16, 10f, aHeight, new Color(255, 255, 255), new Color(255, 255, 255, 0)
                );
            }

            Images.draw(
                    new ResourceLocation("client/gui/settings/values/color.png"),
                    x + tW + 124.5f, y + 13.5f + aHeight * (1 - alpha), 5f, 5f, -1
            );

            if (Hover.is(
                    x + tW + 122, y + 16, 10f, aHeight, (int) mouseX, (int) mouseY
            ) && Mouse.isButtonDown(0) || MainPanel.dragLock.equals(mod.name + setting.name + 3)) {
                if (MainPanel.dragLock.equals("null")) {
                    MainPanel.dragLock = mod.name + setting.name + 3;
                }
                if (MainPanel.dragLock.equals(mod.name + setting.name + 3)) {
                    alpha = max(min(1 - (mouseY - (y + 15)) / aHeight, 1f), 0f);
                }
            }

            if (!Mouse.isButtonDown(0)) {
                MainPanel.dragLock = "null";
            }

            // Apply the modified color
            if (hue != customColor.hue || saturation != customColor.saturation || brightness != customColor.brightness || alpha != customColor.alpha) {
                binding.setHsba(hue, saturation, brightness, alpha);
            }
        } else {
            // Keep value stable; no-op
        }

        aHeight = expand ? (float) AnimMath.base(aHeight, 80.0, 0.2) : (float) AnimMath.base(aHeight, 0.0, 0.2);

        ScaledGuiScreen screen = ScaledGuiScreen.getActiveScreen();
        if (screen != null) {
            float labelW = FPSMaster.fontManager.s16.getStringWidth(
                    FPSMaster.i18n.get((mod.name + "." + setting.name).toLowerCase(Locale.getDefault()))
            );
            ScaledGuiScreen.ConsumedClick click = screen.consumeClickInBounds(x + labelW + 26, y + 1, 80f, 14f);
            if (click != null && click.button == 0) {
                expand = !expand;
            }
        }
        this.height = aHeight + 20;
    }

}




