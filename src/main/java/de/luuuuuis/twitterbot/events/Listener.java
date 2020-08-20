/*
 *  Created by SpyderScript on 21.08.2020, 00:13.
 *  Project: Twitter-Bot.
 *  Copyright (c) 2020.
 */

package de.luuuuuis.twitterbot.events;

public interface Listener {
    void onFollower(FollowerEvent event);
    void onUnFollower(UnFollowerEvent event);
}
