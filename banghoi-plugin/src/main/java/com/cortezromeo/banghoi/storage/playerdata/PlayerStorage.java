package com.cortezromeo.banghoi.storage.playerdata;

public interface PlayerStorage {
    PlayerData getData(String p0);

    void saveData(String p0, PlayerData p1);
}
