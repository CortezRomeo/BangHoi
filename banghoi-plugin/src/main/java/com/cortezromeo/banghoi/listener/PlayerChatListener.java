package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {
	public BangHoi plugin;

	public PlayerChatListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(AsyncPlayerChatEvent e) {

		Player p = e.getPlayer();

		if (DatabaseManager.bangHoiChattingPlayers.contains(p.getName())) {
			e.setCancelled(true);
			PlayerData pData = DatabaseManager.getPlayerData(p.getName());
			if (pData.getBangHoi() == null) {
				MessageUtil.sendMessage(p, "&cBạn không còn ở trong bang hội!");
				while (DatabaseManager.bangHoiChattingPlayers.contains(p.getName()))
					DatabaseManager.bangHoiChattingPlayers.remove(p.getName());
				e.setCancelled(false);
				return;
			}

			BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());

			for (String str : bangHoiData.getThanhVien()) {
				if (Bukkit.getPlayer(str) != null) {
					Bukkit.getPlayer(str)
							.sendMessage(BangHoi.nms.addColor(MessageFile.get().getString("chat").replaceAll("%player%",
									(pData.getChucVu().equals("Leader") ? "&6(Lãnh đạo)&f " : "&a(Thành viên)&f ")
											+ p.getName())
									.replaceAll("%msg%", e.getMessage())));
				}
			}

			String chatSpyMessage = "&cBang hội chat (SPY) &f[" + bangHoiData.getBangHoiName() + "] &e" + p.getName() + "&f: " + e.getMessage();
			for (Player player: Bukkit.getOnlinePlayers())
				if (player.hasPermission("banghoi.chatspy"))
					player.sendMessage(BangHoi.nms.addColor(chatSpyMessage));
			MessageUtil.log(chatSpyMessage);
		}
	}

}