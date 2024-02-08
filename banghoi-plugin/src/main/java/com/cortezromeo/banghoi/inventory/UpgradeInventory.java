package com.cortezromeo.banghoi.inventory;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class UpgradeInventory implements Listener {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	public UpgradeInventory(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static Inventory inventory(Player p) {

		FileConfiguration guiFileCFG = InventoryFile.get();

		Inventory ivng = Bukkit.createInventory((InventoryHolder) p, guiFileCFG.getInt("gui.upgrade.rows") * 9,
				BangHoi.nms.addColor(guiFileCFG.getString("gui.upgrade.title")));

		for (int i = 0 ; i <= 26; i ++)
			ivng.setItem(i, InventoryUtil.getItem(InventoryFile.get().getString("borderItem.type"),
					InventoryFile.get().getString("borderItem.value"),
					(short) InventoryFile.get().getInt("borderItem.data"),
					InventoryFile.get().getString("borderItem.name"),
					InventoryFile.get().getStringList("borderItem.lore"), "", "", 0));

		ivng.setItem(guiFileCFG.getInt("gui.upgrade.items.upgradeSlot.slot"),
				InventoryUtil.getItem(guiFileCFG.getString("gui.upgrade.items.upgradeSlot.type"),
						guiFileCFG.getString("gui.upgrade.items.upgradeSlot.value"),
						(short) guiFileCFG.getInt("gui.upgrade.items.upgradeSlot.data"),
						guiFileCFG.getString("gui.upgrade.items.upgradeSlot.name"),
						guiFileCFG.getStringList("gui.upgrade.items.upgradeSlot.lore"), "", "", 0));

		ivng.setItem(guiFileCFG.getInt("gui.upgrade.items.warPointShop.slot"),
				InventoryUtil.getItem(guiFileCFG.getString("gui.upgrade.items.warPointShop.type"),
						guiFileCFG.getString("gui.upgrade.items.warPointShop.value"),
						(short) guiFileCFG.getInt("gui.upgrade.items.warPointShop.data"),
						guiFileCFG.getString("gui.upgrade.items.warPointShop.name"),
						guiFileCFG.getStringList("gui.upgrade.items.warPointShop.lore"), "", "", 0));

		return ivng;
	}

}