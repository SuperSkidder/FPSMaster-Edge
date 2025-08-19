package top.fpsmaster.api.interfaces.client.settings;

public interface IKeyBinding {
    void setKeyBindState(int keyCode, boolean state);

    int getKeyCode();
}
