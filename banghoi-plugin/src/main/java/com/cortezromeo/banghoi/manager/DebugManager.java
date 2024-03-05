package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.BangHoi;
import org.bukkit.Bukkit;

public class DebugManager {

    public static boolean debug;

    public static boolean getDebug() {
        return debug;
    }

    public static void setDebug(boolean b) {
        debug = b;
    }

    public static void debug(String prefix, String message) {

        if (!debug)
            return;

        Bukkit.getConsoleSender().sendMessage(BangHoi.nms.addColor("&6[BANG HOI DEBUG] &e" + prefix.toUpperCase() + " >>> " + message));
    }

}
