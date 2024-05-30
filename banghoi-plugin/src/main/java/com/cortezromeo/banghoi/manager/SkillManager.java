package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.file.UpgradeFile;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Random;

public class SkillManager implements Listener {
    public BangHoi plugin;

    public SkillManager(BangHoi plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private static FileConfiguration mse = MessageFile.get();
    public static HashMap<SkillType, Integer> skillWarPoint = new HashMap<>();
    public static HashMap<SkillType, String> skillValue = new HashMap<>();
    public static HashMap<SkillType, Double> skillChance = new HashMap<>();

    public static void setupValue() {

        FileConfiguration upgradeFileCfg = UpgradeFile.get();

        skillWarPoint.put(SkillType.dodge, upgradeFileCfg.getInt("skills.dodge.warPoint.1"));
        skillWarPoint.put(SkillType.dodge2, upgradeFileCfg.getInt("skills.dodge.warPoint.2"));
        skillWarPoint.put(SkillType.critDamage, upgradeFileCfg.getInt("skills.critDamage.warPoint"));
        skillWarPoint.put(SkillType.boostScore, upgradeFileCfg.getInt("skills.boostScore.warPoint"));
        skillWarPoint.put(SkillType.vampire, upgradeFileCfg.getInt("skills.vampire.warPoint"));

        skillValue.put(SkillType.boostScore, upgradeFileCfg.getString("skills.boostScore.bonus"));
        try {
            skillValue.put(SkillType.critDamage, upgradeFileCfg.getString("skills.critDamage.damage").replace("\\s+", ""));
            skillValue.put(SkillType.vampire, upgradeFileCfg.getString("skills.vampire.heal").replace("\\s+", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        skillChance.put(SkillType.critDamage, upgradeFileCfg.getDouble("skills.critDamage.chance"));
        skillChance.put(SkillType.vampire, upgradeFileCfg.getDouble("skills.vampire.chance"));
        skillChance.put(SkillType.dodge, upgradeFileCfg.getDouble("skills.dodge.chance"));
        skillChance.put(SkillType.dodge2, upgradeFileCfg.getDouble("skills.dodge.chance"));

    }

    public static int getSkilLWarPoint(SkillType skillType) {
        return skillWarPoint.get(skillType);
    }

    public static String getSkillValue(SkillType skillType) {
        return skillValue.get(skillType);
    }

    public static double getSkillChance(SkillType skillType) {
        return skillChance.get(skillType);
    }

    @EventHandler
    public void critDamageSkill(EntityDamageByEntityEvent event) {

        if (!WarManager.eventStarted || event.getDamage() == 0)
            return;

        Entity d = event.getDamager();
        Entity v = event.getEntity();

        if (d == null || v == null)
            return;

        if (d.getType() != EntityType.PLAYER || v.getType() != EntityType.PLAYER)
            return;

        Player victim = (Player) v;

        if (!DatabaseManager.playerDatabase.containsKey(victim.getName()))
            return;

        String bangHoiName = DatabaseManager.getPlayerData(d.getName()).getBangHoi();

        if (bangHoiName == null || DatabaseManager.getPlayerData(v.getName()).getBangHoi() == null)
            return;

        if (DatabaseManager.getBangHoiData(bangHoiName).getSkillLevel(SkillType.critDamage) == 0)
            return;

        Player damager = (Player) d;
        if (new Random().nextDouble() < SkillManager.getSkillChance(SkillType.critDamage) / 100) {
            try {
                double damage = StringUtil.evaluate(SkillManager.getSkillValue(SkillType.critDamage).replace("%damage%", String.valueOf(event.getDamage())));
                long formatDamage = Math.round(damage);
                event.setDamage(damage);

                Location victimLocation = victim.getLocation();
                victimLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, victimLocation, 2);
                victimLocation.getWorld().playSound(victimLocation, Sound.ENTITY_BLAZE_HURT, 1, 2);

                damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.1CritDamage.damager")
                        .replace("%player%", victim.getName())
                        .replace("%dmg%", String.valueOf(formatDamage))));
                victim.sendMessage(BangHoi.nms.addColor(mse.getString("skill.1CritDamage.victim")
                        .replace("%player%", damager.getName())));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void dodgeSkill(EntityDamageByEntityEvent event) {

        if (!WarManager.eventStarted || event.getDamage() == 0)
            return;

        Entity v = event.getEntity();
        Entity d = event.getDamager();

        if (v == null || d == null)
            return;

        if (v.getType() != EntityType.PLAYER || d.getType() != EntityType.PLAYER)
            return;

        Player victim = (Player) v;
        Player damager = (Player) d;

        if (!DatabaseManager.playerDatabase.containsKey(victim.getName()))
            return;

        PlayerData victimData = DatabaseManager.getPlayerData(victim.getName());

        if (victimData.getBangHoi() == null || DatabaseManager.getPlayerData(damager.getName()).getBangHoi() == null)
            return;

        int skill3Level = DatabaseManager.getBangHoiData(victimData.getBangHoi()).getSkillLevel(SkillType.dodge);
        if (skill3Level > 0) {
            double chance = (DatabaseManager.getBangHoiData(victimData.getBangHoi())).getSkillLevel(SkillType.dodge) == 1 ? SkillManager.getSkillChance(SkillType.dodge) : SkillManager.getSkillChance(SkillType.dodge2);
            if (new Random().nextDouble() < chance / 100) {
                event.setCancelled(true);

                Location victimLocation = victim.getLocation();
                victimLocation.getWorld().playSound(victimLocation, Sound.ITEM_SHIELD_BLOCK, 1, 2);

                if (skill3Level == 2) {
                    damager.damage(event.getDamage());

                    damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv2.damager")
                            .replace("%player%", victim.getName())
                            .replace("%dmg%", String.valueOf(Math.round(event.getDamage())))));
                    victim.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv2.victim")
                            .replace("%player%", damager.getName())
                            .replace("%dmg%", String.valueOf(Math.round(event.getDamage())))));
                } else {
                    damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv1.damager")
                            .replace("%player%", victim.getName())));
                    victim.sendMessage(BangHoi.nms.addColor(mse.getString("skill.3Dodge.lv1.victim")
                            .replace("%player%", damager.getName())));
                }
            }
        }
    }

    @EventHandler
    public void vampireSkill(EntityDamageByEntityEvent event) {

        if (!WarManager.eventStarted || event.getDamage() == 0)
            return;

        Entity v = event.getEntity();
        Entity d = event.getDamager();

        if (v == null || d == null)
            return;

        if (v.getType() != EntityType.PLAYER || d.getType() != EntityType.PLAYER)
            return;

        Player victim = (Player) v;
        Player damager = (Player) d;

        if (!DatabaseManager.playerDatabase.containsKey(victim.getName()))
            return;

        PlayerData damagerData = DatabaseManager.getPlayerData(damager.getName());

        if (damagerData.getBangHoi() == null || DatabaseManager.getPlayerData(victim.getName()).getBangHoi() == null)
            return;

        if (DatabaseManager.getBangHoiData(damagerData.getBangHoi()).getSkillLevel(SkillType.vampire) > 0) {
            if (new Random().nextDouble() < SkillManager.getSkillChance(SkillType.vampire) / 100) {

                double pMaxHP = damager.getMaxHealth();
                try {
                    double revivingHP = StringUtil.evaluate(SkillManager.getSkillValue(SkillType.vampire).replace("%playerMaxHealth%", String.valueOf(pMaxHP)).replaceAll("%playerHealth%", String.valueOf(damager.getHealth())));
                    if (damager.getHealth() + revivingHP > pMaxHP)
                        damager.setHealth(pMaxHP);
                    else
                        damager.setHealth(damager.getHealth() + revivingHP);

                    Location damagerLocation = damager.getLocation();
                    damagerLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, damagerLocation, 2);

                    damager.sendMessage(BangHoi.nms.addColor(mse.getString("skill.4Vampire")
                            .replace("%hp%", String.valueOf(Math.round(revivingHP)))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

}
