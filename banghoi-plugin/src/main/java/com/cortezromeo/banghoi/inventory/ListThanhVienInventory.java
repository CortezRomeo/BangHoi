package com.cortezromeo.banghoi.inventory;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.inventory.page.Button;
import com.cortezromeo.banghoi.inventory.page.PagedPane;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.util.InventoryUtil;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ListThanhVienInventory {
	public static void openInventory(Player p, String bangHoi) {

		if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi)) {
			MessageUtil.sendMessage(p, MessageFile.get().getString("bangHoiKhongTonTai").replace("%name%", bangHoi));
			return;
		}

		PagedPane pagedPane = new PagedPane(InventoryFile.get().getInt("gui.viewMembers.rows") - 1,
				InventoryFile.get().getInt("gui.viewMembers.rows"),
				BangHoi.nms.addColor(InventoryFile.get().getString("gui.viewMembers.title")));

		Set<ItemStack> thanhVienItems = new HashSet<ItemStack>();

		for (String thanhvien : DatabaseManager.getBangHoiData(bangHoi).getThanhVien()) {
			try {
				thanhVienItems.add(InventoryUtil.getItem(InventoryFile.get().getString("gui.viewMembers.items.member.type"),
						InventoryFile.get().getString("gui.viewMembers.items.member.value"),
						(short) InventoryFile.get().getInt("gui.viewMembers.items.member.data"),
						InventoryFile.get().getString("gui.viewMembers.items.member.name"),
						InventoryFile.get().getStringList(("gui.viewMembers.items.member.lore")), "thanhVienInfo",
						thanhvien, 0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (ItemStack items : thanhVienItems) {
			pagedPane.addButton(new Button(items, new Consumer<InventoryClickEvent>() {
				@Override
				public void accept(InventoryClickEvent e) {
					HumanEntity whoClicked = e.getWhoClicked();

					if (whoClicked instanceof Player) {
						@SuppressWarnings("unused")
						Player player = (Player) whoClicked;

					}
				}
			}));
		}

		pagedPane.open(p);
	}
}
