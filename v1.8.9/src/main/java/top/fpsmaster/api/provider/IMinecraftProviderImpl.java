package top.fpsmaster.api.provider;

import net.minecraft.client.Minecraft;
import top.fpsmaster.api.interfaces.IMinecraft;
import top.fpsmaster.api.interfaces.IMinecraftImpl;

public class IMinecraftProviderImpl implements IMinecraftProvider {
    IMinecraftImpl mcImpl;
    Minecraft mc;

    @Override
    public IMinecraft getMinecraft() {
        if (mcImpl == null || mc != Minecraft.getMinecraft()) {
            mc = Minecraft.getMinecraft();
            mcImpl = new IMinecraftImpl(mc);
        }
        return mcImpl;
    }
}
