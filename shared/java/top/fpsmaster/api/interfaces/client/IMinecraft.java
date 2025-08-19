package top.fpsmaster.api.interfaces.client;

import org.jetbrains.annotations.Nullable;
import top.fpsmaster.api.interfaces.client.settings.IGameSettings;
import top.fpsmaster.api.interfaces.client.settings.IKeyBinding;

public interface IMinecraft {
    @Nullable IClientPlayerEntity getClientPlayer();

    IKeyBinding getKeyBinding();

    IGameSettings getGameSettings();
}
