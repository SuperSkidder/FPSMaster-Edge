package top.fpsmaster.features.settings.impl;

import top.fpsmaster.features.settings.Setting;
import top.fpsmaster.features.settings.impl.utils.CustomColor;

import java.awt.*;

public class ColorSetting extends Setting<CustomColor> {

    public ColorSetting(String name, CustomColor value, VisibleCondition visible) {
        super(name, value, visible);
    }

    public ColorSetting(String name, Color value, VisibleCondition visible) {
        super(name, new CustomColor(value), visible);
    }

    public ColorSetting(String name, CustomColor value) {
        super(name, value);
    }

    public ColorSetting(String name, Color value) {
        super(name, new CustomColor(value));
    }

    public int getRGB() {
        return getValue().getRGB();
    }

    public Color getColor() {
        return getValue().getColor();
    }

    public void setColor(float hue, float saturation, float brightness, float alpha) {
        CustomColor v = getValue();
        v.setColor(hue, saturation, brightness, alpha);
        notifyValueChanged();
    }

    public void setColor(Color color) {
        CustomColor v = getValue();
        v.setColor(color);
        notifyValueChanged();
    }
}



