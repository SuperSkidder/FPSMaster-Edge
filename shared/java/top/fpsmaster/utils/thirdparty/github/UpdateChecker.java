package top.fpsmaster.utils.thirdparty.github;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import top.fpsmaster.utils.GitInfo;
import top.fpsmaster.utils.os.HttpRequest;

import java.io.IOException;

public class UpdateChecker {
    public static String getLatestVersion() {
        return HttpRequest.get("https://service.fpsmaster.top/api/github/latest/commit?branch=refs/heads/"+ GitInfo.getBranch());
    }
}