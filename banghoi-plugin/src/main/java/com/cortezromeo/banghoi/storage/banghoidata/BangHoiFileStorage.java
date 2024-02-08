package com.cortezromeo.banghoi.storage.banghoidata;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.util.FilenameUtil;
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

        BangHoiData data = new BangHoiData(FilenameUtil.removeExtension(file.getName()).toString()
                , null
                , null
                , 0, 0, 0, 0, 0, members, null
                , 0, 0, 0, 0);

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
        data.setBangHoiWarn(storage.getInt("data.warn"));
        data.setWarPoint(storage.getInt("data.warpoint"));

        if (storage.getString("data.banghoiicon") != null)
            data.setBangHoiIcon(storage.getString("data.banghoiicon"));
        else
            data.setBangHoiIcon(null);

        for (int i = 1; i <= 4; i++)
            data.setSkillLevel(i, storage.getInt("data.skill." + i));

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
        storage.set("data.warn", data.getBangHoiWarn());
        storage.set("data.warpoint", data.getBangHoiWarPoint());
        storage.set("data.banghoiicon", data.getBangHoiIcon());

        for (int i = 1; i <= 4; i++)
            storage.set("data.skill." + i, data.getSkillLevel(i));
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
