package top.fpsmaster.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.exception.FileException;

@net.minecraftforge.fml.common.Mod(modid = "fpsmaster", useMetadata = true)
public class Mod {
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventAPI());
        FPSMaster.INSTANCE.initialize();
    }
}
