package com.cortezromeo.banghoi.storage.banghoidata;

public interface BangHoiStorage {
    BangHoiData getData(String p0);

    void saveData(String p0, BangHoiData p1);
}
