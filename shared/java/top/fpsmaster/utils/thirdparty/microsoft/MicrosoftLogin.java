package top.fpsmaster.utils.thirdparty.microsoft;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.util.Session;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import top.fpsmaster.ui.screens.account.GuiWaiting;
import top.fpsmaster.interfaces.ProviderManager;

import java.awt.Desktop;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MicrosoftLogin {

    private static final String CLIENT_ID = "d1ed1b72-9f7c-41bc-9702-365d2cbd2e38";
    private static HttpServer httpServer;

    public static void start() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            httpServer = HttpServer.create(new InetSocketAddress(17342), 0);
            System.out.println("Create login server");

            httpServer.createContext("/", exchange -> {
                Map<String, String> map = new HashMap<>();
                String requestURI = exchange.getRequestURI().toString();
                String result = "Login successfully! You can close this window now.";
                exchange.sendResponseHeaders(200, result.length());
                exchange.getResponseBody().write(result.getBytes(StandardCharsets.UTF_8));
                httpServer.stop(3);

                String code = requestURI.substring(requestURI.indexOf("=") + 1);
                map.put("client_id", CLIENT_ID);
                map.put("code", code);
                map.put("grant_type", "authorization_code");
                map.put("redirect_uri", "http://127.0.0.1:17342");

                String oauthResponse = postMap("https://login.live.com/oauth20_token.srf", map);
                String accessToken = gsonBuilder.create().fromJson(oauthResponse, JsonObject.class).get("access_token").getAsString();

                Map<String, Object> map2 = new HashMap<>();
                map2.put("AuthMethod", "RPS");
                map2.put("SiteName", "user.auth.xboxlive.com");
                map2.put("RpsTicket", "d=" + accessToken);

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("Properties", gsonBuilder.create().toJsonTree(map2));
                jsonObject.addProperty("RelyingParty", "http://auth.xboxlive.com");
                jsonObject.addProperty("TokenType", "JWT");

                String xblResponse = postJson("https://user.auth.xboxlive.com/user/authenticate", jsonObject);
                JsonObject xblJson = gsonBuilder.create().fromJson(xblResponse, JsonObject.class);
                String xblToken = xblJson.get("Token").getAsString();
                String xstsResponse = authorizeWithXsts(gsonBuilder, xblToken);

                String xstsToken = new JsonObject().getAsJsonObject("Token").getAsString();
                String xstsUserHash = getXstsUserHash(xstsResponse);

                JsonObject properties = new JsonObject();
                properties.addProperty("identityToken", "XBL3.0 x=" + xstsUserHash + ";" + xstsToken);
                String minecraftAuth = postJson("https://api.minecraftservices.com/authentication/login_with_xbox", properties);
                JsonObject minecraftAuthJson = gsonBuilder.create().fromJson(minecraftAuth, JsonObject.class);
                accessToken = minecraftAuthJson.get("access_token").getAsString();

                // Get profile
                Map<String, String> profileMap = new HashMap<>();
                profileMap.put("Authorization", "Bearer " + accessToken);
                String profile = get("https://api.minecraftservices.com/minecraft/profile", profileMap);
                JsonObject profileJson = gsonBuilder.create().fromJson(profile, JsonObject.class);
                String uuid = profileJson.get("id").getAsString();
                String name = profileJson.get("name").getAsString();

                ProviderManager.mcProvider.setSession(new Session(name, uuid, accessToken, "mojang"));
                GuiWaiting.logged = true;
            });

            httpServer.setExecutor(null);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean login() {
        AtomicBoolean flag = new AtomicBoolean(false);
        try {
            Map<String, String> map = new HashMap<>();
            map.put("client_id", CLIENT_ID);
            map.put("response_type", "code");
            map.put("redirect_uri", "http://127.0.0.1:17342");
            map.put("scope", "XboxLive.signin%20XboxLive.offline_access");

            String url = buildUrl("https://login.live.com/oauth20_authorize.srf", map);
            start();
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag.get();
    }

    private static String postMap(String url, Map<String, String> param) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(paramList));
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
            return "";
        }

    private static String get(String url, Map<String, String> headers) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(35000)
                .setConnectionRequestTimeout(35000)
                .setSocketTimeout(60000)
                .build();
            httpGet.setConfig(requestConfig);
            headers.forEach(httpGet::addHeader);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                return EntityUtils.toString(response.getEntity());
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
            return "";
        }

    private static String postJson(String url, JsonObject jsonObject) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(35000)
                .setConnectionRequestTimeout(35000)
                .setSocketTimeout(60000)
                .build();
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(jsonObject.toString()));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
            return "";
        }

    private static String buildUrl(String url, Map<String, String> map) {
        StringBuilder sb = new StringBuilder(url);
        if (!map.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String authorizeWithXsts(GsonBuilder gsonBuilder, String xblToken) {
        JsonObject jo2 = new JsonObject();
        JsonObject jop = new JsonObject();
        jop.addProperty("SandboxId", "RETAIL");
        jop.add("UserTokens", gsonBuilder.create().toJsonTree(new String[]{xblToken}));
        jo2.add("Properties", jop);
        jo2.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        jo2.addProperty("TokenType", "JWT");

        return postJson("https://xsts.auth.xboxlive.com/xsts/authorize", jo2);
    }

    private static String getXstsUserHash(String xstsResponse) {
        JsonObject xstsJson = new GsonBuilder().create().fromJson(xstsResponse, JsonObject.class);
        return xstsJson.getAsJsonObject("DisplayClaims")
            .getAsJsonArray("xui")
            .get(0)
            .getAsJsonObject()
            .get("uhs")
            .getAsString();
    }
}
