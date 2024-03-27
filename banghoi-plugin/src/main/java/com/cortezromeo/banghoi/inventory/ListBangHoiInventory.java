package com.cortezromeo.banghoi.inventory;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.inventory.page.Button;
import com.cortezromeo.banghoi.inventory.page.PagedPane;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.util.InventoryUtil;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ListBangHoiInventory {
	public static void openInventory(Player p) {

		PagedPane pagedPane = new PagedPane(InventoryFile.get().getInt("gui.listBangHoi.rows") - 1,
				InventoryFile.get().getInt("gui.listBangHoi.rows"),
				BangHoi.nms.addColor(InventoryFile.get().getString("gui.listBangHoi.title")));

		Map<String, Integer> sortedBangHoi_diem = DatabaseManager.bangHoi_diem.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		int i = 0;
		for (Map.Entry<String, Integer> bangHoi : sortedBangHoi_diem.entrySet()) {
			i--;

			if (DatabaseManager.bangHoiDatabase.containsKey(bangHoi.getKey())) {
				BangHoiData data = DatabaseManager.getBangHoiData(bangHoi.getKey());
				if (data.getBangHoiName() != null) {
					try {

						if (data.getBangHoiIcon() != null && data.getBangHoiIcon() != null) {
							pagedPane.addButton(new Button(InventoryUtil.getItem("material",
									data.getBangHoiIcon(),
									(short) 0,
									InventoryFile.get().getString("gui.listBangHoi.items.banghoi.name"),
									InventoryFile.get().getStringList(("gui.listBangHoi.items.banghoi.lore")), "bangHoiInfo",
									bangHoi.getKey(), -1 * i)));
						} else
							pagedPane.addButton(new Button(InventoryUtil.getItem(InventoryFile.get().getString("gui.listBangHoi.items.banghoi.type"),
								InventoryFile.get().getString(
										"gui.listBangHoi.items.banghoi.value"),
								(short) InventoryFile.get().getInt(
										"gui.listBangHoi.items.banghoi.data"),
								InventoryFile.get().getString("gui.listBangHoi.items.banghoi.name"),
								InventoryFile.get().getStringList(("gui.listBangHoi.items.banghoi.lore")), "bangHoiInfo",
								bangHoi.getKey(), -1 * i)));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		pagedPane.open(p);
	}
}
