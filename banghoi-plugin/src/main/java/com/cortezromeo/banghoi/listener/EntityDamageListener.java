
package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityDamageListener implements Listener {
	public BangHoi plugin;

	public EntityDamageListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void event(EntityDamageByEntityEvent e) {

		Entity v = e.getEntity();
		Entity d = e.getDamager();

		if (v == null || d == null)
			return;

		if (d instanceof Arrow) {
			Arrow a1 = (Arrow) e.getDamager();
			if (a1.getShooter() instanceof Player) {
				Player shooter = (Player) a1.getShooter();
				Damageable player = (Damageable) e.getEntity();
				if (player instanceof Player) {
					Player victim2 = (Player) player;

					if (!DatabaseManager.playerDatabase.containsKey(victim2.getName()))
						return;

					PlayerData victim2Data = DatabaseManager.getPlayerData(victim2.getName());
					PlayerData shooterData = DatabaseManager.getPlayerData(shooter.getName());
					
					if (shooterData.getBangHoi() == null || victim2Data.getBangHoi() == null)
						return;

					if (victim2Data.getBangHoi().equals(shooterData.getBangHoi())) {
						if (!DatabaseManager.PvPPlayers.contains(victim2)) {
							MessageUtil.sendMessage(shooter, MessageFile.get().getString("victimPvPOff")
									.replace("%player%", victim2.getName()));
							e.setCancelled(true);
							return;
						}
						if (!DatabaseManager.PvPPlayers.contains(shooter)) {
							MessageUtil.sendMessage(shooter, MessageFile.get().getString("damagerPvPOff"));
							e.setCancelled(true);
							return;
						}
					} else {
						if (!WarManager.eventStarted)
							return;

						WarManager.playerDmgs.put(shooter.getName(), (WarManager.playerDmgs.getOrDefault(shooter.getName(), 0D) + e.getDamage()));

						if (!DatabaseManager.playersWarProcess.contains(shooter.getName())) {
							DatabaseManager.playersWarProcess.add(shooter.getName());
							new BukkitRunnable() {
								@Override
								public void run() {
									DatabaseManager.playersWarProcess.remove(shooter.getName());
								}
							}.runTaskLaterAsynchronously(BangHoi.plugin, 20L * plugin.getConfig().getLong("bang-hoi-war.cooldown-xai-lenh"));
						}
						if (!DatabaseManager.playersWarProcess.contains(victim2.getName())) {
							DatabaseManager.playersWarProcess.add(victim2.getName());
							new BukkitRunnable() {
								@Override
								public void run() {
									DatabaseManager.playersWarProcess.remove(victim2.getName());
								}
							}.runTaskLaterAsynchronously(BangHoi.plugin, 20L * plugin.getConfig().getLong("bang-hoi-war.cooldown-xai-lenh"));
						}
					}
				}
			}
		}

		if (v.getType() != EntityType.PLAYER || d.getType() != EntityType.PLAYER)
			return;

		Player victim = (Player) v;
		Player damager = (Player) d;

		if (!DatabaseManager.playerDatabase.containsKey(victim.getName()))
			return;

        PlayerData victimData = DatabaseManager.getPlayerData(victim.getName());
		PlayerData damagerData = DatabaseManager.getPlayerData(damager.getName());

		if (damagerData.getBangHoi() == null || victimData.getBangHoi() == null)
			return;

		if (victimData.getBangHoi().equals(damagerData.getBangHoi())) {
			if (!DatabaseManager.PvPPlayers.contains(victim)) {
				MessageUtil.sendMessage(damager, MessageFile.get().getString("victimPvPOff")
						.replace("%player%", victim.getName()));
				e.setCancelled(true);
				return;
			}
			if (!DatabaseManager.PvPPlayers.contains(damager)) {
				MessageUtil.sendMessage(damager, MessageFile.get().getString("damagerPvPOff"));
				e.setCancelled(true);
			}
		} else {

			if (!WarManager.eventStarted)
				return;

			WarManager.playerDmgs.put(damager.getName(), (WarManager.playerDmgs.getOrDefault(damager.getName(), 0D) + e.getDamage()));

			if (!DatabaseManager.playersWarProcess.contains(damager.getName())) {
				DatabaseManager.playersWarProcess.add(damager.getName());
				new BukkitRunnable() {
					@Override
					public void run() {
						DatabaseManager.playersWarProcess.remove(damager.getName());
					}
				}.runTaskLaterAsynchronously(BangHoi.plugin, 20L * plugin.getConfig().getLong("bang-hoi-war.cooldown-xai-lenh"));
			}
			if (!DatabaseManager.playersWarProcess.contains(victim.getName())) {
				DatabaseManager.playersWarProcess.add(victim.getName());
				new BukkitRunnable() {
					@Override
					public void run() {
						DatabaseManager.playersWarProcess.remove(victim.getName());
					}
				}.runTaskLaterAsynchronously(BangHoi.plugin, 20L * plugin.getConfig().getLong("bang-hoi-war.cooldown-xai-lenh"));
			}
		}
	}

}