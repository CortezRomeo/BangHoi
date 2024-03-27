package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiDataStorage;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerDataStorage;
import com.cortezromeo.banghoi.util.FilenameUtil;

import java.io.File;
import java.util.*;

public class DatabaseManager {

    // Data tạm thời
    public static Map<String, String> bangHoiInvitingPlayers = new HashMap<>();
    public static Map<String, Integer> bangHoi_diem = new HashMap<>();
    public static Map<String, String> bangHoi_customName = new HashMap<>();
    public static List<String> bangHoiChattingPlayers = new ArrayList<>();
    public static List<String> playersWarProcess = new ArrayList<>();

    // Data được lưu
    public static Map<String, PlayerData> playerDatabase = new HashMap<>();
    public static Map<String, BangHoiData> bangHoiDatabase = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static PlayerData getPlayerData(String name) {
        if (!playerDatabase.containsKey(name)) {
            loadPlayerData(name);
        }
        return playerDatabase.get(name);
    }

    public static BangHoiData getBangHoiData(String name) {
        if (!bangHoiDatabase.containsKey(name)) {
            loadBangHoiData(name);
        }
        return bangHoiDatabase.get(name);
    }

    public static void loadPlayerData(String player) {
        if (!playerDatabase.containsKey(player))
            playerDatabase.put(player, PlayerDataStorage.getplayerData(player));
    }

    public static void loadBangHoiData(String bangHoi) {
        bangHoiDatabase.put(bangHoi, BangHoiDataStorage.getBangHoiData(bangHoi));
    }

    public static void savePlayerData(String player) {
        PlayerDataStorage.savePlayerData(player, DatabaseManager.playerDatabase.get(player));
    }

    public static void saveBangHoiData(String bangHoi) {
        BangHoiDataStorage.saveBangHoiData(bangHoi, DatabaseManager.bangHoiDatabase.get(bangHoi));
    }

    public static void unloadPlayerData(String player) {
        PlayerDataStorage.savePlayerData(player, playerDatabase.get(player));
        playerDatabase.remove(player);
    }

    public static void unloadBangHoiData(String bangHoi) {
        BangHoiDataStorage.saveBangHoiData(bangHoi, bangHoiDatabase.get(bangHoi));
        bangHoi_diem.remove(bangHoi);
        bangHoiDatabase.remove(bangHoi);
    }

    public static void loadAllDatabase() {

        DebugManager.debug("LOADING DATABASE", "Loading bang hoi database...");
        File bangHoiFolder = new File(BangHoi.plugin.getDataFolder() + "/banghoiData");
        File[] listOfFilesBangHoi = bangHoiFolder.listFiles();

        if (listOfFilesBangHoi == null)
            return;

        int bangHoiDatabaseAmount = 0;
        int playerDatabaseAmount = 0;
        for (File file : listOfFilesBangHoi) {
            try {
                if (file.isFile()) {

                    String bangHoiName = FilenameUtil.removeExtension(file.getName());

                    loadBangHoiData(bangHoiName);
                    BangHoiData bangHoiData = getBangHoiData(bangHoiName);

                    // Fix old data
                    if (bangHoiData.getBangHoiIcon() != null && bangHoiData.getBangHoiIcon().equals("@null"))
                        bangHoiData.setBangHoiIcon(null);
                    if (bangHoiData.getTenCustom() != null && bangHoiData.getTenCustom().equals("@null"))
                        bangHoiData.setTenCustom(null);
                    bangHoiData.getThanhVien().remove("@null");

                    if (bangHoiData.getBangHoiName() == null || bangHoiData.getThanhVien().isEmpty()) {
                        try {
                            bangHoiDatabase.remove(bangHoiName);
                            if (file.delete()) {
                                continue;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    for (String thanhVien : bangHoiData.getThanhVien()) {
                        playerDatabaseAmount++;
                        PlayerData thanhVienData = getPlayerData(thanhVien);

                        if (thanhVienData.getBangHoi() == null)
                            bangHoiData.removeThanhVien(thanhVien);
                    }

                    bangHoiDatabaseAmount++;
                    saveBangHoiData(bangHoiName);
                    bangHoi_diem.put(bangHoiName, getBangHoiData(bangHoiName).getBangHoiScore());
                    bangHoi_customName.put(bangHoiName, getBangHoiData(bangHoiName).getTenCustom());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DebugManager.debug("LOADING DATABASE", "Loaded/Fixed " + bangHoiDatabaseAmount + " (" + playerDatabaseAmount + " members) amount of bang hoi");

        DebugManager.debug("LOADING DATABASE", "Loading player database...");
        File playerFolder = new File(BangHoi.plugin.getDataFolder() + "/playerData");
        File[] listOfFilesPlayer = playerFolder.listFiles();
        if (listOfFilesPlayer == null) return;

        playerDatabaseAmount = 0;
        for (File file : listOfFilesPlayer) {
            try {
                if (file.isFile()) {

                    String playerName = FilenameUtil.removeExtension(file.getName());
                    loadPlayerData(playerName);

                    PlayerData playerData = DatabaseManager.getPlayerData(playerName);
                    if (playerData.getBangHoi() == null) {
                        continue;
                    }

                    // Fix old data
                    if (playerData.getBangHoi() != null && playerData.getBangHoi().equals("@null"))
                        playerData.setBangHoi(null);
                    if (playerData.getChucVu() != null && playerData.getChucVu().equals("@null"))
                        playerData.setChucVu(null);

                    // player bang hoi != null || bang hoi == null
                    if (!DatabaseManager.bangHoiDatabase.containsKey(playerData.getBangHoi())
                            || !DatabaseManager.getBangHoiData(playerData.getBangHoi()).getThanhVien().contains(playerName)) {
                        resetPlayerData(playerData);
                        DatabaseManager.savePlayerData(playerName);
                    }

                    playerDatabaseAmount++;
                    savePlayerData(playerName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DebugManager.debug("LOADING DATABASE", "Loaded/Fixed " + playerDatabaseAmount + " amount of players");

    }

    private static void resetPlayerData(PlayerData playerData) {
        playerData.setBangHoi(null);
        playerData.setChucVu(null);
        playerData.setNgayThamGia(0);
    }

    public static void saveAllDatabase() {

        Set<String> playersData = playerDatabase.keySet();
        for (String player : playersData) {

            if (getPlayerData(player).getBangHoi() == null)
                continue;

            savePlayerData(player);
        }

        Set<String> bangHoiData = bangHoiDatabase.keySet();
        for (String bangHoi : bangHoiData)
            saveBangHoiData(bangHoi);

        DebugManager.debug("SAVING DATABASE", "Database has been saved!");

    }

}
