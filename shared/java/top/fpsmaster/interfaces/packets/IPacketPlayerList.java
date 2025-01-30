package top.fpsmaster.interfaces.packets;

import java.util.List;

public interface IPacketPlayerList extends IPacket {
    boolean isActionJoin(Object p);
    boolean isActionLeave(Object p);
    List<IAddPlayerData> getEntries(Object p);
}