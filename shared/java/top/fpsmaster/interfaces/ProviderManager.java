package top.fpsmaster.interfaces;

import top.fpsmaster.interfaces.client.IConstantsProvider;
import top.fpsmaster.interfaces.game.*;
import top.fpsmaster.interfaces.gui.IGuiIngameProvider;
import top.fpsmaster.interfaces.gui.IGuiMainMenuProvider;
import top.fpsmaster.interfaces.packets.IPacketChat;
import top.fpsmaster.interfaces.packets.IPacketPlayerList;
import top.fpsmaster.interfaces.packets.IPacketTimeUpdate;
import top.fpsmaster.interfaces.render.IEffectRendererProvider;
import top.fpsmaster.interfaces.render.IRenderManagerProvider;
import top.fpsmaster.interfaces.sound.ISoundProvider;
import top.fpsmaster.wrapper.*;
import top.fpsmaster.wrapper.packets.SPacketChatProvider;
import top.fpsmaster.wrapper.packets.SPacketPlayerListProvider;
import top.fpsmaster.wrapper.packets.SPacketTimeUpdateProvider;
import top.fpsmaster.wrapper.sound.SoundProvider;

public class ProviderManager {
    public static final IConstantsProvider constants = new Constants();
    public static IUtilityProvider utilityProvider = new UtilityProvider();
    public static final IMinecraftProvider mcProvider = new MinecraftProvider();
    public static final IGuiMainMenuProvider mainmenuProvider = new GuiMainMenuProvider();
    public static final ISkinProvider skinProvider = new SkinProvider();
    public static final IWorldClientProvider worldClientProvider = new WorldClientProvider();
    public static final ITimerProvider timerProvider = new TimerProvider();
    public static final IRenderManagerProvider renderManagerProvider = new RenderManagerProvider();
    public static final IGameSettings gameSettings = new GameSettingsProvider();

    // Packets
    public static final IPacketChat packetChat = new SPacketChatProvider();
    public static final IPacketPlayerList packetPlayerList = new SPacketPlayerListProvider();
    public static final IPacketTimeUpdate packetTimeUpdate = new SPacketTimeUpdateProvider();
    public static final IGuiIngameProvider guiIngameProvider = new GuiIngameProvider();
    public static final ISoundProvider soundProvider = new SoundProvider();
    public static final IEffectRendererProvider effectManager = new EffectRendererProvider();
}
