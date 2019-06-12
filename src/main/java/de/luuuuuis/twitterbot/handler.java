package de.luuuuuis.twitterbot;

import de.luuuuuis.twitterbot.database.User;
import twitter4j.IDs;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Luuuuuis
 * Project: TwitterBot
 * Package: de.luuuuuis.twitterbot
 * Date: 15.03.2019
 * Time 15:19
 */
public class handler {

    public handler() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                ArrayList<Long> unfollow = new ArrayList<>(Twitterbot.followers);

                try {
                    String twitterScreenName = Twitterbot.twitter.getScreenName();


                    IDs followerIDs = Twitterbot.twitter.getFollowersIDs(twitterScreenName, -1);
                    long[] ids = followerIDs.getIDs();


                    for (long id : ids) {
                        twitter4j.User user = Twitterbot.twitter.showUser(id);
                        //here i am trying to fetch the followers of each id
                        String screenName = user.getScreenName();
                        System.out.println("Username: " + screenName);

                        unfollow.remove(id);

                    }

                    unfollow.forEach(unfollower -> {
                        try {
                            Twitterbot.twitter.updateStatus(Twitterbot.config.getUnfollow()
                                    .replace("%USER", "@" + Twitterbot.twitter.showUser(unfollower).getScreenName())
                                    .replace("%FOLLOWERS", "" + ids.length));
                            Twitterbot.followers.remove(unfollower);
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                    });

                    for (long id : ids) {
                        twitter4j.User user = Twitterbot.twitter.showUser(id);
                        String screenName = user.getScreenName();
                        if (!Twitterbot.followers.contains(id)) {
                            Twitterbot.followers.add(id);

                            if (User.userExists(id)) {
                                System.out.println("Comeback follower: " + screenName);
                            } else {
                                User.create(id, ids.length);

                                if (ids.length % 100 == 0) {
                                    Twitterbot.twitter.updateStatus(Twitterbot.config.getFollow100()
                                            .replace("%USER", "@" + screenName)
                                            .replace("%FOLLOWERS", "" + ids.length));
                                    System.out.println("New follower: " + screenName);
                                    return;
                                }

                                Twitterbot.twitter.updateStatus(Twitterbot.config.getFollow()
                                        .replace("%USER", "@" + screenName)
                                        .replace("%FOLLOWERS", "" + ids.length));
                                System.out.println("New follower: " + screenName);
                            }
                        }

                    }

                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                System.out.println("Total followers: " + Twitterbot.followers.size());

            }
        }, 5 * 60 * 1000, 5 * 60 * 1000);
    }
}
