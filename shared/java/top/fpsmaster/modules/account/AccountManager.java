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

import java.util.HashMap;

public class AccountManager {
    private String token = "";
    private String username = "";
    private String[] itemsHeld = new String[0];

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

    private boolean attemptLogin(String username, String token) throws NetworkException {
        if (username.isEmpty() || token.isEmpty()) {
            return false;
        }
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization","Bearer " + token);
            System.out.println("Bearer " + token);
            String s = HttpRequest.get(FPSMaster.SERVICE_API + "/api/auth/validate-jwt", headers);
            JsonObject json = parser.parse(s).getAsJsonObject();
            this.username = username;
            this.token = token;
            return json.get("data").getAsJsonObject().get("success").getAsBoolean();
        } catch (Exception e) {
            throw new NetworkException("Failed to login via token", e);
        }
    }

    public static JsonParser parser = new JsonParser();
    public static String cape = "";
    public static String skin = "";

    public static JsonObject login(String username, String password) throws NetworkException {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("username", username);
            body.addProperty("password", password);
            String s = HttpRequest.post(FPSMaster.SERVICE_API + "/api/auth/login", body.toString());

            JsonObject jsonObject = parser.parse(s).getAsJsonObject();
            if (!jsonObject.get("data").getAsJsonObject().get("success").getAsBoolean()) {
                throw new NetworkException("Login failed: " + jsonObject.get("message").getAsString());
            }
            return jsonObject;
        } catch (Exception e) {
            if (e instanceof NetworkException) {
                throw (NetworkException) e;
            }
            throw new NetworkException("Login failed", e);
        }
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

    public String[] getItemsHeld() {
        return itemsHeld;
    }

    public void setItemsHeld(String[] itemsHeld) {
        this.itemsHeld = itemsHeld;
    }
}
