package top.fpsmaster.interfaces.game;

import net.minecraft.client.settings.KeyBinding;
import top.fpsmaster.interfaces.IProvider;

public interface IGameSettings extends IProvider {
    void setKeyPress(KeyBinding key, boolean value);
}