package top.fpsmaster.modules.client;

import top.fpsmaster.websocket.data.message.server.SFetchPlayerPacket;

import java.util.ArrayList;

public class ClientUsersManager {
    public ArrayList<ClientUser> users = new ArrayList<>();

    public void addFromFetch(SFetchPlayerPacket packet) {
        ClientUser clientUser = new ClientUser(packet.uid, packet.name, packet.uuid, packet.gameId, packet.cosmetics, packet.skin, packet.rank, packet.customRank);
        for (ClientUser user : users) {
            if (user.uid.equals(clientUser.uid))
                return;
        }
        users.add(clientUser);
    }
}
