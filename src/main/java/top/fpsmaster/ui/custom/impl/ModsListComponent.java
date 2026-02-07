package top.fpsmaster.ui.custom.impl;

import top.fpsmaster.utils.render.draw.Rects;

import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.impl.interfaces.ModsList;
import top.fpsmaster.features.manager.Category;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.font.impl.UFontRenderer;
import net.minecraft.client.Minecraft;
import top.fpsmaster.ui.custom.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModsListComponent extends Component {

    List<Module> modules = new ArrayList<>();
    //default background rectangle height
    public static final float MODULE_HEIGHT = 14f;
    public ModsListComponent() {
        super(ModsList.class);
        this.x = 1f;
        this.allowScale = true;
    }

    @Override
    public void draw(float x, float y) {
        super.draw(x, y);
        UFontRenderer font = FPSMaster.fontManager.s18;
        float modY = 0f;

        ModsList modlist = (ModsList) mod;
        if (modlist.showLogo.getValue()) {
            float stringWidth = getStringWidth(36, modlist.text.getValue());
            drawString(36, modlist.text.getValue(), (float) (x +(0.5 + width - stringWidth)*scale), y + 0.5f, new Color(0, 0, 0, 100).getRGB());
            drawString(36, modlist.text.getValue(), x + (width - stringWidth) * scale, y, new Color(113, 127, 254).getRGB());
            modY = 18f * scale;
        }

        float maxWidth = 40f;
        x += this.width * scale;
        if (Minecraft.getMinecraft().thePlayer.ticksExisted % 20 == 0)
            modules = FPSMaster.moduleManager.modules.stream()
                    .sorted((m1, m2) -> {
                        float w1 = (mod).betterFont.getValue()
                                ? font.getStringWidth(modlist.english.getValue() ? m1.name : FPSMaster.i18n.get(m1.name.toLowerCase()))
                                : Minecraft.getMinecraft().fontRendererObj.getStringWidth(modlist.english.getValue() ? m1.name : FPSMaster.i18n.get(m1.name.toLowerCase()));
                        float w2 = (mod.betterFont.getValue()
                                ? font.getStringWidth(modlist.english.getValue() ? m2.name : FPSMaster.i18n.get(m2.name.toLowerCase()))
                                : Minecraft.getMinecraft().fontRendererObj.getStringWidth(modlist.english.getValue() ? m2.name : FPSMaster.i18n.get(m2.name.toLowerCase())));
                        return Float.compare(w2, w1);
                    }).collect(Collectors.toList());

        int index = 0;
        for (Module module : modules) {
            Color textColor = Color.getHSBColor(
                    index / (float) modules.size() - Minecraft.getMinecraft().thePlayer.ticksExisted % 50 / 50f,
                    0.7f,
                    1f
            );
            if (!module.isEnabled() || module.category == Category.Interface) {
                continue;
            }

            String name = FPSMaster.i18n.get(module.name.toLowerCase());
            if (modlist.english.getValue()) {
                name = module.name;
            }

            float textWidth = getStringWidth(18,name);

            if (maxWidth < textWidth) {
                maxWidth = textWidth + 5;
            }
            if(modlist.bg.getValue()) {
                drawRect(x - textWidth - 4, y + modY, textWidth + 4, MODULE_HEIGHT + modlist.spacing.getValue().intValue() , modlist.backgroundColor.getColor());
            }
            Color color = modlist.color.getColor();
            if (modlist.rainbow.getValue()) {
                color = textColor;
            }
            //text y position centered offset
            int yOffset;
            yOffset = (int) ((- getStringHeight(18)) / 2);
            drawString(18,name, x - (textWidth + 2)*scale, y + modY + yOffset, color.getRGB());
            index++;
            modY += (getStringHeight(18)+ modlist.spacing.getValue().intValue()) * scale;
        }

        this.width = maxWidth;
        height = modY;
    }
}




