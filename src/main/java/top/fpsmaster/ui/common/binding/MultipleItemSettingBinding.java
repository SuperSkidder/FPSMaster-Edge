package top.fpsmaster.ui.common.binding;

import net.minecraft.item.ItemStack;
import top.fpsmaster.features.settings.Setting;
import top.fpsmaster.features.settings.impl.MultipleItemSetting;

import java.util.ArrayList;

public final class MultipleItemSettingBinding implements ValueBinding<ArrayList<ItemStack>> {
    private final MultipleItemSetting setting;

    public MultipleItemSettingBinding(MultipleItemSetting setting) {
        this.setting = setting;
    }

    @Override
    public ArrayList<ItemStack> get() {
        return setting.getValue();
    }

    @Override
    public void set(ArrayList<ItemStack> value) {
        setting.setValue(value);
    }

    public void addItem(ItemStack itemStack) {
        setting.addItemAndNotify(itemStack);
    }

    public void removeItem(int index) {
        setting.removeItemAndNotify(index);
    }

    @Override
    public Subscription subscribe(Listener<ArrayList<ItemStack>> listener) {
        if (listener == null) {
            return () -> {
            };
        }

        Setting.ChangeListener<ArrayList<ItemStack>> adapter = (s, oldValue, newValue) -> listener.onChanged(oldValue, newValue);
        setting.addChangeListener(adapter);
        return () -> setting.removeChangeListener(adapter);
    }
}
