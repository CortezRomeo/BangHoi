package com.cortezromeo.banghoi.depend;

import com.cortezromeo.banghoi.BangHoi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultDepend {

    public static Economy econ = null;

    public static boolean check() {
        Plugin plugin = BangHoi.plugin.getServer().getPluginManager().getPlugin("Vault");
        if(plugin!=null) {
            return true;
        }
        return false;
    }

    public static boolean setup() {

        if (!check())
            return false;

        RegisteredServiceProvider<Economy> rsp = BangHoi.plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null)
            return false;

        econ = rsp.getProvider();
        return econ != null;
    }

}
