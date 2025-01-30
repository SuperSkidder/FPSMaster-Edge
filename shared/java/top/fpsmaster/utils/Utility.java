package top.fpsmaster.utils;

import net.minecraft.client.Minecraft;
import top.fpsmaster.interfaces.ProviderManager;
import top.fpsmaster.modules.dev.DevMode;

public class Utility {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendClientMessage(String msg) {
        if (ProviderManager.mcProvider.getWorld() != null) {
            ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent(msg));
        }
    }

    public static void sendClientNotify(String msg) {
        if (ProviderManager.mcProvider.getWorld() != null) {
            ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent("§9[FPSMaster]§r " + msg));
        }
    }

    public static void sendClientDebug(String msg) {
        if (DevMode.INSTACE.dev) {
            if (ProviderManager.mcProvider.getWorld() != null) {
                ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent("§9[FPSMaster]§r " + msg));
            }
        }
    }
}
