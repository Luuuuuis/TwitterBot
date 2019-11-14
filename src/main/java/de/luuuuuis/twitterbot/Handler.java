package de.luuuuuis.twitterbot;

import com.google.common.collect.Lists;
import de.luuuuuis.twitterbot.config.Config;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Luuuuuis
 * Project: TwitterBot
 * Package: de.luuuuuis.twitterbot
 * Date: 15.03.2019
 * Time 15:19
 */

class Handler {

    Handler(Twitter twitter) {

        System.out.println("Handler initialized");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.println("-----------------------------------");

                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                System.out.println(simpleDateFormat.format(date) + " UTC+1");

                try {
                    long[] ids = twitter.getFollowersIDs(Main.getScreen_name(), -1).getIDs();

                    if (Main.getFollowers().isEmpty()) {

                        for (long id : ids) {
                            Main.getFollowers().add(id);
                            System.out.println("Follower ID: " + id);
                        }

                    } else {

                        // add all followers to list to see who unfollows
                        List<Long> unfollowers = Lists.newArrayList();
                        unfollowers.addAll(Main.getFollowers());

                        for (long id : ids) {
                            //remove to see who remains
                            unfollowers.remove(id);

                            if (!Main.getFollowers().contains(id)) {
                                Main.getFollowers().add(id);

                                twitter4j.User user = twitter.showUser(id);
                                String screenName = user.getScreenName();

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

                            System.out.println("Follower ID: " + id);

                        }

                        //these who remain in unfollowers
                        unfollowers.forEach(unfollower -> {
                            try {
                                twitter.updateStatus(Config.getInstance().getUnfollow()
                                        .replace("%USER", "@" + twitter.showUser(unfollower).getScreenName())
                                        .replace("%FOLLOWERS", "" + ids.length));
                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }
                            Main.getFollowers().remove(unfollower);
                        });


                    }


                } catch (TwitterException e) {
                    e.printStackTrace();
                }


                System.out.println("Total followers: " + Main.getFollowers().size());


            }
        }, 0, 600000);
    }
}