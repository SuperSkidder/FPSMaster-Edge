package top.fpsmaster.modules.account;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.exception.AccountException;
import top.fpsmaster.exception.ExceptionHandler;
import top.fpsmaster.exception.FileException;
import top.fpsmaster.exception.NetworkException;
import top.fpsmaster.modules.logger.ClientLogger;
import top.fpsmaster.utils.os.FileUtils;
import top.fpsmaster.utils.os.HttpRequest;

import java.io.IOException;
import java.util.HashMap;

public class AccountManager {
    private String token = "";
    private String username = "";
    public static JsonParser parser = new JsonParser();
    public static String skin = "";

    public void autoLogin() {
        FPSMaster.async.runnable(() -> {
            try {
                doAutoLogin();
            } catch (FileException e) {
                ExceptionHandler.handleFileException(e, "尝试自动登录失败");
            } catch (AccountException e) {
                ExceptionHandler.handleAccountException(e, "尝试自动登录失败");
            } catch (NetworkException e) {
                ExceptionHandler.handleNetworkException(e, "尝试自动登录失败");
            } catch (Exception e) {
                ExceptionHandler.handle(e, "尝试自动登录失败");
            }
        });
    }

    private void doAutoLogin() throws FileException, AccountException, NetworkException {
        token = FileUtils.readTempValue("token").trim();
        username = FPSMaster.configManager.configure.getOrCreate("username", "").trim();
        if (!token.isEmpty() && !username.isEmpty()) {
            if (attemptLogin(username, token)) {
                ClientLogger.info("自动登录成功！  " + username);
                FPSMaster.INSTANCE.loggedIn = true;
            } else {
                ClientLogger.info(username);
                ClientLogger.error("自动登录失败！");
                throw new AccountException("自动登录失败");
            }
        }
    }

    private boolean attemptLogin(String username, String token) throws AccountException {
        if (username.isEmpty() || token.isEmpty()) {
            return false;
        }
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            HttpRequest.HttpResponseResult s = HttpRequest.get(FPSMaster.SERVICE_API + "/api/auth/validate-jwt", headers);
            JsonObject json = parser.parse(s.getBody()).getAsJsonObject();
            if (!s.isSuccess()){
                throw new AccountException("Failed to login via token " + s.getStatusCode());
            }
            this.username = username;
            this.token = token;
            return json.get("data").getAsJsonObject().get("success").getAsBoolean();
        } catch (Exception e) {
            throw new AccountException("Failed to login via token");
        }
    }


    public static JsonObject login(String username, String password) throws AccountException {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        HttpRequest.HttpResponseResult s = null;
        try {
            s = HttpRequest.post(FPSMaster.SERVICE_API + "/api/auth/login", body.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject jsonObject = parser.parse(s.getBody()).getAsJsonObject();
        if (jsonObject.get("data") == null || !jsonObject.get("data").getAsJsonObject().get("success").getAsBoolean()) {
            throw new AccountException("登录失败： " + jsonObject.get("message").getAsString());
        }
        return jsonObject;

    }

    // Getter and Setter methods
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
