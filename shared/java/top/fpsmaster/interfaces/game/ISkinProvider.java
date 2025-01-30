
package top.fpsmaster.interfaces.game;

import top.fpsmaster.interfaces.IProvider;

public interface ISkinProvider extends IProvider {
    void updateSkin(String name, String uuid, String skin);
}