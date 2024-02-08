package com.cortezromeo.banghoi.storage.playerdata;

import com.cortezromeo.banghoi.BangHoi;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerFileStorage implements PlayerStorage {

    private static File getFile(String player) {
        File file = new File(BangHoi.plugin.getDataFolder() + "/playerData/" + player + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static PlayerData fromFile(File file) {
        YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);
        PlayerData data = new PlayerData(null, null, 0);

        if (!storage.contains("data"))
            return data;

        data.setBangHoi(storage.getString("data.bang_hoi"));
        data.setChucVu(storage.getString("data.chuc_vu"));
        data.setNgayThamGia(storage.getLong("data.ngay_tham_gia"));

        return data;
    }

    @Override
    public void saveData(String player, PlayerData data) {
        saveDataDirect(player, data);
    }

    public static void saveDataDirect(String player, PlayerData data) {
        File file = getFile(player);
        YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);

        storage.set("data.bang_hoi", data.getBangHoi());
        storage.set("data.chuc_vu", data.getChucVu());
        storage.set("data.ngay_tham_gia", data.getNgayThamGia());

        try {
            storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerData getData(String player) {
        File file = getFile(player);
        return fromFile(file);
    }

}
