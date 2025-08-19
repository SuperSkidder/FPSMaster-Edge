package top.fpsmaster.api.interfaces.settings;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.settings.GameSettings;
import top.fpsmaster.api.interfaces.client.settings.IGameSettings;
import top.fpsmaster.api.interfaces.client.settings.IKeyBinding;

@RequiredArgsConstructor
public class IGameSettingsImpl implements IGameSettings {
    private final GameSettings gameSettings;

    @Override
    public IKeyBinding getKeyBindSprint() {
        return new IKeyBindingImpl(gameSettings.keyBindSprint);
    }
}
