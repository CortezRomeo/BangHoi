package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
	public BangHoi plugin;

	public PlayerRespawnListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (DatabaseManager.playersWarProcess.contains(p.getName()))
			DatabaseManager.playersWarProcess.remove(p.getName());
	}

}