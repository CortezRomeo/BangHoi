package com.cortezromeo.banghoi.storage.banghoidata;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.util.FilenameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BangHoiFileStorage implements BangHoiStorage {
    private static File getFile(String bangHoi) {
        File file = new File(BangHoi.plugin.getDataFolder() + "/banghoiData/" + bangHoi + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static BangHoiData fromFile(File file) {
        YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);

        List<String> members = new ArrayList<>();
        List<String> managers = new ArrayList<>();

        BangHoiData data = new BangHoiData(FilenameUtil.removeExtension(file.getName()).toString()
                , null
                , null
                , 0, 0, 0, 0, 0, members, managers, null
                , null, 0, 0, 0, 0);

        if (!storage.contains("data"))
            return data;

        data.setBangHoiName(storage.getString("data.ten"));
        data.setTenCustom(storage.getString("data.ten_custom"));
        data.setBangHoiFounder(storage.getString("data.leader"));
        data.setBangHoiScore(storage.getInt("data.diem"));
        data.setNgayThanhLap(storage.getLong("data.ngay_thanh_lap"));
        data.setSoLuongToiDa(storage.getInt("data.thanh_vien_toi_da"));
        for (String key : storage.getStringList("data.thanh_vien"))
            data.addThanhVien(key);
        if (storage.getString("data.managers") != null) {
            for (String key : storage.getStringList("data.managers"))
                data.addManager(key);
        }
        data.setBangHoiWarn(storage.getInt("data.warn"));
        data.setBangHoiWarPoint(storage.getInt("data.warpoint"));

        if (storage.getString("data.banghoiicon") != null)
            data.setBangHoiIcon(storage.getString("data.banghoiicon"));
        else
            data.setBangHoiIcon(null);

        String spawnWorld = storage.getString("data.spawn.world");
        if (spawnWorld != null) {
            Location location = new Location(Bukkit.getWorld(spawnWorld), storage.getDouble("data.spawn.x"), storage.getDouble("data.spawn.y"), storage.getDouble("data.spawn.z"));
            data.setBangHoiSpawn(location);
        }

        data.setSkillLevel(SkillType.critDamage, storage.getInt("data.skill.1"));
        data.setSkillLevel(SkillType.boostScore, storage.getInt("data.skill.2"));
        data.setSkillLevel(SkillType.dodge, storage.getInt("data.skill.3"));
        data.setSkillLevel(SkillType.vampire, storage.getInt("data.skill.4"));

        return data;
    }

    @Override
    public void saveData(String bangHoi, BangHoiData data) {
        saveDataDirect(bangHoi, data);
    }

    public static void saveDataDirect(String banghoi, BangHoiData data) {
        File file = getFile(banghoi);
        YamlConfiguration storage = YamlConfiguration.loadConfiguration(file);

        storage.set("data.ten", data.getBangHoiName());
        storage.set("data.ten_custom", data.getTenCustom());
        storage.set("data.leader", data.getBangHoiFounder());
        storage.set("data.diem", data.getBangHoiScore());
        storage.set("data.ngay_thanh_lap", data.getNgayThanhLap());
        storage.set("data.thanh_vien_toi_da", data.getSoLuongToiDa());
        storage.set("data.thanh_vien", data.getThanhVien());
        storage.set("data.managers", data.getManagers());
        storage.set("data.warn", data.getBangHoiWarn());
        storage.set("data.warpoint", data.getBangHoiWarPoint());
        storage.set("data.banghoiicon", data.getBangHoiIcon());
        if (data.getBangHoiSpawn() != null) {
            storage.set("data.spawn.world", data.getBangHoiSpawn().getWorld().getName());
            storage.set("data.spawn.x", data.getBangHoiSpawn().getX());
            storage.set("data.spawn.y", data.getBangHoiSpawn().getY());
            storage.set("data.spawn.z", data.getBangHoiSpawn().getZ());
        }

        storage.set("data.skill.1", data.getSkillLevel(SkillType.critDamage));
        storage.set("data.skill.2", data.getSkillLevel(SkillType.boostScore));
        storage.set("data.skill.3", data.getSkillLevel(SkillType.dodge));
        storage.set("data.skill.4", data.getSkillLevel(SkillType.vampire));

        try {
            storage.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BangHoiData getData(String bangHoi) {
        File file = getFile(bangHoi);
        return fromFile(file);
    }
}
