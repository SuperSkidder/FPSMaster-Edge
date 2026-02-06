package top.fpsmaster.utils.core;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Utility {

    public static Minecraft mc = Minecraft.getMinecraft();

    static ArrayList<String> messages = new ArrayList<>();

    public static void sendChatMessage(String message) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.sendChatMessage(message);
    }

    public static void sendClientMessage(String msg) {
        if (mc.theWorld != null) {
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(msg));
        } else {
            messages.add(msg);
        }
    }

    public static void sendClientNotify(String msg) {
        String msg1 = "§9[FPSMaster]§r " + msg;
        if (mc.theWorld != null) {
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(msg1));
        } else {
            messages.add(msg1);
        }
    }

    public static void sendClientDebug(String msg) {
        sendClientNotify(msg);
    }

    public static void flush() {
        for (String message : messages) {
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
        }
        messages.clear();
    }
}



