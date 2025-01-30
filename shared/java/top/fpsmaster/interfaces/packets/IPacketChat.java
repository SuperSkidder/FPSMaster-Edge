package top.fpsmaster.interfaces.packets;

public interface IPacketChat extends IPacket {
    String getUnformattedText(Object packet);
    int getType(Object p);
    void appendTranslation(Object p);
}
