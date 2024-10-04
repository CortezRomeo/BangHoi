package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
	public BangHoi plugin;

	public PlayerDeathListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(PlayerDeathEvent e) {

		if (!WarManager.eventStarted)
			return;

		Player killer = e.getEntity().getKiller();

		if (killer == null)
			return;

		Player p = e.getEntity().getPlayer();

		if (!DatabaseManager.playerDatabase.containsKey(p.getName()))
			return;

		if (DatabaseManager.playersWarProcess.contains(p.getName()))
			DatabaseManager.playersWarProcess.remove(p.getName());

		FileConfiguration mse = MessageFile.get();
		String prefix = mse.getString("PREFIX");

		if (killer.getType() == EntityType.PLAYER) {
			PlayerData pData = DatabaseManager.getPlayerData(p.getName());
			PlayerData killerData = DatabaseManager.getPlayerData(killer.getName());

			if (pData.getBangHoi() == null || killerData.getBangHoi() == null)
				return;

			if (pData.getBangHoi().equals(killerData.getBangHoi())) {
				killer.sendMessage(BangHoi.nms.addColor(
						prefix + mse.getString("bangHoiWar.gietNguoiCungBang").replace("%player%", p.getName())));
				return;
			}

			if (!WarManager.inWarWorld(killer)) {
				killer.sendMessage(
						BangHoi.nms.addColor(mse.getString("bangHoiWar.PREFIX") + mse.getString("bangHoiWar.saiTheGioi")));
				return;
			}

			if (WarManager.playerKills.getOrDefault(p.getName(), 0) - WarManager.playerDies.getOrDefault(p.getName(), 0) <= -5) {
				MessageUtil.sendMessage(killer, "&fVì số lần chết của người chơi " + p.getName() + " chênh lệch quá nhiều với số lần giết của người này nên không cộng điểm!");
				return;
			}

			int bonusScore = 0;
			if (DatabaseManager.getBangHoiData(killerData.getBangHoi()).getSkillLevel(SkillType.boostScore) > 0)
				bonusScore = 1;

			BangHoiManager.bangHoiAlert(killerData.getBangHoi(), mse.getString("bangHoiWar.playerCongDiem")
					.replace("%number%",
							String.valueOf(BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.player") + bonusScore))
					.replace("%p1%", killer.getName()).replace("%p2%", p.getName())
					.replace("%name%", BangHoiManager.getBangHoiName(pData.getBangHoi())));


			DatabaseManager.getBangHoiData(killerData.getBangHoi())
					.addBangHoiScore(BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.player") + bonusScore);

/*			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				LogManager.log(killer, p);
			});*/

			WarManager.playerKills.put(killer.getName(), WarManager.playerKills.getOrDefault(killer.getName(), 0) + 1);
			WarManager.playerDies.put(p.getName(), WarManager.playerDies.getOrDefault(p.getName(), 0) + 1);
			DatabaseManager.playersWarProcess.remove(p.getName());

			if (!WarManager.top.containsKey(killerData.getBangHoi()))
				WarManager.top.put(killerData.getBangHoi(), 0);

			WarManager.top.put(killerData.getBangHoi(),
					WarManager.top.get(killerData.getBangHoi())
							+ BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.player") + bonusScore);

		}

	}

}