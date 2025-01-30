package top.fpsmaster.features.impl.interfaces;

import org.lwjgl.input.Keyboard;
import top.fpsmaster.features.impl.InterfaceModule;
import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.settings.impl.BindSetting;
import top.fpsmaster.features.settings.impl.BooleanSetting;

public class ClientSettings extends InterfaceModule {
    public static BindSetting keyBind = new BindSetting("ClickGuiKey", Keyboard.KEY_RSHIFT);
    public static BooleanSetting fixedScale = new BooleanSetting("FixedScale", true);

    public ClientSettings() {
        super("ClientSettings", Category.Interface);
        addSettings(keyBind, fixedScale);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.set(false);
    }
}
