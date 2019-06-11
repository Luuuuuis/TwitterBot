package de.luuuuuis.twitterbot;

import de.luuuuuis.twitterbot.config.Config;
import de.luuuuuis.twitterbot.database.DBManager;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

/**
 * Author: Luuuuuis
 * Project: TwitterBot
 * Package: de.luuuuuis.twitterbot
 * Date: 12.03.2019
 * Time 16:25
 */
public class Twitterbot {

    public static ArrayList<Long> followers = new ArrayList<>();

    public static Twitter twitter;
    public static Config config;

    public static void main(String[] args) {

        DBManager.init();
        config = new Config();

        System.out.println("Your Keys:" +
                "\nOAuthConsumerKey: " + config.getKeys().get("OAuthConsumerKey").toString() +
                "\nOAuthConsumerSecret: " + config.getKeys().get("OAuthConsumerSecret").toString() +
                "\nOAuthAccessToken: " + config.getKeys().get("OAuthAccessToken").toString() +
                "\nOAuthAccessTokenSecret: " + config.getKeys().get("OAuthAccessTokenSecret").toString());

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(config.getKeys().get("OAuthConsumerKey").toString())
                .setOAuthConsumerSecret(config.getKeys().get("OAuthConsumerSecret").toString())
                .setOAuthAccessToken(config.getKeys().get("OAuthAccessToken").toString())
                .setOAuthAccessTokenSecret(config.getKeys().get("OAuthAccessTokenSecret").toString());
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

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

            new handler();

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
