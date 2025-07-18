package top.fpsmaster.utils.thirdparty.github;

import top.fpsmaster.utils.GitInfo;
import top.fpsmaster.utils.os.HttpRequest;

import java.io.IOException;

public class UpdateChecker {
    public static String getLatestVersion() {
        try {
            return HttpRequest.get("https://service.fpsmaster.top/api/github/latest/commit?branch=refs/heads/"+ GitInfo.getBranch()).getBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}