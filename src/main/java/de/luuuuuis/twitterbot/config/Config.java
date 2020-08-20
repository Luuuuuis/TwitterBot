package de.luuuuuis.twitterbot.config;

import com.google.common.collect.Maps;
import de.luuuuuis.twitterbot.Main;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class Config {

    private static Config instance;

    private final String follow;
    private final String follow100;
    private final String unfollow;
    private final Map<String, Object> tokens = Maps.newHashMap();

    public Config(String follow, String follow100, String unfollow) {
        this.follow = follow;
        this.follow100 = follow100;
        this.unfollow = unfollow;
    }

    public static void init() {
        try {
            String path = System.getProperty("user.dir") + "/config.json";
            if (Files.notExists(Paths.get(path))) {
                InputStream in = Main.class.getClassLoader().getResourceAsStream("config.json");
                Files.copy(Objects.requireNonNull(in), Paths.get(path));
                instance = Main.GSON.fromJson(new InputStreamReader(in), Config.class);
            } else {
                instance = Main.GSON.fromJson(new FileReader(path), Config.class);
            }
        } catch (IOException e) {
            instance = new Config("Follow-Default", "Follow-100-Default", "Unfollow-Default");
            e.printStackTrace();
        }
        System.out.println("Config initialized");
    }

    public static Config getInstance() {
        return instance;
    }

    public String getFollow() {
        return follow;
    }

    public String getFollow100() {
        return follow100;
    }

    public String getUnfollow() {
        return unfollow;
    }

    public Map<String, Object> getTokens() {
        return tokens;
    }
}
