package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {
	public BangHoi plugin;

	public PlayerCommandPreprocessListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(PlayerCommandPreprocessEvent e) {

		if (!WarManager.eventStarted)
			return;

		Player p = e.getPlayer();

		if (!DatabaseManager.playersWarProcess.contains(p.getName()) || !WarManager.inWarWorld(p))
			return;

		String command = e.getMessage().toLowerCase();
		if (command.startsWith("/")) {
			e.setCancelled(true);
			MessageUtil.sendMessage(p, "&cBạn đang trong trạng thái chiến đấu, không thể xài lệnh!");
		}

	}

}