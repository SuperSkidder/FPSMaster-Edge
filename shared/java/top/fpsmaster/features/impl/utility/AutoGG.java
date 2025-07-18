package top.fpsmaster.features.impl.utility;

import net.minecraft.util.StringUtils;
import top.fpsmaster.event.Subscribe;
import top.fpsmaster.event.events.EventPacket;
import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.features.settings.impl.ModeSetting;
import top.fpsmaster.features.settings.impl.TextSetting;
import top.fpsmaster.interfaces.ProviderManager;
import top.fpsmaster.modules.logger.ClientLogger;
import top.fpsmaster.utils.Utility;

public class AutoGG extends Module {
    public TextSetting message = new TextSetting("Message", "gg");
    public ModeSetting servers = new ModeSetting("Servers", 0, "hypxiel");
    public AutoGG() {
        super("AutoGG", Category.Utility);
        this.addSettings(message,servers);
    }
    @Subscribe
    public void onPacket(EventPacket event) {
        if(event == null) return;
        // judge packet type
        if(event.type == EventPacket.PacketType.RECEIVE && ProviderManager.packetChat.isPacket(event.packet)){
            switch (servers.getValue()){
                // hypxiel
                case 0:
                    // get chat message
                    String componentValue = ProviderManager.packetChat.getChatComponent(event.packet).toString();
                    ClientLogger.info(componentValue);
                    // check message is game over
                    String chatMessage = ProviderManager.packetChat.getUnformattedText(event.packet);
                    boolean hasPlayCommand = componentValue.contains("ClickEvent{action=RUN_COMMAND, value='/play ");
                    boolean hasDiedInformation = StringUtils.stripControlCodes(chatMessage).startsWith("You died!");
                    if (hasPlayCommand && !hasDiedInformation) {
                        // send chat packet message
                        Utility.sendChatMessage("/ac " + message.getValue());
                    }
                    break;
                default:

            }
        }
    }
}
