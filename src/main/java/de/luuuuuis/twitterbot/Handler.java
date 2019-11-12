package de.luuuuuis.twitterbot;

import com.google.common.collect.Lists;
import de.luuuuuis.twitterbot.config.Config;
import de.luuuuuis.twitterbot.database.User;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Luuuuuis
 * Project: TwitterBot
 * Package: de.luuuuuis.twitterbot
 * Date: 15.03.2019
 * Time 15:19
 */

class Handler {

    Handler(Twitter twitter) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Long> unfollow = Lists.newArrayList();

                try {
                    long[] ids = twitter.getFollowersIDs(twitter.getScreenName(), -1).getIDs();

                    for (long id : ids) {
                        twitter4j.User user = twitter.showUser(id);
                        //here i am trying to fetch the followers of each id
                        String screenName = user.getScreenName();
                        System.out.println("Username: " + screenName);

                        unfollow.remove(id);
                    }

                    unfollow.forEach(unfollower -> {
                        try {
                            twitter.updateStatus(Config.getInstance().getUnfollow()
                                    .replace("%USER", "@" + twitter.showUser(unfollower).getScreenName())
                                    .replace("%FOLLOWERS", "" + ids.length));
                            Main.getFollowers().remove(unfollower);
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                    });

                    for (long id : ids) {
                        twitter4j.User user = twitter.showUser(id);
                        String screenName = user.getScreenName();

                        if (!Main.getFollowers().contains(id)) {
                            Main.getFollowers().add(id);

                            if (User.userExists(id)) {
                                System.out.println("Comeback follower: " + screenName);
                            } else {
                                User.create(id, ids.length);

                                if (ids.length % 100 == 0) {
                                    twitter.updateStatus(Config.getInstance().getFollow100()
                                            .replace("%USER", "@" + screenName)
                                            .replace("%FOLLOWERS", "" + ids.length));
                                    System.out.println("New follower: " + screenName);
                                    return;
                                }

                                twitter.updateStatus(Config.getInstance().getFollow()
                                        .replace("%USER", "@" + screenName)
                                        .replace("%FOLLOWERS", "" + ids.length));
                                System.out.println("New follower: " + screenName);
                            }
                        }

                    }

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                System.out.println("Total followers: " + Main.getFollowers().size());
            }
        }, 600000, 600000);
    }
}