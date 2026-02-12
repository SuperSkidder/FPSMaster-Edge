package top.fpsmaster.features.settings;

import top.fpsmaster.event.EventDispatcher;
import top.fpsmaster.event.events.EventValueChange;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Setting<T> {

    public String name;
    public T value;
    public VisibleCondition visible;

    private final List<ChangeListener<T>> changeListeners = new CopyOnWriteArrayList<>();

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Setting(String name, T value, VisibleCondition visible) {
        this(name, value);
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible == null || visible.isVisible();
    }

    @FunctionalInterface
    public interface ChangeListener<T> {
        void onValueChanged(Setting<T> setting, T oldValue, T newValue);
    }

    public void addChangeListener(ChangeListener<T> listener) {
        if (listener != null) {
            changeListeners.add(listener);
        }
    }

    public void removeChangeListener(ChangeListener<T> listener) {
        changeListeners.remove(listener);
    }

    public void clearChangeListeners() {
        changeListeners.clear();
    }

    // Functional interface to represent the visibility check (similar to the Kotlin lambda)
    public interface VisibleCondition {
        boolean isVisible();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        T oldValue = this.value;
        if (!fireValueChangeEvent(oldValue, value)) {
            return;
        }
        this.value = value;
        notifyChangeListeners(oldValue, value);
    }

    protected boolean fireValueChangeEvent(T oldValue, T newValue) {
        EventValueChange event = new EventValueChange(this, oldValue, newValue);
        EventDispatcher.dispatchEvent(event);
        return !event.isCanceled();
    }

    protected void notifyChangeListeners(T oldValue, T newValue) {
        for (ChangeListener<T> listener : changeListeners) {
            try {
                listener.onValueChanged(this, oldValue, newValue);
            } catch (Throwable ignored) {
                // Listener failures must not break setting updates.
            }
        }
    }
}



