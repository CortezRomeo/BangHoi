
package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

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
						e.setCancelled(true);
						MessageUtil.sendMessage(shooter, MessageFile.get().getString("danhNguoiTrongBangHoi")
								.replace("%player%", victim2.getName()));
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
							}.runTaskLaterAsynchronously(BangHoi.plugin, 20 * 15);
						}
						if (!DatabaseManager.playersWarProcess.contains(victim2.getName())) {
							DatabaseManager.playersWarProcess.add(victim2.getName());
							new BukkitRunnable() {
								@Override
								public void run() {
									DatabaseManager.playersWarProcess.remove(victim2.getName());
								}
							}.runTaskLaterAsynchronously(BangHoi.plugin, 20 * 15);
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
			e.setCancelled(true);
			MessageUtil.sendMessage(damager,
					MessageFile.get().getString("danhNguoiTrongBangHoi").replace("%player%", victim.getName()));
		} else {

			if (!WarManager.eventStarted)
				return;

			FileConfiguration mse = MessageFile.get();

			// skill 3
			int skill3Level = DatabaseManager.getBangHoiData(victimData.getBangHoi()).getSkillLevel(3);
			if (skill3Level > 0) {
				double chance = new Random().nextDouble();
				if (chance < 0.15) {
					e.setCancelled(true);

					Location victimLocation = victim.getLocation();
					victimLocation.getWorld().playSound(victimLocation, Sound.ITEM_SHIELD_BLOCK, 1, 2);

					if (skill3Level == 2) {
						damager.damage(e.getDamage());

						damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv2.damager")
								.replaceAll("%player%", victim.getName())
								.replaceAll("%dmg%", String.valueOf(Math.round(e.getDamage())))));
						victim.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv2.victim")
								.replaceAll("%player%", damager.getName())
								.replaceAll("%dmg%", String.valueOf(Math.round(e.getDamage())))));
					} else {
						damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv1.damager")
								.replaceAll("%player%", victim.getName())));
						victim.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv1.victim")
								.replace("%player%", damager.getName())));
					}
					return;
				}
			}
			//

			// skill 1
			if (DatabaseManager.getBangHoiData(damagerData.getBangHoi()).getSkillLevel(1) > 0) {
				double chance = new Random().nextDouble();
				if (chance < 0.25) {
					double damage = e.getDamage() * 2;
					long formatDamage = Math.round(damage);
					e.setDamage(damage);

					Location victimLocation = victim.getLocation();
					victimLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, victimLocation, 2);
					victimLocation.getWorld().playSound(victimLocation, Sound.ENTITY_BLAZE_HURT, 1, 2);

					damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.1CritDamage.damager")
							.replaceAll("%player%", victim.getName())
							.replaceAll("%dmg%", String.valueOf(formatDamage))));
					victim.sendMessage(BangHoi.nms.addColor(mse.getString("skill.1CritDamage.victim")
							.replace("%player%", damager.getName())));
				}
			}
			//

			// skill 4
			if (DatabaseManager.getBangHoiData(damagerData.getBangHoi()).getSkillLevel(4) > 0) {
				double chance = new Random().nextDouble();
				if (chance < 0.15) {

					double pMaxHP = damager.getMaxHealth();
					double revivingHP = pMaxHP / 5;
					if (damager.getHealth() + revivingHP > pMaxHP)
						damager.setHealth(pMaxHP);
					else
						damager.setHealth(damager.getHealth() + revivingHP);

					Location damagerLocation = damager.getLocation();
					damagerLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, damagerLocation, 2);

					damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.4Vampire")
							.replace("%hp%", String.valueOf(Math.round(revivingHP)))));
				}
			}
			//

			WarManager.playerDmgs.put(damager.getName(), (WarManager.playerDmgs.getOrDefault(damager.getName(), 0D) + e.getDamage()));

			if (!DatabaseManager.playersWarProcess.contains(damager.getName())) {
				DatabaseManager.playersWarProcess.add(damager.getName());
				new BukkitRunnable() {
					@Override
					public void run() {
						DatabaseManager.playersWarProcess.remove(damager.getName());
					}
				}.runTaskLaterAsynchronously(BangHoi.plugin, 20 * 15);
			}
			if (!DatabaseManager.playersWarProcess.contains(victim.getName())) {
				DatabaseManager.playersWarProcess.add(victim.getName());
				new BukkitRunnable() {
					@Override
					public void run() {
						DatabaseManager.playersWarProcess.remove(victim.getName());
					}
				}.runTaskLaterAsynchronously(BangHoi.plugin, 20 * 15);
			}
		}
	}

}