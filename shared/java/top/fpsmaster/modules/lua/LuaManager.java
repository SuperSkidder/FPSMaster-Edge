package top.fpsmaster.modules.lua;

import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua53.Lua53;
import party.iroiro.luajava.value.LuaValue;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.manager.Module;
import top.fpsmaster.utils.os.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LuaManager {
    public static ArrayList<Lua> scripts = new ArrayList<>();

    public static void main(String[] args) {
        while (true){
            reload();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter to reload");
            scanner.nextLine();
        }
    }


    public void init() {
        File[] luas = FileUtils.INSTANCE.getPlugins().listFiles();
        for (File luaFile : luas) {
            try {
                scripts.add(loadLua(FileUtils.readAbsoluteFile(luaFile.getAbsolutePath())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static Lua loadLua(String script) {
        Lua lua = new Lua53();
        lua.run("System = java.import('java.lang.System')");
        lua.push(L -> {
            String name = L.toString(1);
            String category = L.toString(2);
            Map<String, LuaValue> luaTable = (Map<String, LuaValue>) lua.toMap(3);
            LuaModule module = new LuaModule(name, category, luaTable);
            // 返回 Java 对象给 Lua
            FPSMaster.moduleManager.getModules().add(module);
            L.pushJavaObject(module);
            return 1; // 返回值数量
        });
        lua.setGlobal("registerModule");
        // Client object
        lua.pushJavaObject(FPSMaster.INSTANCE);
        lua.setGlobal("client");

        // Module object
        lua.pushJavaObject(FPSMaster.moduleManager);
        lua.setGlobal("moduleManager");
        lua.pushJavaClass(LuaModule.class);
        lua.setGlobal("module");

        lua.run(script);
        // call load
        LuaValue unload = lua.get("load");
        if (unload.type().equals(Lua.LuaType.FUNCTION)) {
            unload.call();
        }

        return lua;
    }

    public static void reload() {
        ArrayList<Module> remove = new ArrayList<>();
        FPSMaster.moduleManager.getModules().forEach(m -> {
            if (m instanceof LuaModule) {
                remove.add(m);
                if (m.isEnabled())
                    m.toggle();
            }
        });

        for (Lua script : scripts) {
            LuaValue unload = script.get("unload");
            if (unload.type().equals(Lua.LuaType.FUNCTION)) {
                unload.call();
            }
            script.close();
        }
        scripts.clear();

        FPSMaster.moduleManager.getModules().removeAll(remove);

        File[] luas = FileUtils.INSTANCE.getPlugins().listFiles();
        for (File luaFile : luas) {
            try {
                scripts.add(loadLua(FileUtils.readAbsoluteFile(luaFile.getAbsolutePath())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
