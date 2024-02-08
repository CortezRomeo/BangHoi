package com.cortezromeo.banghoi.util;

import com.cortezromeo.banghoi.BangHoi;
import org.bukkit.configuration.file.FileConfiguration;

public class StringUtil {
    public static String timeFormat(long totalSeconds) {

        FileConfiguration config = BangHoi.plugin.getConfig();

        if (totalSeconds > 604800) {
            String str = config.getString("time-format.wwddhhmmss");
            str = str.replace("%w%", String.valueOf(totalSeconds / 604800));
            str = str.replace("%d%", String.valueOf(totalSeconds % 604800 / 86400));
            str = str.replace("%h%", String.valueOf((totalSeconds % 86400) / 3600));
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        if (totalSeconds > 86400) {
            String str = config.getString("time-format.ddhhmmss");
            str = str.replace("%d%", String.valueOf(totalSeconds / 86400));
            str = str.replace("%h%", String.valueOf((totalSeconds % 86400) / 3600));
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        if (totalSeconds > 3600) {

            String str = config.getString("time-format.hhmmss");
            str = str.replace("%h%", String.valueOf(totalSeconds / 3600));
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        if (totalSeconds >= 60) {
            String str = config.getString("time-format.mmss");
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        return BangHoi.nms.addColor(config.getString("time-format.ss").replace("%s%", String.valueOf(totalSeconds)));

    }
}
