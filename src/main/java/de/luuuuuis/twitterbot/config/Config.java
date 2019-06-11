package de.luuuuuis.twitterbot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private HashMap<String, Object> Keys = new HashMap<>();
    private String follow;
    private String follow100;
    private String unfollow;

    public Config() {
        query();
    }


    private void query() {
        Thread thread = new Thread(() -> {

            try {

                File f = new File(System.getProperty("java.class.path"));
                File dir = f.getAbsoluteFile().getParentFile();
                String currentFile = dir.toString() + "/";

                System.out.println("Current File: " + currentFile);

                File file = new File(currentFile + "config.json");


                if (!file.exists()) {

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try (FileWriter fileWriter = new FileWriter(currentFile + "config.json")) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("follow", "%USER follows now! We are now %FOLLOWERS!");
                        jsonObject.put("follow100", "%USER follows now! We reached %FOLLOWERS!");
                        jsonObject.put("unfollow", "%USER unfollowed! :( Only %FOLLOWERS followers left.");

                        JSONObject Tokens = new JSONObject();
                        Tokens.put("OAuthConsumerKey", "123456789");
                        Tokens.put("OAuthConsumerSecret", "123456789");
                        Tokens.put("OAuthAccessToken", "123456789");
                        Tokens.put("OAuthAccessTokenSecret", "123456789");
                        jsonObject.put("tokens", Tokens);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        fileWriter.write(gson.toJson(jsonObject));
                        fileWriter.flush();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }


                Object object = new JSONParser().parse(new FileReader(currentFile + "config.json"));
                JSONObject jsonObject = (JSONObject) object;

                follow = jsonObject.get("follow").toString();
                follow100 = jsonObject.get("follow100").toString();
                unfollow = jsonObject.get("unfollow").toString();

                Map TokensJSON = (Map) jsonObject.get("tokens");
                for (Object o : TokensJSON.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    Keys.put(pair.getKey().toString(), pair.getValue());
                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getKeys() {
        return Keys;
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
}
