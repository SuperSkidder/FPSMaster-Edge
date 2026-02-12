package top.fpsmaster.ui.click.modules.impl;

import top.fpsmaster.utils.render.draw.Images;
import top.fpsmaster.utils.render.draw.Hover;
import top.fpsmaster.utils.render.draw.Rects;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.features.settings.impl.ModeSetting;
import top.fpsmaster.ui.click.modules.SettingRender;
import top.fpsmaster.utils.math.anim.AnimMath;
import top.fpsmaster.utils.render.gui.UiScale;
import top.fpsmaster.ui.common.binding.SettingBinding;
import top.fpsmaster.utils.render.gui.ScaledGuiScreen;

import java.awt.*;
import java.util.Locale;

public class ModeSettingRender extends SettingRender<ModeSetting> {
    private boolean expand = false;
    private float expandH = 0f;
    private final SettingBinding<Integer> binding;

    public ModeSettingRender(Module mod, ModeSetting setting) {
        super(setting);
        this.mod = mod;
        this.binding = new SettingBinding<>(setting);
    }

    @Override
    public void render(float x, float y, float width, float height, float mouseX, float mouseY, boolean custom) {
        float fw = FPSMaster.fontManager.s16.drawString(
                FPSMaster.i18n.get((mod.name + "." + setting.name).toLowerCase(Locale.getDefault())),
                x + 10, y + 8, new Color(162, 162, 162).getRGB()
        );
        float maxWidth = 80f;
        maxWidth = Math.max(maxWidth, fw + 10);
        Rects.roundedBorder(
                Math.round(x + 16 + fw),
                Math.round(y + 4),
                Math.round(maxWidth),
                Math.round(16 + expandH),
                3,
                0.5f,
                new Color(52, 52, 52).getRGB(),
                new Color(255, 255, 255, 50).getRGB()
        );
        FPSMaster.fontManager.s18.drawString(
                FPSMaster.i18n.get((mod.name + "." + setting.name + "." + setting.getModeName()).toLowerCase(Locale.getDefault())),
                x + 20 + fw, y + 7, new Color(234, 234, 234).getRGB()
        );

        // Rotate this icon
        GL11.glPushMatrix();
        float rotatePercent = expandH / (setting.getModesSize() * 14);
        GL11.glTranslatef((x + 16 + fw + maxWidth - 12) * UiScale.getScale(), (y + 12) * UiScale.getScale(), 0f);
        GL11.glRotatef(rotatePercent * 180, 0f, 0f, 1f);
        GL11.glTranslatef(-(x + 16 + fw + maxWidth - 12) * UiScale.getScale(), -(y + 12) * UiScale.getScale(), 0f);
        Images.draw(
                new ResourceLocation("client/gui/settings/icons/arrow.png"),
                x + 16 + fw + maxWidth - 16,
                y + 8,
                8f,
                8f,
                new Color(234, 234, 234)
        );
        GL11.glPopMatrix();
        if (expand) {
            expandH = (float) AnimMath.base(expandH, setting.getModesSize() * 14, 0.2);
            for (int i = 1; i <= setting.getModesSize(); i++) {
                if (Hover.is(x + 20 + fw, y + 4 + i * 14, maxWidth, 16f, (int) mouseX, (int) mouseY)) {
                    FPSMaster.fontManager.s16.drawString(
                            FPSMaster.i18n.get((mod.name + "." + setting.name + "." + setting.getMode(i)).toLowerCase(Locale.getDefault())),
                            x + 20 + fw, y + 7 + i * 14, new Color(182, 182, 182).getRGB()
                    );
                } else {
                    FPSMaster.fontManager.s16.drawString(
                            FPSMaster.i18n.get((mod.name + "." + setting.name + "." + setting.getMode(i)).toLowerCase(Locale.getDefault())),
                            x + 20 + fw, y + 7 + i * 14, new Color(162, 162, 162).getRGB()
                    );
                }
            }
        } else {
            expandH = (float) AnimMath.base(expandH, 0.0, 0.2);
        }

        ScaledGuiScreen screen = ScaledGuiScreen.getActiveScreen();
        if (screen != null) {
            String label = FPSMaster.i18n.get((mod.name + "." + setting.name).toLowerCase(Locale.getDefault()));
            float labelW = FPSMaster.fontManager.s16.getStringWidth(label);
            float clickWidth = Math.max(80f, labelW + 10);
            ScaledGuiScreen.ConsumedClick headClick = screen.consumeClickInBounds(x + 16 + labelW, y + 4, clickWidth, 16f);
            if (headClick != null) {
                expand = !expand;
            } else if (expand) {
                for (int i = 1; i <= setting.getModesSize(); i++) {
                    ScaledGuiScreen.ConsumedClick itemClick = screen.consumeClickInBounds(x + 20 + labelW, y + 4 + i * 14, clickWidth, 16f);
                    if (itemClick != null) {
                        binding.set(i - 1);
                        expand = false;
                        break;
                    }
                }
            }
        }
        this.height = 24 + expandH;
    }

}




