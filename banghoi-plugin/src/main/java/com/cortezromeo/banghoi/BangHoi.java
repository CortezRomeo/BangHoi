package com.cortezromeo.banghoi;

import com.cortezromeo.banghoi.api.server.VersionSupport;
import com.cortezromeo.banghoi.command.BangHoiAdminCommand;
import com.cortezromeo.banghoi.command.BangHoiCommand;
import com.cortezromeo.banghoi.depend.PapiDepend;
import com.cortezromeo.banghoi.depend.VaultDepend;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.file.UpgradeFile;
import com.cortezromeo.banghoi.listener.*;
import com.cortezromeo.banghoi.manager.*;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiDataStorage;
import com.cortezromeo.banghoi.storage.playerdata.PlayerDataStorage;
import com.cortezromeo.banghoi.support.version.cross.CrossVersionSupport;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

import static com.cortezromeo.banghoi.util.MessageUtil.log;

public final class BangHoi extends JavaPlugin implements Listener {
    public static BangHoi plugin;
    private static final String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
    public static VersionSupport nms;
    public static PlayerPointsAPI ppAPI;
    private boolean serverSoftwareSupport = true;
    private static boolean papiSupport = false;
    private static boolean mythicMobSupport = false;
    private boolean developerMode = true;

    @Override
    public void onLoad() {

        plugin = this;
        nms = new CrossVersionSupport(plugin);

    }

    @Override
    public void onEnable() {

        if (!serverSoftwareSupport) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        initFile();

        log("&f--------------------------------");
        log("");
        log("  _                         _           _ ");
        log(" | |                       | |         (_)");
        log(" | |__   __ _ _ __   __ _  | |__   ___  _ ");
        log(" | '_ \\ / _` | '_ \\ / _` | | '_ \\ / _ \\| |");
        log(" | |_) | (_| | | | | (_| | | | | | (_) | |");
        log(" |_.__/ \\__,_|_| |_|\\__, | |_| |_|\\___/|_|");
        log("                     __/ |                ");
        log("                    |___/                 ");
        log("");
        log("&fĐang xác minh license key...");
        log("");
        log("&f--------------------------------");

        if (!developerMode) {
            if (DiHoaManager.KeyStatus(plugin.getConfig().getString("license-key"))) {
                if (DiHoaManager.action(plugin.getConfig().getString("license-key"), "enable")) {
                    log("&f--------------------------------");
                    log("");
                    log("&2  _                         _           _ ");
                    log("&2 | |                       | |         (_)");
                    log("&2 | |__   __ _ _ __   __ _  | |__   ___  _ ");
                    log("&2 | '_ \\ / _` | '_ \\ / _` | | '_ \\ / _ \\| |");
                    log("&2 | |_) | (_| | | | | (_| | | | | | (_) | |");
                    log("&2 |_.__/ \\__,_|_| |_|\\__, | |_| |_|\\___/|_|");
                    log("&2                     __/ |                ");
                    log("&2                    |___/                 ");
                    log("");
                    log("&fVersion: &b" + getDescription().getVersion());
                    log("&fAuthor: &bCortez_Romeo");
                    log("&eKhởi chạy plugin trên phiên bản: " + version);
                    log("&f--------------------------------");
                    log("&2Cảm ơn bạn đã ủng hộ mua plugin!");
                }
            } else {
                log("&f--------------------------------");
                log("");
                log("&4  _                         _           _ ");
                log("&4 | |                       | |         (_)");
                log("&4 | |__   __ _ _ __   __ _  | |__   ___  _ ");
                log("&4 | '_ \\ / _` | '_ \\ / _` | | '_ \\ / _ \\| |");
                log("&4 | |_) | (_| | | | | (_| | | | | | (_) | |");
                log("&4 |_.__/ \\__,_|_| |_|\\__, | |_| |_|\\___/|_|");
                log("&4                     __/ |                ");
                log("&4                    |___/                 ");
                log("");
                log("       &eThiếu license key!");
                log("");
                log("&cVUI LÒNG GHI LICENSE KEY ĐÃ MUA TẠI DIHOASTORE.NET vào config.yml");
                log("");
                log("&f--------------------------------");
                Bukkit.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } else
            log("&a&lBANG HOI DEVELOPER MODE IS ON! ENABLING PLUGIN");

        DebugManager.setDebug(BangHoi.plugin.getConfig().getBoolean("debug"));

        initDatabase();
        initCommand();
        initListener();
        initSupport();
        new SkillManager(this);
        SkillManager.setupValue();

        WarManager.runTask(getConfig().getInt("bang-hoi-war.thoi-gian-su-kien"));

        if (getConfig().getLong("auto-save") != 0) {
            long seconds = getConfig().getLong("auto-save.time");
            new BukkitRunnable() {
                @Override
                public void run() {
                    DatabaseManager.saveAllDatabase();
                }
            }.runTaskTimerAsynchronously(this, 20 * seconds, 20 * seconds);
        }

        if (getConfig().getBoolean("use-bstats"))
            new Metrics(this, 21611);
    }

    private void initFile() {

        // config.yml
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile, "bang-hoi-war.cong-diem.mobs", "bang-hoi-war.cong-diem.mythicmobs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();

        // message.yml
        String messageFileName = "messagev13.yml";
        MessageFile.setup();
        MessageFile.saveDefault();
        File messageFile = new File(getDataFolder(), "message.yml");
        try {
            ConfigUpdater.update(this, messageFileName, messageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageFile.reload();

        // inventory.yml
        String inventoryFileName = "inventory.yml";
        InventoryFile.setup();
        InventoryFile.saveDefault();
        File inventoryFile = new File(getDataFolder(), inventoryFileName);
        try {
            ConfigUpdater.update(this, inventoryFileName, inventoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InventoryFile.reload();

        // upgrade.yml
        String upgradeFileName = "upgrade.yml";
        UpgradeFile.setup();
        UpgradeFile.saveDefault();
        File upgradeFIle = new File(getDataFolder(), upgradeFileName);
        try {
            ConfigUpdater.update(this, upgradeFileName, upgradeFIle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UpgradeFile.reload();
    }

    private void initDatabase() {
        PlayerDataStorage.init();
        BangHoiDataStorage.init();
        DatabaseManager.loadAllDatabase();
    }

    private void initCommand() {
        new BangHoiCommand(this);
        new BangHoiAdminCommand(this);
    }

    private void initListener() {
        new InventoryClickListener(this);
        new EntityDamageListener(this);
        new EntityDeathListener(this);
        new PlayerChatListener(this);
        new PlayerDeathListener(this);
        new PlayerJoinListener(this);
        new PlayerCommandPreprocessListener(this);
        new PlayerRespawnListener(this);
    }

    private void initSupport() {

        VaultDepend.setup();

        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints"))
            ppAPI = PlayerPoints.getInstance().getAPI();

        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PapiDepend().register();

        if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null)
            mythicMobSupport = true;
    }

    public static boolean PAPISupport() {
        return papiSupport;
    }

    public static boolean MythicMSupport() {
        return mythicMobSupport;
    }

    @Override
    public void onDisable() {

        log("&f--------------------------------");
        log("");
        log("&c  _                         _           _ ");
        log("&c | |                       | |         (_)");
        log("&c | |__   __ _ _ __   __ _  | |__   ___  _ ");
        log("&c | '_ \\ / _` | '_ \\ / _` | | '_ \\ / _ \\| |");
        log("&c | |_) | (_| | | | | (_| | | | | | (_) | |");
        log("&c |_.__/ \\__,_|_| |_|\\__, | |_| |_|\\___/|_|");
        log("&c                     __/ |                ");
        log("&c                    |___/                 ");
        log("");
        log("&fVersion: &b" + getDescription().getVersion());
        log("&fAuthor: &bCortez_Romeo");
        log("&f--------------------------------");

        DatabaseManager.saveAllDatabase();

        if (!WarManager.bb.isEmpty())
            for (Player player : WarManager.bb.keySet())
                WarManager.bb.get(player).removeAll();

        //warManager.eventStarted = false;

    }
}
