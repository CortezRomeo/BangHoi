package com.cortezromeo.banghoi.inventory;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class UpgradeSlotInventory implements Listener {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	public UpgradeSlotInventory(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static Inventory inventory(Player p) {

		FileConfiguration guiFileCFG = InventoryFile.get();

		Inventory ivng = Bukkit.createInventory((InventoryHolder) p, guiFileCFG.getInt("gui.upgradeSlot.rows") * 9,
				BangHoi.nms.addColor(guiFileCFG.getString("gui.upgradeSlot.title")));

		for (int i = 0 ; i <= 26; i ++)
			ivng.setItem(i, InventoryUtil.getItem(InventoryFile.get().getString("borderItem.type"),
					InventoryFile.get().getString("borderItem.value"),
					(short) InventoryFile.get().getInt("borderItem.data"),
					InventoryFile.get().getString("borderItem.name"),
					InventoryFile.get().getStringList("borderItem.lore"), "", "", 0));

		ivng.setItem(guiFileCFG.getInt("gui.upgradeSlot.items.upgradeSlotXu.slot"),
				InventoryUtil.getItem(guiFileCFG.getString("gui.upgradeSlot.items.upgradeSlotXu.type"),
						guiFileCFG.getString("gui.upgradeSlot.items.upgradeSlotXu.value"),
						(short) guiFileCFG.getInt("gui.upgradeSlot.items.upgradeSlotXu.data"),
						guiFileCFG.getString("gui.upgradeSlot.items.upgradeSlotXu.name"),
						guiFileCFG.getStringList("gui.upgradeSlot.items.upgradeSlotXu.lore"), "upgrade",
						DatabaseManager.getPlayerData(p.getName()).getBangHoi(), 0));

		ivng.setItem(guiFileCFG.getInt("gui.upgradeSlot.items.upgradeSlotWarpoint.slot"),
				InventoryUtil.getItem(guiFileCFG.getString("gui.upgradeSlot.items.upgradeSlotWarpoint.type"),
						guiFileCFG.getString("gui.upgradeSlot.items.upgradeSlotWarpoint.value"),
						(short) guiFileCFG.getInt("gui.upgradeSlot.items.upgradeSlotWarpoint.data"),
						guiFileCFG.getString("gui.upgradeSlot.items.upgradeSlotWarpoint.name"),
						guiFileCFG.getStringList("gui.upgradeSlot.items.upgradeSlotWarpoint.lore"), "upgrade",
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