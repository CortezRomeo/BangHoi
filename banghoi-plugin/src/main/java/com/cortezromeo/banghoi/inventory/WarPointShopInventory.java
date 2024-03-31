package com.cortezromeo.banghoi.inventory;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class WarPointShopInventory implements Listener {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	public WarPointShopInventory(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static Inventory inventory(Player p) {

		FileConfiguration guiFileCFG = InventoryFile.get();

		Inventory ivng = Bukkit.createInventory((InventoryHolder) p, guiFileCFG.getInt("gui.warPointShop.rows") * 9,
				BangHoi.nms.addColor(guiFileCFG.getString("gui.warPointShop.title")));

		for (int i = 0 ; i <= 26; i ++)
			ivng.setItem(i, InventoryUtil.getItem(InventoryFile.get().getString("borderItem.type"),
					InventoryFile.get().getString("borderItem.value"),
					(short) InventoryFile.get().getInt("borderItem.data"),
					InventoryFile.get().getString("borderItem.name"),
					InventoryFile.get().getStringList("borderItem.lore"), "", "", 0));

		BangHoiData bangHoiData = DatabaseManager.getBangHoiData(DatabaseManager.getPlayerData(p.getName()).getBangHoi());

		if (bangHoiData.getSkillLevel(SkillType.critDamage) == 0)
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.1CritDamage.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.1CritDamage.type"),
							guiFileCFG.getString("gui.warPointShop.items.1CritDamage.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.1CritDamage.data"),
							guiFileCFG.getString("gui.warPointShop.items.1CritDamage.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.1CritDamage.lore.locked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));
		else
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.1CritDamage.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.1CritDamage.type"),
							guiFileCFG.getString("gui.warPointShop.items.1CritDamage.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.1CritDamage.data"),
							guiFileCFG.getString("gui.warPointShop.items.1CritDamage.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.1CritDamage.lore.unlocked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));

		if (bangHoiData.getSkillLevel(SkillType.boostScore) == 0)
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.2BoostScore.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.2BoostScore.type"),
							guiFileCFG.getString("gui.warPointShop.items.2BoostScore.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.2BoostScore.data"),
							guiFileCFG.getString("gui.warPointShop.items.2BoostScore.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.2BoostScore.lore.locked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));
		else
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.2BoostScore.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.2BoostScore.type"),
							guiFileCFG.getString("gui.warPointShop.items.2BoostScore.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.2BoostScore.data"),
							guiFileCFG.getString("gui.warPointShop.items.2BoostScore.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.2BoostScore.lore.unlocked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));

		if (bangHoiData.getSkillLevel(SkillType.dodge) == 0)
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.3Dodge.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.3Dodge.type"),
							guiFileCFG.getString("gui.warPointShop.items.3Dodge.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.3Dodge.data"),
							guiFileCFG.getString("gui.warPointShop.items.3Dodge.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.3Dodge.lore.level1.locked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));
		else if (bangHoiData.getSkillLevel(SkillType.dodge) == 1)
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.3Dodge.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.3Dodge.type"),
							guiFileCFG.getString("gui.warPointShop.items.3Dodge.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.3Dodge.data"),
							guiFileCFG.getString("gui.warPointShop.items.3Dodge.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.3Dodge.lore.level1.unlocked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));
		else
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.3Dodge.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.3Dodge.type"),
							guiFileCFG.getString("gui.warPointShop.items.3Dodge.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.3Dodge.data"),
							guiFileCFG.getString("gui.warPointShop.items.3Dodge.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.3Dodge.lore.level2.unlocked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));

		if (bangHoiData.getSkillLevel(SkillType.vampire) == 0)
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.4Vampire.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.4Vampire.type"),
							guiFileCFG.getString("gui.warPointShop.items.4Vampire.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.4Vampire.data"),
							guiFileCFG.getString("gui.warPointShop.items.4Vampire.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.4Vampire.lore.locked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));
		else
			ivng.setItem(guiFileCFG.getInt("gui.warPointShop.items.4Vampire.slot"),
					InventoryUtil.getItem(guiFileCFG.getString("gui.warPointShop.items.4Vampire.type"),
							guiFileCFG.getString("gui.warPointShop.items.4Vampire.value"),
							(short) guiFileCFG.getInt("gui.warPointShop.items.4Vampire.data"),
							guiFileCFG.getString("gui.warPointShop.items.4Vampire.name"),
							guiFileCFG.getStringList("gui.warPointShop.items.4Vampire.lore.unlocked"), "warPointShop",
							DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));

		ivng.setItem(guiFileCFG.getInt("gui.upgradeSlot.items.back.slot"),
				InventoryUtil.getItem(guiFileCFG.getString("back.type"),
						guiFileCFG.getString("back.value"),
						(short) guiFileCFG.getInt("back.data"),
						guiFileCFG.getString("back.name"),
						guiFileCFG.getStringList("back.lore"), "","", 0));

		return ivng;
	}

}