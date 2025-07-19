package top.fpsmaster.modules.account;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.utils.os.HttpRequest;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static top.fpsmaster.utils.Utility.mc;

public class Cosmetic {
    public int id;
    public String name;
    public String img;
    public String category;
    public double price;
    public boolean available;
    public String resource;
    public boolean loaded;

    public Cosmetic() {
    }

    public void load() {
        if (mc.theWorld == null) return;
        FPSMaster.async.runnable(() -> {
            ResourceLocation textureLocation = new ResourceLocation("ornaments/" + id + "_resource");
            ThreadDownloadImageData downloadImageData = new ThreadDownloadImageData(null, resource, textureLocation, null);
            try {
                downloadImageData.setBufferedImage(HttpRequest.downloadImage(resource));
                mc.getTextureManager().loadTexture(textureLocation, downloadImageData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loaded = true;
    }
}
