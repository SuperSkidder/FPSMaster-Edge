package top.fpsmaster.features.command.impl;

import com.google.gson.JsonArray;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.command.Command;
import top.fpsmaster.modules.client.AsyncTask;
import top.fpsmaster.utils.Utility;
import top.fpsmaster.utils.thirdparty.openai.OpenAI;
import top.fpsmaster.utils.thirdparty.openai.OpenAIClient;

import java.io.IOException;

public class AI extends Command {
    public AI() {
        super("ai");
    }

    @Override
    public void execute(String[] args) {
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            for (String arg : args) {
                sb.append(arg);
            }
            FPSMaster.async.execute(() -> {
                OpenAIClient.Message[] messages = {new OpenAIClient.Message("system", "your name is Ares"), new OpenAIClient.Message("user", sb.toString())};
                Utility.sendClientNotify("Fetching AI response...");
                try {
                    String response = OpenAIClient.getChatResponse(messages);
                    Utility.sendClientNotify("[AI] " + response);
                } catch (IOException e) {
                    Utility.sendClientNotify("[AI] failed to fetch AI response.");
                }
                return null;
            });
        }
    }
}
