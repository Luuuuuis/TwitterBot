/*
 *  Created by SpyderScript on 20.08.2020, 22:15.
 *  Project: Twitter-Bot.
 *  Copyright (c) 2020.
 */

package de.luuuuuis.twitterbot;

import de.luuuuuis.twitterbot.config.Config;
import de.luuuuuis.twitterbot.events.handler.FollowerListener;
import de.luuuuuis.twitterbot.scheduler.FollowerHandler;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApp {
    private Twitter api;
    public TwitterApp() {

    }

    public void init() throws TwitterException {
        Config config = Config.getInstance();

        Configuration configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(config.getTokens().get("OAuthConsumerKey").toString())
                .setOAuthConsumerSecret(config.getTokens().get("OAuthConsumerSecret").toString())
                .setOAuthAccessToken(config.getTokens().get("OAuthAccessToken").toString())
                .setOAuthAccessTokenSecret(config.getTokens().get("OAuthAccessTokenSecret").toString())
                .build();

        TwitterFactory factory = new TwitterFactory(configuration);
        api = factory.getInstance();

        long userId = api.getId();
        FollowerHandler handler = new FollowerHandler(api, userId, 0, 10);

        handler.registerListener(new FollowerListener());
        handler.start();
    }
}
