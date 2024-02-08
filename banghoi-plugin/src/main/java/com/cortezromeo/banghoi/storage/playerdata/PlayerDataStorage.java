package com.cortezromeo.banghoi.storage.playerdata;

import com.cortezromeo.banghoi.BangHoi;

import java.io.File;

public class PlayerDataStorage {

    private static PlayerStorage STORAGE;

    public static void init() {
        File file = new File(BangHoi.plugin.getDataFolder() + "/playerData/");
        if (!file.exists()) {
            file.mkdirs();
        }
        PlayerDataStorage.STORAGE = new PlayerFileStorage();
    }

    public static PlayerData getplayerData(String player) {
        return PlayerDataStorage.STORAGE.getData(player);
    }

    public static void savePlayerData(String player, PlayerData data) {
        PlayerDataStorage.STORAGE.saveData(player, data);
    }

}
