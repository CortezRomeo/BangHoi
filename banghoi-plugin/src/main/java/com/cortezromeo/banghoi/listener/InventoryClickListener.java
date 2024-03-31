package com.cortezromeo.banghoi.listener;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.file.UpgradeFile;
import com.cortezromeo.banghoi.inventory.*;
import com.cortezromeo.banghoi.inventory.page.PagedPane;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	public InventoryClickListener(BangHoi plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {

		String title = e.getView().getTitle();
		Player p = (Player) e.getWhoClicked();

		if (title.equals(BangHoi.nms.addColor(InventoryFile.get().getString("gui.listBangHoi.title")))) {
			InventoryHolder holder = e.getInventory().getHolder();

			if (holder instanceof PagedPane)
				((PagedPane) holder).onClick(e);

			ItemStack item = e.getCurrentItem();
			if (item == null)
				return;

			if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

				String bangHoi = ChatColor.stripColor(item.getItemMeta().getLore().get(0));
				if (!bangHoi.contains(":"))
					return;
				bangHoi = bangHoi.replace(":", "");

				if (DatabaseManager.bangHoiDatabase.containsKey(bangHoi)) {
					ListThanhVienInventory.openInventory(p, bangHoi);
				}
			}
		}

		if (title.equals(BangHoi.nms.addColor(InventoryFile.get().getString("gui.viewMembers.title")))) {
			InventoryHolder holder = e.getInventory().getHolder();

			if (holder instanceof PagedPane)
				((PagedPane) holder).onClick(e);

			if (e.getSlot() == InventoryFile.get().getInt("gui.viewMembers.items.back.slot")) {
				ListBangHoiInventory.openInventory(p);
			}
		}

		if (title.equals(BangHoi.nms.addColor(InventoryFile.get().getString("gui.upgrade.title")))) {
			e.setCancelled(true);

			if (e.getCurrentItem() == null)
				return;

			FileConfiguration guiFile = InventoryFile.get();

			if (e.getSlot() == guiFile.getInt("gui.upgrade.items.upgradeSlot.slot"))
				p.openInventory(UpgradeSlotInventory.inventory(p));

			if (e.getSlot() == guiFile.getInt("gui.upgrade.items.warPointShop.slot"))
				p.openInventory(WarPointShopInventory.inventory(p));

		}

		if (title.equals(BangHoi.nms.addColor(InventoryFile.get().getString("gui.upgradeSlot.title")))) {
			e.setCancelled(true);

			if (e.getCurrentItem() == null)
				return;

			FileConfiguration guiFile = InventoryFile.get();

			if (e.getSlot() == guiFile.getInt("gui.upgradeSlot.items.upgradeSlotXu.slot") && BangHoi.ppAPI != null) {

				PlayerData pData = DatabaseManager.getPlayerData(p.getName());

				if (pData.getBangHoi() == null) {
					p.closeInventory();
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());
				FileConfiguration upgradeFileCfg = UpgradeFile.get();
				int point = BangHoi.ppAPI.look(p.getUniqueId());

				int requiredPoint = upgradeFileCfg.getInt("maxMemberSlot.playerPoints." + (bangHoiData.getSoLuongToiDa() + 1));

				if (requiredPoint == 0)
					requiredPoint = upgradeFileCfg.getInt("maxMemberSlot.playerPoints.else");

				if (point < requiredPoint) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("thieuPoint").replace("%point%",
							String.valueOf(requiredPoint)));
					p.closeInventory();
					return;
				}

				BangHoi.ppAPI.take(p.getUniqueId(), requiredPoint);

				bangHoiData.addSoLuongToiDa(1);
				DatabaseManager.saveBangHoiData(pData.getBangHoi());

				int oldSlot = bangHoiData.getSoLuongToiDa() - 1;
				int newSlot = bangHoiData.getSoLuongToiDa();

				MessageUtil.sendMessage(p,
						MessageFile.get().getString("nangCap").replaceAll("%oldslot%", String.valueOf(oldSlot))
								.replaceAll("%newslot%", String.valueOf(newSlot)));
				BangHoiManager.bangHoiAlert(pData.getBangHoi(),
						MessageFile.get().getString("thongBaoRieng.upSlot").replaceAll("%player%", p.getName())
								.replaceAll("%slot%", String.valueOf(bangHoiData.getSoLuongToiDa())));

				p.closeInventory();

			}

			if (e.getSlot() == guiFile.getInt("gui.upgradeSlot.items.upgradeSlotWarpoint.slot")) {

				PlayerData pData = DatabaseManager.getPlayerData(p.getName());

				if (pData.getBangHoi() == null) {
					p.closeInventory();
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return;
				}

				if (pData.getChucVu() != null)
					if (!pData.getChucVu().equals("Leader")) {
						p.closeInventory();
						MessageUtil.sendMessage(p, MessageFile.get().getString("memberUseWarpoint"));
						return;
					}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());
				FileConfiguration upgradeFileCfg = UpgradeFile.get();

				int requiredWarpoint = upgradeFileCfg.getInt("maxMemberSlot.warPoint." + (bangHoiData.getSoLuongToiDa() + 1));

				if (requiredWarpoint == 0)
					requiredWarpoint = upgradeFileCfg.getInt("maxMemberSlot.warPoint.");

				if (bangHoiData.getBangHoiWarPoint() < requiredWarpoint) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("thieuWarpoint").replace("%warpoint%",
							String.valueOf(requiredWarpoint)));
					p.closeInventory();
					return;
				}

				bangHoiData.removeBangHoiWarPoint(requiredWarpoint);
				bangHoiData.addSoLuongToiDa(1);
				DatabaseManager.saveBangHoiData(pData.getBangHoi());

				int oldSlot = bangHoiData.getSoLuongToiDa() - 1;
				int newSlot = bangHoiData.getSoLuongToiDa();

				MessageUtil.sendMessage(p,
						MessageFile.get().getString("nangCap").replaceAll("%oldslot%", String.valueOf(oldSlot))
								.replaceAll("%newslot%", String.valueOf(newSlot)));
				BangHoiManager.bangHoiAlert(pData.getBangHoi(),
						MessageFile.get().getString("thongBaoRieng.upSlot").replaceAll("%player%", p.getName())
								.replaceAll("%slot%", String.valueOf(bangHoiData.getSoLuongToiDa())));

				p.closeInventory();

			}

			if (e.getSlot() == guiFile.getInt("gui.upgradeSlot.items.back.slot"))
				p.openInventory(UpgradeInventory.inventory(p));

		}
		//

		if (title.equals(BangHoi.nms.addColor(InventoryFile.get().getString("gui.warPointShop.title")))) {
			e.setCancelled(true);

			if (e.getCurrentItem() == null)
				return;

			FileConfiguration guiFile = InventoryFile.get();

			if (e.getSlot() == guiFile.getInt("gui.warPointShop.items.1CritDamage.slot")) {

				PlayerData pData = DatabaseManager.getPlayerData(p.getName());

				if (pData.getBangHoi() == null) {
					p.closeInventory();
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());
				if (bangHoiData.getSkillLevel(SkillType.critDamage) == 1)
					return;

				if (pData.getChucVu() != null)
					if (!pData.getChucVu().equals("Leader")) {
						p.closeInventory();
						MessageUtil.sendMessage(p, MessageFile.get().getString("memberUseWarpoint"));
						return;
					}

				int requiredWarpoint = BangHoiManager.getWarPointCost(1, 1);

				if (requiredWarpoint <= 0) {
					p.closeInventory();
					return;
				}

				if (bangHoiData.getBangHoiWarPoint() < requiredWarpoint) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("thieuWarpoint").replace("%warpoint%",
							String.valueOf(requiredWarpoint)));
					p.closeInventory();
					return;
				}

				bangHoiData.removeBangHoiWarPoint(requiredWarpoint);
				bangHoiData.setSkillLevel(SkillType.critDamage, 1);
				DatabaseManager.saveBangHoiData(pData.getBangHoi());

				BangHoiManager.bangHoiAlert(pData.getBangHoi(),
						MessageFile.get().getString("thongBaoRieng.nangCap-1CritDamage"));
				p.closeInventory();
			}

			if (e.getSlot() == guiFile.getInt("gui.warPointShop.items.2BoostScore.slot")) {

				PlayerData pData = DatabaseManager.getPlayerData(p.getName());

				if (pData.getBangHoi() == null) {
					p.closeInventory();
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());
				if (bangHoiData.getSkillLevel(SkillType.boostScore) == 1)
					return;

				if (pData.getChucVu() != null)
					if (!pData.getChucVu().equals("Leader")) {
						p.closeInventory();
						MessageUtil.sendMessage(p, MessageFile.get().getString("memberUseWarpoint"));
						return;
					}

				int requiredWarpoint = BangHoiManager.getWarPointCost(2, 1);

				if (requiredWarpoint <= 0) {
					p.closeInventory();
					return;
				}

				if (bangHoiData.getBangHoiWarPoint() < requiredWarpoint) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("thieuWarpoint").replace("%warpoint%",
							String.valueOf(requiredWarpoint)));
					p.closeInventory();
					return;
				}

				bangHoiData.removeBangHoiWarPoint(requiredWarpoint);
				bangHoiData.setSkillLevel(SkillType.boostScore, 1);
				DatabaseManager.saveBangHoiData(pData.getBangHoi());

				BangHoiManager.bangHoiAlert(pData.getBangHoi(),
						MessageFile.get().getString("thongBaoRieng.nangCap-2BoostScore"));
				p.closeInventory();
			}

			if (e.getSlot() == guiFile.getInt("gui.warPointShop.items.3Dodge.slot")) {

				PlayerData pData = DatabaseManager.getPlayerData(p.getName());

				if (pData.getBangHoi() == null) {
					p.closeInventory();
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());
				if (bangHoiData.getSkillLevel(SkillType.dodge) == 2)
					return;

				if (pData.getChucVu() != null)
					if (!pData.getChucVu().equals("Leader")) {
						p.closeInventory();
						MessageUtil.sendMessage(p, MessageFile.get().getString("memberUseWarpoint"));
						return;
					}

				int requiredWarpoint = BangHoiManager.getWarPointCost(3, bangHoiData.getSkillLevel(SkillType.dodge) + 1);

				if (requiredWarpoint <= 0) {
					p.closeInventory();
					return;
				}

				if (bangHoiData.getBangHoiWarPoint() < requiredWarpoint) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("thieuWarpoint").replace("%warpoint%",
							String.valueOf(requiredWarpoint)));
					p.closeInventory();
					return;
				}

				bangHoiData.removeBangHoiWarPoint(requiredWarpoint);
				bangHoiData.setSkillLevel(SkillType.dodge, 1);
				DatabaseManager.saveBangHoiData(pData.getBangHoi());

				String message = "thongBaoRieng.nangCap-3Dodge-level";
				message = message + bangHoiData.getSkillLevel(SkillType.dodge);

				BangHoiManager.bangHoiAlert(pData.getBangHoi(),
						MessageFile.get().getString(message));

				p.closeInventory();
			}

			if (e.getSlot() == guiFile.getInt("gui.warPointShop.items.4Vampire.slot")) {

				PlayerData pData = DatabaseManager.getPlayerData(p.getName());

				if (pData.getBangHoi() == null) {
					p.closeInventory();
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(pData.getBangHoi());
				if (bangHoiData.getSkillLevel(SkillType.vampire) == 1)
					return;

				if (pData.getChucVu() != null)
					if (!pData.getChucVu().equals("Leader")) {
						p.closeInventory();
						MessageUtil.sendMessage(p, MessageFile.get().getString("memberUseWarpoint"));
						return;
					}

				int requiredWarpoint = BangHoiManager.getWarPointCost(4, 1);

				if (requiredWarpoint <= 0) {
					p.closeInventory();
					return;
				}

				if (bangHoiData.getBangHoiWarPoint() < requiredWarpoint) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("thieuWarpoint").replace("%warpoint%",
							String.valueOf(requiredWarpoint)));
					p.closeInventory();
					return;
				}

				bangHoiData.removeBangHoiWarPoint(requiredWarpoint);
				bangHoiData.setSkillLevel(SkillType.vampire, 1);
				DatabaseManager.saveBangHoiData(pData.getBangHoi());

				BangHoiManager.bangHoiAlert(pData.getBangHoi(),
						MessageFile.get().getString("thongBaoRieng.nangCap-4Vampire"));
				p.closeInventory();
			}

			//

			if (e.getSlot() == guiFile.getInt("gui.upgradeSlot.items.back.slot"))
				p.openInventory(UpgradeInventory.inventory(p));

		}

	}

}