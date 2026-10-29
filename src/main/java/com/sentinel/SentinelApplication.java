package com.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class SentinelApplication {
    public static void main(String[] args) {
        loadDotEnv();
        SpringApplication.run(SentinelApplication.class, args);
    }

    private static void loadDotEnv() {
        File envFile = new File(".env");
        if (envFile.exists() && envFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    int eqIndex = line.indexOf('=');
                    if (eqIndex > 0) {
                        String key = line.substring(0, eqIndex).trim();
                        String value = line.substring(eqIndex + 1).trim();
                        if ((value.startsWith("\"") && value.endsWith("\"")) ||
                            (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }
                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, value);
                        }
                        if ("OPENAI_API_KEY".equals(key)) {
                            System.setProperty("sentinel.openai.api-key", value);
                        }
                        if ("OPENAI_MODEL".equals(key)) {
                            System.setProperty("sentinel.openai.model", value);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
