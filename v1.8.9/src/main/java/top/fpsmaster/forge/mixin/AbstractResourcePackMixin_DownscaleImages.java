package top.fpsmaster.forge.mixin;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.AbstractResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Mixin(AbstractResourcePack.class)
public abstract class AbstractResourcePackMixin_DownscaleImages {

    @Shadow
    protected abstract InputStream getInputStreamByName(String name) throws IOException;

    @Inject(method = "getPackImage", at = @At("HEAD"), cancellable = true)
    private void patcher$downscalePackImage(CallbackInfoReturnable<BufferedImage> cir) throws IOException {
        BufferedImage image = TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
        if (image == null) {
            cir.setReturnValue(null);
            return;
        }

        // 检查是否是特殊材质（如附魔效果）
        if (isSpecialTexture(image)) {
            cir.setReturnValue(image);
            return;
        }

        // 如果图片尺寸已经小于等于64x64，直接返回原图
        if (image.getWidth() <= 64 && image.getHeight() <= 64) {
            cir.setReturnValue(image);
            return;
        }

        // 正常缩放其他图片
        BufferedImage downscaledIcon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = downscaledIcon.createGraphics();
        try {
            // 设置更好的渲染质量
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(image, 0, 0, 64, 64, null);
        } finally {
            graphics.dispose();
        }
        cir.setReturnValue(downscaledIcon);
    }

    /**
     * 检查是否为特殊材质（如附魔效果）
     * @param image 要检查的图片
     * @return 如果是特殊材质返回true
     */
    private boolean isSpecialTexture(BufferedImage image) {
        // 检查图片是否具有半透明像素（附魔效果通常有）
        if (hasSemiTransparentPixels(image)) {
            return true;
        }
        
        // 可以添加其他特殊材质的检测条件
        return false;
    }

    /**
     * 检查图片是否包含半透明像素
     * @param image 要检查的图片
     * @return 如果包含半透明像素返回true
     */
    private boolean hasSemiTransparentPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // 只检查部分像素以提高性能
        for (int x = 0; x < width; x += Math.max(1, width / 10)) {
            for (int y = 0; y < height; y += Math.max(1, height / 10)) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                // 如果有半透明像素（既不全透明也不全不透明）
                if (alpha > 0 && alpha < 255) {
                    return true;
                }
            }
        }
        return false;
    }
}
