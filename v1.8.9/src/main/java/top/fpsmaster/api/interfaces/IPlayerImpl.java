package top.fpsmaster.api.interfaces;

import net.minecraft.client.entity.EntityPlayerSP;

public class IPlayerImpl implements IPlayer{
    EntityPlayerSP playerSP;
    public IPlayerImpl(EntityPlayerSP playerSP) {
        this.playerSP = playerSP;
    }

    @Override
    public String getName() {
        return playerSP.getName();
    }
}
