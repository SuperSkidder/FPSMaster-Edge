package top.fpsmaster.features.impl.utility;

import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.features.settings.impl.BooleanSetting;

public class IRC extends Module {
    public static boolean using = false;
    public static final BooleanSetting showMates = new BooleanSetting("showMates", true);

    public IRC() {
        super("IRC", Category.Utility);
        addSettings(showMates);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        using = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        using = false;
    }
}
