/*
 *  Created by SpyderScript on 21.08.2020, 00:16.
 *  Project: Twitter-Bot.
 *  Copyright (c) 2020.
 */

package de.luuuuuis.twitterbot.events;

import twitter4j.User;

public class UnFollowerEvent extends Event {

    private final User user;

    public UnFollowerEvent(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
