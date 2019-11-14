package de.luuuuuis.twitterbot;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.luuuuuis.twitterbot.config.Config;
import lombok.Getter;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

/**
 * Author: Luuuuuis
 * Project: TwitterBot
 * Package: de.luuuuuis.twitterbot
 * Date: 12.03.2019
 * Time 16:25
 */

public class Main {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Getter
    private static final List<Long> followers = Lists.newArrayList();

    @Getter
    private static String screen_name;

    @Getter
    private static Twitter twitter;

    public static void main(String... args) {
        System.out.println("\n\n ______   __     __     __     ______   ______   ______     ______     ______     ______     ______  \n" +
                "/\\__  _\\ /\\ \\  _ \\ \\   /\\ \\   /\\__  _\\ /\\__  _\\ /\\  ___\\   /\\  == \\   /\\  == \\   /\\  __ \\   /\\__  _\\ \n" +
                "\\/_/\\ \\/ \\ \\ \\/ \".\\ \\  \\ \\ \\  \\/_/\\ \\/ \\/_/\\ \\/ \\ \\  __\\   \\ \\  __<   \\ \\  __<   \\ \\ \\/\\ \\  \\/_/\\ \\/ \n" +
                "   \\ \\_\\  \\ \\__/\".~\\_\\  \\ \\_\\    \\ \\_\\    \\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\_____\\    \\ \\_\\ \n" +
                "    \\/_/   \\/_/   \\/_/   \\/_/     \\/_/     \\/_/   \\/_____/   \\/_/ /_/   \\/_____/   \\/_____/     \\/_/ \n\n\n" + "by https://twitter.com/realluuuuuis\n");
        Config.init();
        ConfigurationBuilder cb = new ConfigurationBuilder();

        Config config = Config.getInstance();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(config.getTokens().get("OAuthConsumerKey").toString())
                .setOAuthConsumerSecret(config.getTokens().get("OAuthConsumerSecret").toString())
                .setOAuthAccessToken(config.getTokens().get("OAuthAccessToken").toString())
                .setOAuthAccessTokenSecret(config.getTokens().get("OAuthAccessTokenSecret").toString());

        TwitterFactory factory = new TwitterFactory(cb.build());
        twitter = factory.getInstance();


        try {
            //screen name is @ name without @ ;)
            screen_name = twitter.getScreenName();

            new Handler(twitter);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
