package top.fpsmaster.features.impl.utility;

import top.fpsmaster.api.provider.ProviderRegistry;
import top.fpsmaster.event.Subscribe;
import top.fpsmaster.event.events.EventKey;
import top.fpsmaster.event.events.EventUpdate;
import top.fpsmaster.features.impl.InterfaceModule;
import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.settings.impl.BooleanSetting;

public class Sprint extends InterfaceModule {
    private final BooleanSetting toggleSprint = new BooleanSetting("ToggleSprint", true);
    public static boolean using = false;
    public static boolean sprint = true;

    public Sprint() {
        super("Sprint", Category.Utility);
        addSettings(toggleSprint, betterFont);
    }


    @Override
    public void onEnable() {
        super.onEnable();
        using = true;
    }

    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (!toggleSprint.getValue()) {
            sprint = true;
        }
    }

    @Subscribe
    public void onKey(EventKey e) {
        if (toggleSprint.getValue() && e.key == ProviderRegistry.getMinecraftProvider().getMinecraft().getGameSettings().getKeyBindSprint().getKeyCode()) {
            sprint = !sprint;
            if (!sprint) {
                if (ProviderRegistry.getMinecraftProvider().getMinecraft().getClientPlayer() != null) {
                    ProviderRegistry.getMinecraftProvider().getMinecraft().getClientPlayer().setSprinting(false);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ProviderRegistry.getMinecraftProvider().getMinecraft().getKeyBinding().setKeyBindState(ProviderRegistry.getMinecraftProvider().getMinecraft().getGameSettings().getKeyBindSprint().getKeyCode(), false);
        if (ProviderRegistry.getMinecraftProvider().getMinecraft().getClientPlayer() != null) {
            ProviderRegistry.getMinecraftProvider().getMinecraft().getClientPlayer().setSprinting(false);
        }
        using = false;
    }
}