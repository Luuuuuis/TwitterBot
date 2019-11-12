package de.luuuuuis.twitterbot;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.luuuuuis.twitterbot.config.Config;
import de.luuuuuis.twitterbot.database.DBManager;
import lombok.Getter;
import twitter4j.IDs;
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
    private static Twitter twitter;

    public static void main(String... args) {
        DBManager.init();
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
            String twitterScreenName = twitter.getScreenName();

            IDs followerIDs = twitter.getFollowersIDs(twitterScreenName, -1);
            long[] ids = followerIDs.getIDs();
            for (long id : ids) {
                twitter4j.User user = twitter.showUser(id);
                //here i am trying to fetch the followers of each id
                System.out.println("Username: " + user.getScreenName());

                if (!followers.contains(id)) {
                    followers.add(id);
                }
            }

            System.out.println("Total followers: " + followers.size());

            new Handler(twitter);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
