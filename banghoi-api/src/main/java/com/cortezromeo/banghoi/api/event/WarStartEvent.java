package com.cortezromeo.banghoi.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final int time;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public WarStartEvent(int time) {
        this.time = time;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public int getTime() {
        return this.time;
    }

}
