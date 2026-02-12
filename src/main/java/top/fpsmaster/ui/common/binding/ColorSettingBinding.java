package top.fpsmaster.ui.common.binding;

import top.fpsmaster.features.settings.Setting;
import top.fpsmaster.features.settings.impl.ColorSetting;
import top.fpsmaster.features.settings.impl.utils.CustomColor;

import java.awt.Color;

public final class ColorSettingBinding implements ValueBinding<CustomColor> {
    private final ColorSetting setting;

    public ColorSettingBinding(ColorSetting setting) {
        this.setting = setting;
    }

    @Override
    public CustomColor get() {
        return setting.getValue();
    }

    @Override
    public void set(CustomColor value) {
        setting.setValue(value);
    }

    public void setHsba(float hue, float saturation, float brightness, float alpha) {
        setting.setColor(hue, saturation, brightness, alpha);
    }

    public void setColor(Color color) {
        setting.setColor(color);
    }

    @Override
    public Subscription subscribe(Listener<CustomColor> listener) {
        if (listener == null) {
            return () -> {
            };
        }

        Setting.ChangeListener<CustomColor> adapter = (s, oldValue, newValue) -> listener.onChanged(oldValue, newValue);
        setting.addChangeListener(adapter);
        return () -> setting.removeChangeListener(adapter);
    }
}
