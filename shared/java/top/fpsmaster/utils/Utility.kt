package top.fpsmaster.utils

import net.minecraft.client.Minecraft
import top.fpsmaster.interfaces.ProviderManager
import top.fpsmaster.modules.dev.DevMode

open class Utility {
    companion object {
        @JvmField
        var mc: Minecraft = Minecraft.getMinecraft()

        @JvmStatic
        fun sendClientMessage(msg: String?) {
            if (ProviderManager.mcProvider.getWorld() != null) {
                ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent(msg))
            }
        }

        @JvmStatic
        fun sendClientNotify(msg: String?) {
            if (ProviderManager.mcProvider.getWorld() != null) {
                ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent("§9[FPSMaster]§r $msg"))
            }
        }

        @JvmStatic
        fun sendClientDebug(msg: String?) {
            if (DevMode.INSTACE.dev)
                if (ProviderManager.mcProvider.getWorld() != null) {
                    ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent("§9[FPSMaster]§r $msg"))
                }
        }
    }
}
