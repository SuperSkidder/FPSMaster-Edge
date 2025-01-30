package top.fpsmaster.utils.thirdparty.github;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import top.fpsmaster.utils.os.HttpRequest;

import java.io.IOException;

public class UpdateChecker {
    public static String getLatestVersion() {
        String json = HttpRequest.get("https://api.github.com/repos/FPSMasterTeam/FPSMaster/releases/latest");
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        return jsonObject.get("tag_name").getAsString();
    }
}