package com.cortezromeo.banghoi.util;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.MessageFile;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void throwErrorMessage(String message) {
        Bukkit.getLogger().severe(message);
        log("&4&l[BANG HOI ERROR] &c&lNếu lỗi này ảnh hưởng đến trải nghiệm của người chơi, hãy liên hệ mình qua discord: Cortez_Romeo#1290");
    }

    public static void sendBroadCast(String message) {

        if (message.equals(""))
            return;

        for (Player p : Bukkit.getOnlinePlayers()) {
            sendMessage(p, message);
        }
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(BangHoi.nms.addColor(message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(BangHoi.nms.addColor(message));
    }

    public static void sendMessage(Player player, String message) {

        if (player == null | message.equals(""))
            return;

        message = MessageFile.get().getString("PREFIX") + message;

        if (!BangHoi.PAPISupport())
            player.sendMessage(BangHoi.nms.addColor(message));
        else
            player.sendMessage(BangHoi.nms.addColor(PlaceholderAPI.setPlaceholders(player, message)));
    }

}
