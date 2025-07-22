package top.fpsmaster.interfaces.gui;

import net.minecraft.client.gui.ChatLine;

import java.util.List;

public interface IGuiNewChatProvider {
    List<ChatLine> getChatLines();

    List<ChatLine> getDrawnChatLines();
}
