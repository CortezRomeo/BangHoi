package com.cortezromeo.banghoi.storage.banghoidata;

import com.cortezromeo.banghoi.BangHoi;

import java.io.File;

public class BangHoiDataStorage {
    private static BangHoiStorage STORAGE;

    public static void init() {
        File file = new File(BangHoi.plugin.getDataFolder() + "/banghoiData/");
        if (!file.exists()) {
            file.mkdirs();
        }
        BangHoiDataStorage.STORAGE = new BangHoiFileStorage();
    }

    public static BangHoiData getBangHoiData(String bangHoi) {
        return BangHoiDataStorage.STORAGE.getData(bangHoi);
    }

    public static void saveBangHoiData(String bangHoi, BangHoiData data) {
        BangHoiDataStorage.STORAGE.saveData(bangHoi, data);
    }
}
