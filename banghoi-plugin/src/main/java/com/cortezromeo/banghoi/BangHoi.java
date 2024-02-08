package com.cortezromeo.banghoi;

import com.cortezromeo.banghoi.api.server.VersionSupport;
import com.cortezromeo.banghoi.command.BangHoiAdminCommand;
import com.cortezromeo.banghoi.command.BangHoiCommand;
import com.cortezromeo.banghoi.depend.PapiDepend;
import com.cortezromeo.banghoi.depend.VaultDepend;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.listener.*;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.DiHoaManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiDataStorage;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerDataStorage;
import com.cortezromeo.banghoi.support.version.cross.CrossVersionSupport;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.cortezromeo.banghoi.util.MessageUtil.log;

public final class BangHoi extends JavaPlugin {
    public static BangHoi plugin;
    private static final String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
    public static VersionSupport nms;
    public static PlayerPointsAPI ppAPI;
    private boolean serverSoftwareSupport = true;
    private static boolean papiSupport = false;

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
        log("         &e&lBANG HỘI");
        log("");
        log("&fLoad license key...");
        log("");
        log("&f--------------------------------");

        if (DiHoaManager.KeyStatus(plugin.getConfig().getString("license-key"))) {
            if (DiHoaManager.action(plugin.getConfig().getString("license-key"), "enable")) {
                log("&f--------------------------------");
                log("");
                log("         &a&lBANG HỘI");
                log("");
                log("&fVersion: &b" + getDescription().getVersion());
                log("&fAuthor: &bCortez_Romeo");
                log("&eKhởi chạy plugin trên phiên bản: " + version);
                log("&f--------------------------------");
                log("&2Cảm ơn đã ủng hộ mua plugin!");
            }
        } else {
            log("&f--------------------------------");
            log("");
            log("         &4&lBANG HỘI");
            log("       &eThiếu license key!");
            log("");
            log("&cVUI LÒNG GHI LICENSE KEY ĐÃ MUA TẠI DIHOASTORE.NET vào config.yml");
            log("");
            log("&f--------------------------------");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        initDatabase();
        initCommand();
        initListener();
        initSupport();

        WarManager.runTask(getConfig().getInt("bang-hoi-war.thoi-gian-su-kien"));

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Map.Entry<String, BangHoiData> banghoiData : DatabaseManager.bangHoiDatabase.entrySet()) {
                    DatabaseManager.saveBangHoiData(banghoiData.getKey());
                }
                for (Map.Entry<String, PlayerData> playerData : DatabaseManager.playerDatabase.entrySet()) {
                    DatabaseManager.savePlayerData(playerData.getKey());
                }
            }
        }.runTaskTimerAsynchronously(this, 20 * 300, 20 * 900);
    }

    private void initFile() {

        // config.yml
        saveDefaultConfig();
/*        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        reloadConfig();

        // message.yml
        String messageFileName = getForCurrentVersion("message.yml", "messagev13.yml");
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
        if (!new File(getDataFolder() + "/inventory.yml").exists()) {
            InventoryFile.setup();
            InventoryFile.setupLang();
        } else
            InventoryFile.fileExists();
        InventoryFile.reload();

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
    }

    public static boolean PAPISupport() {
        return papiSupport;
    }

    public static String getServerVersion() {
        return version;
    }

    public static String getForCurrentVersion(String v12, String v13) {
        switch (getServerVersion()) {
            case "v1_9_R1":
            case "v1_9_R2":
            case "v1_10_R1":
            case "v1_11_R1":
            case "v1_12_R1":
                return v12;
        }
        return v13;
    }

    @Override
    public void onDisable() {

        log("&f--------------------------------");
        log("");
        log("         &4&lBANG HỘI");
        log("");
        log("&fVersion: &b" + getDescription().getVersion());
        log("&fAuthor: &bCortez_Romeo");
        log("&f--------------------------------");

        DatabaseManager.saveAllDatabase();
        //warManager.eventStarted = false;

    }
}
