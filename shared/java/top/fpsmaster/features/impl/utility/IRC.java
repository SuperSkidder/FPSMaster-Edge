package top.fpsmaster.features.impl.utility;

import top.fpsmaster.FPSMaster;
import top.fpsmaster.event.Subscribe;
import top.fpsmaster.event.events.EventTick;
import top.fpsmaster.features.impl.interfaces.ClientSettings;
import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.features.settings.impl.BooleanSetting;
import top.fpsmaster.utils.Utility;
import top.fpsmaster.utils.math.MathTimer;
import top.fpsmaster.websocket.client.WsClient;
import top.fpsmaster.interfaces.ProviderManager;
import top.fpsmaster.modules.dev.DevMode;

import java.net.URISyntaxException;

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
