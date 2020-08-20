/*
 *  Created by SpyderScript on 21.08.2020, 00:15.
 *  Project: Twitter-Bot.
 *  Copyright (c) 2020.
 */

package de.luuuuuis.twitterbot.events;

import twitter4j.User;

public abstract class Event {
    public abstract User getUser();
}
