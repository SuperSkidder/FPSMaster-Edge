package top.fpsmaster.utils.thirdparty.rawinput;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;

public class RawInputMod {

    private Thread inputThread;

    public void start() {
        try {
            Minecraft.getMinecraft().mouseHelper = new RawMouseHelper();
            controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            inputThread = new Thread(() -> {
                while (true) {
                    int i = 0;
                    while (i < controllers.length && mouse == null) {
                        if (controllers[i].getType() == Controller.Type.MOUSE) {
                            controllers[i].poll();
                            if (((Mouse) controllers[i]).getX().getPollData() != 0.0 || ((Mouse) controllers[i]).getY().getPollData() != 0.0) {
                                mouse = (Mouse) controllers[i];
                            }
                        }
                        i++;
                    }
                    if (mouse != null) {
                        mouse.poll();
                        dx += mouse.getX().getPollData();
                        dy += mouse.getY().getPollData();
                        if (Minecraft.getMinecraft().currentScreen != null) {
                            dx = 0;
                            dy = 0;
                        }
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            inputThread.setName("inputThread");
            inputThread.start();
        } catch (Exception e) {
            // ignored
        }
    }

    public void stop() {
        try {
            if (inputThread.isAlive()) {
                inputThread.interrupt();
            }
            Minecraft.getMinecraft().mouseHelper = new MouseHelper();
        } catch (Exception e) {
            // ignored
        }
    }

    public static Mouse mouse = null;
    public static Controller[] controllers;
    public static int dx = 0;
    public static int dy = 0;
}
