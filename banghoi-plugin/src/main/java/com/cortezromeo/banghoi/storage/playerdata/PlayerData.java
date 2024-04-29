package com.cortezromeo.banghoi.storage.playerdata;

import com.cortezromeo.banghoi.enums.ClanRank;

public class PlayerData {

    private String bangHoi;
    private ClanRank rank;
    private long joinDate;

    public PlayerData(String bangHoi, ClanRank rank, long joinDate) {
        this.bangHoi = bangHoi;
        this.rank = rank;
        this.joinDate = joinDate;
    }

    public String getBangHoi() {
        return bangHoi;
    }

    public void setBangHoi(String bangHoi) {
        this.bangHoi = bangHoi;
    }

    public ClanRank getChucVu() {
        return rank;
    }

    public void setChucVu(ClanRank rank) {
        this.rank = rank;
    }

    public long getNgayThamGia() {
        return joinDate;
    }

    public void setNgayThamGia(long joinDate) {

        if (joinDate < 0) {
            this.joinDate = 0;
            return;
        }

        this.joinDate = joinDate;
    }

}
