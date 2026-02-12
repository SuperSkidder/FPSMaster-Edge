package top.fpsmaster.ui.common.binding;

public interface ValueBinding<T> {
    @FunctionalInterface
    interface Listener<T> {
        void onChanged(T oldValue, T newValue);
    }

    T get();

    void set(T value);

    Subscription subscribe(Listener<T> listener);
}
