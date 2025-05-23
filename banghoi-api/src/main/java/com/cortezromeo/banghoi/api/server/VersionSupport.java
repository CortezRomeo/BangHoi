package com.cortezromeo.banghoi.api.server;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class VersionSupport {
    private final Plugin plugin;

    public VersionSupport(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public abstract ItemStack createItemStack(String material, int amount, short data);

    public abstract ItemStack createItemStack(String material, int amount);

    public abstract Sound playSound(String soundName);

    public abstract ItemStack getHeadItemFromPlayerName(String playerName);

    public abstract ItemStack getHeadItemFromBase64(String headValue);

    public abstract ItemStack addCustomData(ItemStack i, String data);

    public abstract String getCustomData(ItemStack i);

    public abstract String addColor(String textToTranslate);

    public abstract String stripColor(String textToStrip);

}
