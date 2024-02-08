package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	public PlayerJoinListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		if (DatabaseManager.getPlayerData(p.getName()) == null)
			Bukkit.getScheduler().runTaskAsynchronously(BangHoi.plugin,
					() -> DatabaseManager.loadPlayerData(p.getName()));

		if (WarManager.getBossBar(p) != null) {
			BossBar b = WarManager.getBossBar(p);

			b.removeAll();
			WarManager.bb.remove(p);

		}

		if (DatabaseManager.playersWarProcess.contains(p.getName()))
			DatabaseManager.playersWarProcess.remove(p.getName());

		new BukkitRunnable() {

			@Override
			public void run() {
				if (WarManager.eventStarted)
					WarManager.createBossBar(p);

				if (plugin.getConfig().getBoolean("bang-hoi-war.join-notification"))
					WarManager.sendMessageEventStatus(p);
			}
		}.runTaskLaterAsynchronously(BangHoi.plugin, 30);

	}

}
