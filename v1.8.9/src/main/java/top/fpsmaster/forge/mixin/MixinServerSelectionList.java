package top.fpsmaster.forge.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.gui.ServerSelectionListEntryNormal;
import net.minecraft.client.network.ServerData;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.class)
public class MixinServerSelectionList {

    @Shadow
    private List<ServerData> serverList;

    @Shadow
    private int selectedIndex;

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.buttonWidth = 100;
        this.buttonHeight = 20;
    }

    @Inject(method = "renderServer", at = @At("HEAD"))
    private void renderServer(ServerData serverData, int index, int width, int height, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (mouseX >= this.left && mouseX <= this.right && mouseY >= this.top + index * height && mouseY <= this.top + index * height + height) {
            String tooltip = I18n.format("multiplayer.server.tooltip", serverData.serverName);
            if (serverData.ping <= 0) {
                tooltip += "\n" + I18n.format("multiplayer.server.ping", I18n.format("multiplayer.ping.offline"));
            } else {
                tooltip += "\n" + I18n.format("multiplayer.server.ping", serverData.ping + "ms");
            }
            drawHoveringText(tooltip, mouseX, mouseY);
        }
    }
}