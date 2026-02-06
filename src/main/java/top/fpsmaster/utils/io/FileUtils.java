package top.fpsmaster.utils.io;

import top.fpsmaster.FPSMaster;
import top.fpsmaster.exception.FileException;
import top.fpsmaster.modules.logger.ClientLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtils {

    public static File fpsmasterCache;
    public static File netease;
    public static File dir;
    public static File cache;
    public static File music;
    public static File artists;
    public static File omaments;
    public static File background;
    public static boolean hasBackground = false;
    public static File fonts;

    private static boolean initialized;

    public static synchronized void init(File dataDir) {
        if (initialized) {
            return;
        }
        if (dataDir == null) {
            throw new IllegalStateException("FileUtils init requires a valid mcDataDir");
        }
        cache = ensureDir(new File(dataDir, ".cache"));
        dir = ensureDir(new File(dataDir, "FPSMaster " + FPSMaster.EDITION));
        fonts = ensureDir(new File(dir, "fonts"));

        fpsmasterCache = ensureDir(new File(cache, "FPSMasterClient"));
        netease = ensureDir(new File(cache, "netease"));
        music = ensureDir(new File(netease, "songs"));
        artists = ensureDir(new File(netease, "artists"));
        omaments = ensureDir(new File(cache, "omaments"));
        background = new File(dir, "background.png");
        hasBackground = background.exists();
        initialized = true;
    }

    private static File ensureDir(File file) {
        if (!file.exists() && !file.mkdirs()) {
            ClientLogger.error("FileUtils", "failed to create directory: " + file.getAbsolutePath());
        }
        return file;
    }

    public static void saveFileBytes(String s, byte[] bytes) throws FileException {
        File file = new File(dir, s);
        writeBytes(file, bytes);
    }

    public static void saveFile(String name, String content) throws FileException {
        writeString(new File(dir, name), content);
    }

    private static void saveAbsoluteFile(String name, String content) throws FileException {
        writeString(new File(name), content);
    }

    public static void saveTempValue(String name, String value) throws FileException {
        File file = new File(fpsmasterCache, name + ".tmp");
        writeString(file, value);
    }


    public static void release(String file) {
        ClientLogger.info("release " + file);
        try {
            releaseResource("/assets/minecraft/client/lang/" + file + ".lang", new File(dir, file + ".lang"));
        } catch (IOException e) {
            ClientLogger.error("An error occurred while releasing language file: " + file + ".lang");
            e.printStackTrace();
        }
    }

    public static void releaseFont(String fileName) {
        try {
            releaseResource("/assets/minecraft/client/fonts/" + fileName, new File(fonts, fileName));
        } catch (IOException e) {
            ClientLogger.error("Failed to release font: " + fileName);
            e.printStackTrace();
        }
    }

    public static int getDirSize(File folder) {
        long size = 0;
        File[] fileList = folder.listFiles();
        if (fileList == null) return 0;
        for (File file : fileList) {
            size += file.isDirectory() ? getDirSize(file) : file.length();
        }
        return (int) (size / 1024 / 1024);
    }

    public static String readFile(String name) throws FileException {
        return readString(new File(dir, name));
    }

    public static String readAbsoluteFile(String name) throws FileException {
        try {
            return readString(new File(name));
        } catch (FileException e) {
            throw e;
        }
    }

    public static String fixName(String s) {
        return s.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    private static void writeBytes(File file, byte[] bytes) throws FileException {
        ensureParent(file);
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new FileException("Failed to create file: " + file.getAbsolutePath());
            }
            try (FileOutputStream fOut = new FileOutputStream(file)) {
                fOut.write(bytes);
                fOut.flush();
            }
        } catch (IOException e) {
            throw new FileException("Failed to save file bytes: " + file.getAbsolutePath(), e);
        }
    }

    private static void writeString(File file, String content) throws FileException {
        ensureParent(file);
        try {
            if (!file.exists() && !file.createNewFile()) {
                ClientLogger.error("FileUtils", "failed to create " + file.getAbsolutePath());
                throw new FileException("Failed to create file: " + file.getAbsolutePath());
            }
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
                bw.write(content);
                bw.flush();
            }
        } catch (IOException e) {
            throw new FileException("Failed to save file: " + file.getAbsolutePath(), e);
        }
    }

    private static String readString(File file) throws FileException {
        ensureParent(file);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new FileException("Failed to create file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new FileException("Failed to create file: " + file.getAbsolutePath(), e);
            }
        }
        StringBuilder result = new StringBuilder();
        try (FileInputStream fIn = new FileInputStream(file);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn, StandardCharsets.UTF_8))) {
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                result.append(str).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FileException("Failed to read file: " + file.getAbsolutePath(), e);
        }
        return result.toString();
    }

    private static void ensureParent(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            ClientLogger.error("FileUtils", "failed to create directory: " + parent.getAbsolutePath());
        }
    }

    private static void releaseResource(String resourcePath, File target) throws IOException {
        if (target.exists()) {
            return;
        }
        try (InputStream resourceAsStream = FileUtils.class.getResourceAsStream(resourcePath)) {
            if (resourceAsStream == null) {
                ClientLogger.error("Resource not found: " + resourcePath);
                return;
            }
            ensureParent(target);
            Files.copy(resourceAsStream, target.toPath());
            ClientLogger.info("Released resource: " + target.getName());
        }
    }
}



