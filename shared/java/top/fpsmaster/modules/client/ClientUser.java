package top.fpsmaster.modules.client;

public class ClientUser {
    public String uid;
    public String name;
    public String uuid;
    public String gameId;
    public String cosmetics;
    public String skin;
    public String rank;
    public String customRank;
    public ClientUser(String uid, String name, String uuid, String gameId, String cosmetics, String skin, String rank, String customRank) {
        this.uid = uid;
        this.name = name;
        this.uuid = uuid;
        this.gameId = gameId;
        this.cosmetics = cosmetics;
        this.skin = skin;
        this.rank = rank;
        this.customRank = customRank;
    }
}
