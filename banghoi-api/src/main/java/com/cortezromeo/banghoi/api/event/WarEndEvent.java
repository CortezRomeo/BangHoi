package com.cortezromeo.banghoi.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class WarEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private HashMap<String, Integer> topBangHoi = new HashMap<>();
    private HashMap<String, Integer> playerKills = new HashMap<>();
    private HashMap<String, Double> playerDmgs = new HashMap<>();
    private HashMap<String, Integer> playerDies = new HashMap<>();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public WarEndEvent(HashMap<String, Integer> topBangHoi, HashMap<String, Integer> playerKills, HashMap<String, Double> playerDmgs, HashMap<String, Integer> playerDies) {
        this.topBangHoi = topBangHoi;
        this.playerKills = playerKills;
        this.playerDmgs = playerDmgs;
        this.playerDies = playerDies;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public HashMap<String, Integer> getTopBangHoi() {
        return this.topBangHoi;
    }

    public HashMap<String, Integer> getPlayerKills() {
        return this.playerKills;
    }

    public HashMap<String, Double> getPlayerDmgs() {
        return this.playerDmgs;
    }

    public HashMap<String, Integer> getPlayerDies() {
        return this.playerDies;
    }
}
