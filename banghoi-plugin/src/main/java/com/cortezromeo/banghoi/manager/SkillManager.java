package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.file.UpgradeFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

enum SkillType {
    dodge, dodge2, critDamage, boostScore, vampire
}
public class SkillManager {

    public static HashMap<SkillType, Integer> skillWarPoint = new HashMap<>();
    public static HashMap<SkillType, Integer> skillValue = new HashMap<>();
    public static HashMap<SkillType, Double> skillChance = new HashMap<>();

    public static void setupValue() {
        FileConfiguration upgradeFileCfg = UpgradeFile.get();

        skillWarPoint.put(SkillType.dodge, upgradeFileCfg.getInt("skills.dodge.warPoint.1"));
        skillWarPoint.put(SkillType.dodge2, upgradeFileCfg.getInt("skills.dodge.warPoint.2"));
        skillWarPoint.put(SkillType.critDamage, upgradeFileCfg.getInt("skills.critDamage.warPoint"));
        skillWarPoint.put(SkillType.boostScore, upgradeFileCfg.getInt("skills.boostScore.warPoint"));
        skillWarPoint.put(SkillType.vampire, upgradeFileCfg.getInt("skills.vampire.warPoint"));


    }

    public static int getSkilLWarPoint(SkillType skillType) {
        return skillWarPoint.get(skillType);
    }

    public static int getSkillValue(SkillType skillType) {
        return skillValue.get(skillType);
    }

    public static double getSkillChance(SkillType skillType) {
        return skillChance.get(skillType);
    }

}
