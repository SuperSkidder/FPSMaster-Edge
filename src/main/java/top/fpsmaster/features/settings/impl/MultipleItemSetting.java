package top.fpsmaster.features.settings.impl;

import net.minecraft.item.ItemStack;
import top.fpsmaster.features.settings.Setting;

import java.util.ArrayList;

public class MultipleItemSetting extends Setting<ArrayList<ItemStack>> {
    public static final int MAX_CAPACITY = 7;
    public MultipleItemSetting(String name) {
        super(name, new ArrayList<>());
    }

    public MultipleItemSetting(String name, VisibleCondition condition) {
        super(name, new ArrayList<>(), condition);
    }

    public void addItem(ItemStack itemStack) {
        if (this.getValue().size() < MAX_CAPACITY) {
            this.getValue().add(itemStack);
        }
    }

    public void addItemAndNotify(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        ArrayList<ItemStack> current = this.getValue();
        if (current.size() >= MAX_CAPACITY) {
            return;
        }
        ArrayList<ItemStack> oldSnapshot = new ArrayList<>(current);
        ArrayList<ItemStack> newSnapshot = new ArrayList<>(current);
        newSnapshot.add(itemStack);
        if (!fireValueChangeEvent(oldSnapshot, newSnapshot)) {
            return;
        }
        current.add(itemStack);
        notifyChangeListeners(oldSnapshot, newSnapshot);
    }

    public void removeItem(int index) {
        ItemStack itemStack = this.getValue().get(index);
        this.getValue().remove(itemStack);
    }

    public void removeItemAndNotify(int index) {
        ArrayList<ItemStack> current = this.getValue();
        if (index < 0 || index >= current.size()) {
            return;
        }
        ArrayList<ItemStack> oldSnapshot = new ArrayList<>(current);
        ArrayList<ItemStack> newSnapshot = new ArrayList<>(current);
        newSnapshot.remove(index);
        if (!fireValueChangeEvent(oldSnapshot, newSnapshot)) {
            return;
        }
        removeItem(index);
        notifyChangeListeners(oldSnapshot, newSnapshot);
    }

}



