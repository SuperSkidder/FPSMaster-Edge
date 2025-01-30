package top.fpsmaster.interfaces.client;

import top.fpsmaster.interfaces.IProvider;

public interface IConstantsProvider extends IProvider {
    String getVersion();
    String getEdition();
}
