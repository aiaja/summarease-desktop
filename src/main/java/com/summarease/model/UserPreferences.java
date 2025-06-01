package com.summarease.model;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;



public class UserPreferences {
    private static final String CONFIG_PATH = "/com/summarease/config.properties";

    private final Properties properties = new Properties();

    public UserPreferences() {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_PATH)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Konfigurasi tidak ditemukan, menggunakan default.");
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat konfigurasi: " + e.getMessage());
        }
    }

    public String getDefaultMethod() {
        return properties.getProperty("default.method", "rule");
    }

    public String getExportPath() {
        return properties.getProperty("export.path", "./exports");
    }

    public void setDefaultMethod(String method) {
        properties.setProperty("default.method", method);
    }

    public void setExportPath(String path) {
        properties.setProperty("export.path", path);
    }

    public void saveToFile() {
        try {
            Path path = Paths.get("config.properties");
            try (OutputStream output = Files.newOutputStream(path)) {
                properties.store(output, "User Preferences");
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan konfigurasi: " + e.getMessage());
        }
    }
}
