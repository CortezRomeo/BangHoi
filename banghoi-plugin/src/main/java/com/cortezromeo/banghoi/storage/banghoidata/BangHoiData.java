package com.cortezromeo.banghoi.storage.banghoidata;

import com.cortezromeo.banghoi.manager.DatabaseManager;

import java.util.List;

public class BangHoiData {

    private String bangHoiName;
    private String bangHoiCustomName;
    private String bangHoiFounder;
    private int bangHoiScore;
    private int bangHoiWarPoint;
    private int bangHoiWarn;
    private int bangHoiMaxMember;
    private long bangHoiCreatedDate;
    private String bangHoiIcon;
    private List<String> bangHoiMember;
    private int skill1;
    private int skill2;
    private int skill3;
    private int skill4;

    public BangHoiData(String bangHoiName, String bangHoiCustomName, String bangHoiFounder, int bangHoiScore, int bangHoiWarPoint, int bangHoiWarn,
                       int bangHoiMaxMember, long bangHoiCreatedDate, List<String> bangHoiMember, String bangHoiIcon, int skill1, int skill2, int skill3, int skill4) {

        this.bangHoiName = bangHoiName;
        this.bangHoiCustomName = bangHoiCustomName;
        this.bangHoiFounder = bangHoiFounder;
        this.bangHoiScore = bangHoiScore;
        this.bangHoiWarPoint = bangHoiWarPoint;
        this.bangHoiMaxMember = bangHoiMaxMember;
        this.bangHoiWarn = bangHoiWarn;
        this.bangHoiCreatedDate = bangHoiCreatedDate;
        this.bangHoiMember = bangHoiMember;
        this.bangHoiIcon = bangHoiIcon;

        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
        this.skill4 = skill4;

    }

    public String getBangHoiName() {
        return bangHoiName;
    }

    public void setBangHoiName(String bangHoiName) {
        this.bangHoiName = bangHoiName;
    }

    public String getTenCustom() {
        return bangHoiCustomName;
    }

    public void setTenCustom(String ten_custom) {
        this.bangHoiCustomName = ten_custom;
        DatabaseManager.bangHoi_customName.put(bangHoiName, bangHoiCustomName);
    }

    public String getBangHoiIcon() {
        return bangHoiIcon;
    }

    public void setBangHoiIcon(String material) {
        this.bangHoiIcon = material;
    }

    public String getBangHoiFounder() {
        return bangHoiFounder;
    }

    public void setBangHoiFounder(String bangHoiFounder) {
        this.bangHoiFounder = bangHoiFounder;
    }

    public int getBangHoiScore() {
        return bangHoiScore;
    }

    public void addBangHoiScore(int diem) {

        if (diem < 0 && this.bangHoiScore + diem < 0) {
            this.bangHoiScore = 0;
            return;
        }

        DatabaseManager.bangHoi_diem.put(getBangHoiName(), this.bangHoiScore + diem);
        this.bangHoiScore = this.bangHoiScore + diem;
    }

    public void removeBangHoiScore(int diem) {

        if (this.bangHoiScore - diem < 0) {
            this.bangHoiScore = 0;
            return;
        }

        DatabaseManager.bangHoi_diem.put(getBangHoiName(), this.bangHoiScore - diem);
        this.bangHoiScore = this.bangHoiScore - diem;
    }

    public void setBangHoiScore(int bangHoiScore) {

        if (bangHoiScore < 0) {
            this.bangHoiScore = 0;
            return;
        }

        DatabaseManager.bangHoi_diem.put(getBangHoiName(), bangHoiScore);
        this.bangHoiScore = bangHoiScore;
    }

    public int getBangHoiWarPoint() {
        return bangHoiWarPoint;
    }

    public void addBangHoiWarPoint(int warpoint) {

        if (warpoint < 0 && this.bangHoiWarPoint + warpoint < 0) {
            this.bangHoiWarPoint = 0;
            return;
        }

        this.bangHoiWarPoint = this.bangHoiWarPoint + warpoint;
    }

    public void removeBangHoiWarPoint(int warpoint) {

        if (this.bangHoiWarPoint - warpoint < 0) {
            this.bangHoiWarPoint = 0;
            return;
        }

        this.bangHoiWarPoint = this.bangHoiWarPoint - warpoint;
    }

    public void setBangHoiWarPoint(int warpoint) {

        if (warpoint < 0) {
            this.bangHoiWarPoint = 0;
            return;
        }

        this.bangHoiWarPoint = warpoint;
    }

    public int getSoLuongToiDa() {
        return bangHoiMaxMember;
    }

    public void addSoLuongToiDa(int so_luong_toi_da) {

        if (so_luong_toi_da < 0 && this.bangHoiMaxMember + so_luong_toi_da < 0) {
            this.bangHoiMaxMember = 0;
            return;
        }

        this.bangHoiMaxMember = this.bangHoiMaxMember + so_luong_toi_da;
    }

    public void removeSoLuongToiDa(int so_luong_toi_da) {

        if (this.bangHoiMaxMember - so_luong_toi_da < 0) {
            this.bangHoiMaxMember = 0;
            return;
        }

        this.bangHoiMaxMember = this.bangHoiMaxMember - so_luong_toi_da;
    }

    public void setSoLuongToiDa(int so_luong_toi_da) {

        if (so_luong_toi_da < 0) {
            this.bangHoiMaxMember = 0;
            return;
        }

        this.bangHoiMaxMember = so_luong_toi_da;
    }

    public List<String> getThanhVien() {
        return bangHoiMember;
    }

    public void addThanhVien(String playerName) {
        this.bangHoiMember.add(playerName);
    }

    public void removeThanhVien(String playerName) {
        this.bangHoiMember.remove(playerName);
    }

    public long getNgayThanhLap() {
        return bangHoiCreatedDate;
    }

    public void setNgayThanhLap(long ngay_thanh_lap) {

        if (ngay_thanh_lap < 0) {
            this.bangHoiCreatedDate = 0;
            return;
        }

        this.bangHoiCreatedDate = ngay_thanh_lap;
    }

    public int getBangHoiWarn() {
        return bangHoiWarn;
    }

    public void addWarn(int warn) {

        if (warn < 0 && this.bangHoiWarn + warn < 0) {
            this.bangHoiWarn = 0;
            return;
        }

        this.bangHoiWarn = this.bangHoiWarn + warn;
    }

    public void removeWarn(int warn) {

        if (this.bangHoiWarn - warn < 0) {
            this.bangHoiWarn = 0;
            return;
        }

        this.bangHoiWarn = this.bangHoiWarn - warn;
    }

    public void setBangHoiWarn(int bangHoiWarn) {

        if (bangHoiWarn < 0) {
            this.bangHoiWarn = 0;
            return;
        }

        this.bangHoiWarn = bangHoiWarn;
    }

    public int getSkillLevel(int skill) {
        if (skill == 1)
            return skill1;

        if (skill == 2)
            return skill2;

        if (skill == 3)
            return skill3;

        if (skill == 4)
            return skill4;

        return 0;
    }

    public void addSkillLevel(int skill, int level) {
        if (skill == 1)
            skill1 = skill1 + level;

        if (skill == 2)
            skill2 = skill2 + level;

        if (skill == 3)
            skill3 = skill3 + level;

        if (skill == 4)
            skill4 = skill4 + level;
    }

    public void removeSkillLevel(int skill, int level) {
        if (skill == 1)
            skill1 = skill1 - level;

        if (skill == 2)
            skill2 = skill2 - level;

        if (skill == 3)
            skill3 = skill3 - level;

        if (skill == 4)
            skill4 = skill4 - level;
    }

    public void setSkillLevel(int skill, int level) {
        if (skill == 1)
            skill1 = level;

        if (skill == 2)
            skill2 = level;

        if (skill == 3)
            skill3 = level;

        if (skill == 4)
            skill4 = level;
    }

}
