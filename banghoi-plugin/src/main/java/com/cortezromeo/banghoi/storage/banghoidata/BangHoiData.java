package com.cortezromeo.banghoi.storage.banghoidata;

import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import org.bukkit.Location;

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
    private List<String> members;
    private List<String> managers;
    private Location bangHoiSpawn;
    private int skill1;
    private int skill2;
    private int skill3;
    private int skill4;

    public BangHoiData(String bangHoiName, String bangHoiCustomName, String bangHoiFounder, int bangHoiScore, int bangHoiWarPoint, int bangHoiWarn,
                       int bangHoiMaxMember, long bangHoiCreatedDate, List<String> bangHoiMember, List<String> managers, String bangHoiIcon, Location bangHoiSpawn, int skill1, int skill2, int skill3, int skill4) {

        this.bangHoiName = bangHoiName;
        this.bangHoiCustomName = bangHoiCustomName;
        this.bangHoiFounder = bangHoiFounder;
        this.bangHoiScore = bangHoiScore;
        this.bangHoiWarPoint = bangHoiWarPoint;
        this.bangHoiMaxMember = bangHoiMaxMember;
        this.managers = managers;
        this.bangHoiWarn = bangHoiWarn;
        this.bangHoiCreatedDate = bangHoiCreatedDate;
        this.members = bangHoiMember;
        this.bangHoiIcon = bangHoiIcon;
        this.bangHoiSpawn = bangHoiSpawn;

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

    public Location getBangHoiSpawn() {
        return bangHoiSpawn;
    }

    public void setBangHoiSpawn(Location bangHoiSpawn) {
        this.bangHoiSpawn = bangHoiSpawn;
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
        return members;
    }

    public void addThanhVien(String playerName) {
        this.members.add(playerName);
    }

    public void removeThanhVien(String playerName) {
        this.members.remove(playerName);
        this.managers.remove(playerName);
    }

    public List<String> getManagers() {
        return managers;
    }

    public void addManager(String playerName) {
        this.managers.add(playerName);
    }

    public void removeManager(String playerName) {
        this.managers.remove(playerName);
    }

    public boolean isManager(String playerName) {
        return this.managers.contains(playerName);
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

    public int getSkillLevel(SkillType skillType) {
        if (skillType == SkillType.critDamage)
            return skill1;

        if (skillType == SkillType.boostScore)
            return skill2;

        if (skillType == SkillType.dodge)
            return skill3;

        if (skillType == SkillType.vampire)
            return skill4;

        return 0;
    }

    public void addSkillLevel(SkillType skillType, int level) {
        if (skillType == SkillType.critDamage)
            skill1 = skill1 + level;

        if (skillType == SkillType.boostScore)
            skill2 = skill2 + level;

        if (skillType == SkillType.dodge)
            skill3 = skill3 + level;

        if (skillType == SkillType.vampire)
            skill4 = skill4 + level;
    }

    public void removeSkillLevel(SkillType skillType, int level) {
        if (skillType == SkillType.critDamage)
            skill1 = skill1 - level;

        if (skillType == SkillType.boostScore)
            skill2 = skill2 - level;

        if (skillType == SkillType.dodge)
            skill3 = skill3 - level;

        if (skillType == SkillType.vampire)
            skill4 = skill4 - level;
    }

    public void setSkillLevel(SkillType skillType, int level) {
        if (skillType == SkillType.critDamage)
            skill1 = level;

        if (skillType == SkillType.boostScore)
            skill2 = level;

        if (skillType == SkillType.dodge)
            skill3 = level;

        if (skillType == SkillType.vampire)
            skill4 = level;
    }

}
