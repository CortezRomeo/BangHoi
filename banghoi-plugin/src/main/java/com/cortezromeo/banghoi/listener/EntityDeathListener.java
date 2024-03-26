package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {
	public BangHoi plugin;

	public EntityDeathListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(EntityDeathEvent e) {

		if (!WarManager.eventStarted)
			return;

		if (e.getEntity().getKiller() == null)
			return;

		if (e.getEntity().getType() == EntityType.PLAYER)
			return;

		for (String str : BangHoi.plugin.getConfig().getConfigurationSection("bang-hoi-war.cong-diem.mobs")
				.getKeys(false)) {

			if (str.equals(e.getEntity().getType().toString())) {
				Entity killer = e.getEntity().getKiller();

				FileConfiguration mse = MessageFile.get();

				if (killer.getType() == EntityType.PLAYER) {
					PlayerData killerData = DatabaseManager.getPlayerData(killer.getName());

					if (killerData.getBangHoi() == null)
						return;

					if (!killer.getWorld().getName()
							.equals(BangHoi.plugin.getConfig().getString("bang-hoi-war.world"))) {
						killer.sendMessage(BangHoi.nms.addColor(mse.getString("bangHoiWar.PREFIX") + mse.getString("bangHoiWar.saiTheGioi")));
						return;
					}

					int bonusScore = 0;
					if (DatabaseManager.getBangHoiData(killerData.getBangHoi()).getSkillLevel(2) > 0)
						bonusScore = 1;

					BangHoiManager.bangHoiAlert(killerData.getBangHoi(),
							mse.getString("bangHoiWar.mobCongDiem")
									.replaceAll("%number%",
											String.valueOf(BangHoi.plugin.getConfig()
													.getInt("bang-hoi-war.cong-diem.mobs." + str) + bonusScore))
									.replaceAll("%p1%", killer.getName())
									.replaceAll("%entity%", e.getEntity().getName()));
					DatabaseManager.getBangHoiData(killerData.getBangHoi())
							.addBangHoiScore(BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.mobs." + str) + bonusScore);

					if (!WarManager.top.containsKey(killerData.getBangHoi()))
						WarManager.top.put(killerData.getBangHoi(), 0);

					WarManager.top.put(killerData.getBangHoi(),
							WarManager.top.get(killerData.getBangHoi())
									+ BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.mobs." + str) + bonusScore);
				}
				break;
			}
		}

	}

}