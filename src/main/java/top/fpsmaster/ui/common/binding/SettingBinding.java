package top.fpsmaster.ui.common.binding;

import top.fpsmaster.features.settings.Setting;

public final class SettingBinding<T> implements ValueBinding<T> {
    private final Setting<T> setting;

    public SettingBinding(Setting<T> setting) {
        this.setting = setting;
    }

    @Override
    public T get() {
        return setting.getValue();
    }

    @Override
    public void set(T value) {
        setting.setValue(value);
    }

    @Override
    public Subscription subscribe(Listener<T> listener) {
        if (listener == null) {
            return () -> {
            };
        }

        Setting.ChangeListener<T> adapter = (s, oldValue, newValue) -> listener.onChanged(oldValue, newValue);
        setting.addChangeListener(adapter);
        return () -> setting.removeChangeListener(adapter);
    }
}
