package top.fpsmaster.ui.screens.mainmenu

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.gui.GuiOptions
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import top.fpsmaster.FPSMaster
import top.fpsmaster.interfaces.ProviderManager
import top.fpsmaster.modules.music.MusicPlayer
import top.fpsmaster.ui.screens.account.GuiWaiting
import top.fpsmaster.ui.screens.oobe.GuiLogin
import top.fpsmaster.utils.os.FileUtils
import top.fpsmaster.utils.render.Render2DUtils
import top.fpsmaster.utils.render.ScaledGuiScreen
import top.fpsmaster.wrapper.TextFormattingProvider
import java.awt.Color
import java.awt.Desktop
import java.io.IOException
import java.net.URI

class MainMenu : ScaledGuiScreen() {
    private var singlePlayer: GuiButton = GuiButton("mainmenu.single") {
        ProviderManager.mainmenuProvider.showSinglePlayer(
            this
        )
    }
    private var multiPlayer: GuiButton = GuiButton("mainmenu.multi") {
        mc.displayGuiScreen(
            GuiMultiplayer(
                this
            )
        )
    }
    private var options: GuiButton = GuiButton("mainmenu.settings") {
        mc.displayGuiScreen(
            GuiOptions(
                this, mc.gameSettings
            )
        )
    }

    private var exit: GuiButton = GuiButton("X") { mc.shutdown() }

    private var info: String = "获取版本更新失败"
    private var welcome: String = "获取版本更新失败"
    private var needUpdate: Boolean = false

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    override fun initGui() {
        ProviderManager.mainmenuProvider.initGui()
        if (!MusicPlayer.playList.musics.isEmpty()) {
            if (MusicPlayer.isPlaying) {
                MusicPlayer.playList.pause()
            }
        }
    }

    var textureLocation: ResourceLocation? = null

    /**
     * Draws the screen and all the components in it.
     */
    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
//        if (FPSMaster.configManager.configure.getOrCreate("firstStart", "true") == "true") {
//            Minecraft.getMinecraft().displayGuiScreen(FPSMaster.oobeScreen)
//        }
        if (FileUtils.hasBackground) {
            if (textureLocation == null) {
                textureLocation = ResourceLocation("fpsmaster/gui/background.png")
                val file = FileUtils.background
                val textureManager = Minecraft.getMinecraft().textureManager
                val textureArt = ThreadDownloadImageData(file, null, null, null)
                textureManager.loadTexture(textureLocation, textureArt)
            }
            Render2DUtils.drawImage(textureLocation, 0f, 0f, this.guiWidth.toFloat(), this.guiHeight.toFloat(), -1)
            Render2DUtils.drawRect(
                0f, 0f,
                guiWidth.toFloat(),
                guiHeight.toFloat(), Color(22, 22, 22, 50)
            )
        } else {
            ProviderManager.mainmenuProvider.renderSkybox(
                mouseX,
                mouseY,
                partialTicks,
                this.guiWidth.toInt(),
                this.guiHeight.toInt(),
                this.zLevel
            )
            Render2DUtils.drawRect(
                0f, 0f,
                guiWidth.toFloat(),
                guiHeight.toFloat(), Color(26, 59, 109, 60)
            )
        }



        checkNotNull(FPSMaster.fontManager)
        val stringguiWidth = FPSMaster.fontManager.s16.getStringWidth(mc.session.username)
        if (Render2DUtils.isHovered(10f, 10f, 80f, 20f, mouseX, mouseY)) {
            Render2DUtils.drawOptimizedRoundedRect(10f, 10f, (30 + stringguiWidth).toFloat(), 20f, Color(0, 0, 0, 100))
            if (Mouse.isButtonDown(0)) {
                Minecraft.getMinecraft().displayGuiScreen(GuiWaiting())
            }
        } else {
            Render2DUtils.drawOptimizedRoundedRect(10f, 10f, (30 + stringguiWidth).toFloat(), 20f, Color(0, 0, 0, 60))
        }
        Render2DUtils.drawImage(ResourceLocation("client/gui/screen/avatar.png"), 14f, 15f, 10f, 10f, -1)
        FPSMaster.fontManager.s16.drawString(mc.session.username, 28, 16, Color(255, 255, 255).rgb)
        Render2DUtils.drawImage(
            ResourceLocation("client/gui/logo.png"),
            guiWidth / 2f - 153 / 4f,
            guiHeight / 2f - 100,
            153 / 2f,
            67f,
            -1
        )

        val x = guiWidth / 2 - 50
        val y = guiHeight / 2 - 30

        singlePlayer.render(x.toFloat(), y.toFloat(), 100f, 20f, mouseX.toFloat(), mouseY.toFloat())
        multiPlayer.render(x.toFloat(), (y + 24).toFloat(), 100f, 20f, mouseX.toFloat(), mouseY.toFloat())
        options.render(x.toFloat(), (y + 48).toFloat(), 70f, 20f, mouseX.toFloat(), mouseY.toFloat())
        exit.render((x + 74).toFloat(), (y + 48).toFloat(), 26f, 20f, mouseX.toFloat(), mouseY.toFloat())


        val w = FPSMaster.fontManager.s16.getStringWidth("Copyright Mojang AB. Do not distribute!").toFloat()
        FPSMaster.fontManager.s16.drawString(
            "Copyright Mojang AB. Do not distribute!",
            this.guiWidth - w - 4,
            this.guiHeight - 14,
            Color(255, 255, 255).rgb
        )
        welcome = if (FPSMaster.INSTANCE.loggedIn) {
            TextFormattingProvider.getGreen()
                .toString() + String.format(
                FPSMaster.i18n["mainmenu.welcome"], FPSMaster.configManager.configure.getOrCreate(
                    "username",
                    ""
                )
            )
        } else {
            TextFormattingProvider.getRed()
                .toString() + TextFormattingProvider.getBold()
                .toString() + FPSMaster.i18n["mainmenu.notlogin"]
        }
        FPSMaster.fontManager.s16.drawString(welcome, 4, this.guiHeight.toInt() - 52, Color(255, 255, 255).rgb)

        if (FPSMaster.updateFailed) {
            info = TextFormattingProvider.getGreen().toString() + FPSMaster.i18n["mainmenu.failed"]
        } else {
            if (FPSMaster.isLatest) {
                info = TextFormattingProvider.getGreen().toString() + FPSMaster.i18n["mainmenu.latest"]
            } else {
                info = TextFormattingProvider.getRed().toString() + TextFormattingProvider.getBold()
                    .toString() + String.format(FPSMaster.i18n["mainmenu.notlatest"], FPSMaster.latest)
                needUpdate = true
            }
        }
        FPSMaster.fontManager.s16.drawString(info, 4, this.guiHeight.toInt() - 40, Color(255, 255, 255).rgb)

        Render2DUtils.drawRect(0f, 0f, 0f, 0f, -1)
        FPSMaster.fontManager.s16.drawString(FPSMaster.COPYRIGHT, 4, this.guiHeight.toInt() - 14, Color(255, 255, 255).rgb)
        FPSMaster.fontManager.s16.drawString(
            FPSMaster.CLIENT_NAME + " Client " + FPSMaster.CLIENT_VERSION + " (Minecraft " + FPSMaster.EDITION + ")",
            4,
            this.guiHeight.toInt() - 28,
            Color(255, 255, 255).rgb
        )
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Throws(IOException::class)
    override fun onClick(mouseX: Int, mouseY: Int, mouseButton: Int) {
        singlePlayer.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        multiPlayer.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        multiPlayer.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        options.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        exit.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        checkNotNull(FPSMaster.fontManager)
        val uw = FPSMaster.fontManager.s16.getStringWidth(info).toFloat()
        val nw = FPSMaster.fontManager.s16.getStringWidth(info).toFloat()

        if (mouseButton == 0) {
            if (Render2DUtils.isHovered(4f, (this.guiHeight - 52).toFloat(), nw, 14f, mouseX, mouseY)) {
                Minecraft.getMinecraft().displayGuiScreen(GuiLogin())
            }

            if (Render2DUtils.isHovered(4f, (this.guiHeight - 40).toFloat(), uw, 14f, mouseX, mouseY) && needUpdate) {
                val desktop = Desktop.getDesktop()
                try {
                    desktop.browse(URI("https://fpsmaster.top/download"))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
