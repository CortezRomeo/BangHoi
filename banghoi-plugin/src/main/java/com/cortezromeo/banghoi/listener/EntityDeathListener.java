package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.SkillManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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

		Entity entity = e.getEntity();
		Player killer = e.getEntity().getKiller();

		if (killer == null || entity == null)
			return;

		if (entity.getType() == EntityType.PLAYER)
			return;

		if (killer.getType() != EntityType.PLAYER)
			return;

		PlayerData killerData = DatabaseManager.getPlayerData(killer.getName());

		if (killerData.getBangHoi() == null)
			return;

		FileConfiguration mse = MessageFile.get();

		if (!WarManager.inWarWorld(killer)) {
			killer.sendMessage(BangHoi.nms.addColor(mse.getString("bangHoiWar.PREFIX") + mse.getString("bangHoiWar.saiTheGioi")));
			return;
		}

		boolean isMythicMob = false;
		int scoreAdded = 0;

		if (BangHoi.MythicMSupport()) {
			if (MythicBukkit.inst().getMobManager().isMythicMob(e.getEntity())) {
				ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(e.getEntity());
				String mobName = mob.getMobType();
				scoreAdded = BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.mythicmobs." + mobName);
				if (scoreAdded > 0) {
					isMythicMob = true;
				}
			}
		}

		if (!isMythicMob) {
			scoreAdded = BangHoi.plugin.getConfig().getInt("bang-hoi-war.cong-diem.mobs." + entity.getType().toString());
			if (scoreAdded == 0)
				return;
		}

		int bonusScore = 0;
		if (DatabaseManager.getBangHoiData(killerData.getBangHoi()).getSkillLevel(SkillType.boostScore) > 0)
			bonusScore = Integer.parseInt(SkillManager.getSkillValue(SkillType.boostScore));

		BangHoiManager.bangHoiAlert(killerData.getBangHoi(),
				mse.getString("bangHoiWar.mobCongDiem")
						.replace("%number%",
								String.valueOf(scoreAdded + bonusScore))
						.replace("%p1%", killer.getName())
						.replace("%entity%", e.getEntity().getName()));
		DatabaseManager.getBangHoiData(killerData.getBangHoi())
				.addBangHoiScore(scoreAdded + bonusScore);

		if (!WarManager.top.containsKey(killerData.getBangHoi()))
			WarManager.top.put(killerData.getBangHoi(), 0);

		WarManager.top.put(killerData.getBangHoi(),
				WarManager.top.get(killerData.getBangHoi()) + scoreAdded + bonusScore);
	}
}