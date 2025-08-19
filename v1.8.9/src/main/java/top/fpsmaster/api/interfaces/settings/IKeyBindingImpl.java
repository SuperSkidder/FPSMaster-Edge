package top.fpsmaster.api.interfaces.settings;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.Nullable;
import top.fpsmaster.api.interfaces.client.settings.IKeyBinding;

@AllArgsConstructor
@NoArgsConstructor
public class IKeyBindingImpl implements IKeyBinding {
    private @Nullable KeyBinding keyBinding;

    @Override
    public void setKeyBindState(int keyCode, boolean state) {
        KeyBinding.setKeyBindState(keyCode, state);
    }

    @Override
    public int getKeyCode() {
        return keyBinding == null ? 0 : keyBinding.getKeyCode();
    }
}
