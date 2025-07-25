package top.fpsmaster.features.impl.utility;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.event.Subscribe;
import top.fpsmaster.event.events.EventPacket;
import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.features.settings.impl.BooleanSetting;
import top.fpsmaster.features.settings.impl.ModeSetting;
import top.fpsmaster.features.settings.impl.NumberSetting;
import top.fpsmaster.features.settings.impl.TextSetting;
import top.fpsmaster.interfaces.ProviderManager;
import top.fpsmaster.utils.Utility;

public class AutoGG extends Module {
    public BooleanSetting autoPlay = new BooleanSetting("AutoPlay", false);
    public NumberSetting delay = new NumberSetting("DelayToPlay", 5, 0, 10, 1, () -> autoPlay.getValue());
    public TextSetting message = new TextSetting("Message", "gg");
    public ModeSetting servers = new ModeSetting("Servers", 0, "hypxiel", "kkcraft");
    public String[] hypixelTrigger = new String[]{"Reward Summary", "1st Killer", "Damage Dealt", "奖励总览", "击杀数第一名", "造成伤害"};
    public String[] kkcraftTrigger = new String[]{"获胜者", "第一名杀手", "击杀第一名"};

    public AutoGG() {
        super("AutoGG", Category.Utility);
        this.addSettings(autoPlay, delay, message, servers);
    }

    @Subscribe
    public void onPacket(EventPacket event) {
        if (event.type == EventPacket.PacketType.RECEIVE && ProviderManager.packetChat.isPacket(event.packet)) {
            IChatComponent componentValue = ProviderManager.packetChat.getChatComponent(event.packet);
            String chatMessage = componentValue.getUnformattedText();
            switch (servers.getValue()) {
                case 0:
                    for (String s : hypixelTrigger) {
                        if (StringUtils.stripControlCodes(chatMessage).contains(s)) {
                            Utility.sendChatMessage("/ac " + message.getValue());
                            break;
                        }
                    }
                    if (autoPlay.getValue()) {
                        for (IChatComponent chatComponent : componentValue.getSiblings()) {
                            ClickEvent clickEvent = chatComponent.getChatStyle().getChatClickEvent();
                            if (clickEvent != null && clickEvent.getAction().equals(ClickEvent.Action.RUN_COMMAND) && clickEvent.getValue().trim().toLowerCase().startsWith("/play ")) {
                                Utility.sendClientNotify("Sending you to the next game in " + delay.getValue() + " seconds");
                                FPSMaster.async.runnable(() -> {
                                    try {
                                        Thread.sleep(delay.getValue().longValue() * 1000);
                                        Utility.sendChatMessage(clickEvent.getValue());
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        }
                    }
                    break;
                case 1:
                    for (String s : kkcraftTrigger) {
                        if (StringUtils.stripControlCodes(chatMessage).contains(s)) {
                            Utility.sendChatMessage(message.getValue());
                            if(autoPlay.getValue()) {
                                Utility.sendClientNotify("AutoPlay is not supported at the moment in KKCraft");
                            }
                        }
                    }
                    break;
                default:

            }
        }
    }
}
