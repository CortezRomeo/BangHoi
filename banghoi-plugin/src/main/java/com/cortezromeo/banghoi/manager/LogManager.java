package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

public class LogManager {

    public static void log(Player player, Player victim) {

        File file = new File(BangHoi.plugin.getDataFolder() + "/log/");
        if (!file.exists()) {
            file.mkdirs();
        }

        LocalDate currentDate = LocalDate.now();
        String fileName = currentDate.toString() + ".txt";

        File logFile = new File(BangHoi.plugin.getDataFolder() + "/log/" + fileName);

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        PlayerData playerData = DatabaseManager.getPlayerData(player.getName());
        PlayerData victimData = DatabaseManager.getPlayerData(victim.getName());

        String logMessage = "[" + df.format(date) + "] ";
        logMessage = logMessage + player.getName() + " [" + playerData.getBangHoi() + "] killed " + victim.getName() + " [" + victimData.getBangHoi() + "]";

        writeToFile(logFile, logMessage);

    }

    private static void writeToFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
