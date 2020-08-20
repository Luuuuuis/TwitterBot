/*
 *  Created by SpyderScript on 21.08.2020, 00:12.
 *  Project: Twitter-Bot.
 *  Copyright (c) 2020.
 */

package de.luuuuuis.twitterbot.scheduler;

import de.luuuuuis.twitterbot.events.Event;
import de.luuuuuis.twitterbot.events.FollowerEvent;
import de.luuuuuis.twitterbot.events.Listener;
import de.luuuuuis.twitterbot.events.UnFollowerEvent;
import de.luuuuuis.twitterbot.events.handler.FollowerListener;
import org.apache.commons.lang3.ArrayUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FollowerHandler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Twitter api;
    private final long userId;

    private final int initialDelay, period;

    private final List<Listener> listeners = new ArrayList<>();
    private final List<Long> cachedFollowers = new ArrayList<>();

    private ScheduledFuture<?> task;

    public FollowerHandler(Twitter api, long userId, int initialDelay, int period) {
        this.api = api;
        this.userId = userId;
        this.initialDelay = initialDelay;
        this.period = period;
    }

    public void start() {
        if(task == null || !task.isCancelled()) return;
        task = executor.scheduleAtFixedRate(() -> {
            try {
                List<Long> currentFollowers = Arrays.asList(ArrayUtils.toObject(api.getFollowersIDs(userId).getIDs()));

                if(cachedFollowers.isEmpty()) {
                    cachedFollowers.addAll(currentFollowers);
                    return;
                }
                List<Long> newFollowers = new ArrayList<>(currentFollowers);
                newFollowers.removeAll(cachedFollowers);

                newFollowers.forEach(id -> listeners.forEach((listener -> {
                    try {
                        listener.onFollower(new FollowerEvent(api.showUser(id)));
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                })));

                List<Long> unFollowers = new ArrayList<>(cachedFollowers);
                unFollowers.removeAll(currentFollowers);

                unFollowers.forEach(id -> listeners.forEach((listener -> {
                    try {
                        listener.onUnFollower(new UnFollowerEvent(api.showUser(id)));
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                })));

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.MINUTES);
    }

    public void stop() {
        if(task.isCancelled()) return;
        task.cancel(false);
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }
}