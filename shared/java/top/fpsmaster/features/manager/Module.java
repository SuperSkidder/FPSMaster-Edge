package top.fpsmaster.features.manager;

import net.minecraft.client.Minecraft;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.event.EventDispatcher;
import top.fpsmaster.features.settings.Setting;
import top.fpsmaster.features.settings.impl.BooleanSetting;
import top.fpsmaster.features.settings.impl.BindSetting;
import top.fpsmaster.features.settings.impl.ModeSetting;
import top.fpsmaster.features.settings.impl.NumberSetting;
import top.fpsmaster.features.settings.impl.TextSetting;
import top.fpsmaster.features.settings.impl.ColorSetting;
import top.fpsmaster.interfaces.ProviderManager;
import top.fpsmaster.ui.notification.NotificationManager;

import java.util.LinkedList;
import java.util.Locale;

public class Module {

    public String name;
    public String description = "";
    public Category category;
    public LinkedList<Setting<?>> settings = new LinkedList<>();
    public int key = 0;

    private boolean isEnabled = false;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public void addSettings(Setting<?>... settings) {
        for (Setting<?> setting : settings) {
            if (setting != null) {
                if (setting instanceof BooleanSetting) {
                    this.settings.add((BooleanSetting) setting);
                } else if (setting instanceof BindSetting) {
                    this.settings.add((BindSetting) setting);
                } else if (setting instanceof ModeSetting) {
                    this.settings.add((ModeSetting) setting);
                } else if (setting instanceof NumberSetting) {
                    this.settings.add((NumberSetting) setting);
                } else if (setting instanceof TextSetting) {
                    this.settings.add((TextSetting) setting);
                } else if (setting instanceof ColorSetting) {
                    this.settings.add((ColorSetting) setting);
                }
            }
        }
    }

    public void toggle() {
        set(!isEnabled);
    }

    public void set(boolean state) {
        isEnabled = state;
        try {
            if (state) {
                onEnable();
                if (Minecraft.getMinecraft() != null && ProviderManager.mcProvider.getPlayer() != null) {
                    NotificationManager.addNotification(
                            FPSMaster.i18n.get("notification.module.enable"),
                            String.format(
                                    FPSMaster.i18n.get("notification.module.enable.desc"),
                                    FPSMaster.i18n.get(this.name.toLowerCase(Locale.getDefault()))
                            ),
                            2f
                    );
                }
            } else {
                onDisable();
                if (Minecraft.getMinecraft() != null && ProviderManager.mcProvider.getPlayer() != null) {
                    NotificationManager.addNotification(
                            FPSMaster.i18n.get("notification.module.disable"),
                            String.format(
                                    FPSMaster.i18n.get("notification.module.disable.desc"),
                                    FPSMaster.i18n.get(this.name.toLowerCase(Locale.getDefault()))
                            ),
                            2f
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEnable() {
        EventDispatcher.registerListener(this);
    }

    public void onDisable() {
        EventDispatcher.unregisterListener(this);
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
