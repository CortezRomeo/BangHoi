package com.cortezromeo.banghoi.command;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.SkillType;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.*;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.Map.Entry;

import static com.cortezromeo.banghoi.util.MessageUtil.sendMessage;

public class BangHoiAdminCommand implements CommandExecutor, TabExecutor {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	private List<String> confirmCommand = new ArrayList<>();
	private Set<String> types = new HashSet<>();

	public BangHoiAdminCommand(BangHoi plugin) {
		this.plugin = plugin;
		plugin.getCommand("banghoiadmin").setExecutor(this);

		types.add("diem");
		types.add("warpoint");
		types.add("skill");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (sender instanceof Player) {

			Player p = (Player) sender;

			if (!p.hasPermission("banghoi.admin")) {
				sendMessage(p, MessageFile.get().getString("noPermission"));
				return false;
			}
		}

		// reload
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				BangHoi.plugin.reloadConfig();
				InventoryFile.reload();
				MessageFile.reload();
				SkillManager.setupValue();
				DebugManager.setDebug(BangHoi.plugin.getConfig().getBoolean("debug"));
				sendMessage(sender, "&aReloaded BangHoi");
				DebugManager.debug("RELOADING PLUGIN", "Plugin has been reloaded");
				return false;
			}
		}

		// resetall, delete, warnbanghoi, warnbanghoibyplayername, banghoiwar
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("resetall")) {
				if (!types.contains(args[1])) {
					sendMessage(sender, "&cType &e" + args[1] + "&c không hợp lệ");
					sendMessage(sender, "&fTypes hợp lệ: &ediem&f,&e warpoint&f,&e skill&f");
					return false;
				}

				if (!confirmCommand.contains(sender.getName())) {
					confirmCommand.add(sender.getName());
					sendMessage(sender, "&eVui lòng nhập lại lệnh một lần nữa để xác nhận");
					return false;
				}
				confirmCommand.remove(sender.getName());

				String type = args[1];
				sendMessage(sender, "&eTiến hành reset all " + type.toUpperCase());
				for (Entry<String, BangHoiData> data : DatabaseManager.bangHoiDatabase.entrySet()) {
					if (type.equalsIgnoreCase("diem") || type.equalsIgnoreCase("score")) {
						data.getValue().setBangHoiScore(0);
						DatabaseManager.bangHoi_diem.replace(data.getKey(), 0);
					}
					if (type.equalsIgnoreCase("warpoint"))
						data.getValue().setBangHoiWarPoint(0);
					if (type.equalsIgnoreCase("skill")) {
						data.getValue().setSkillLevel(SkillType.critDamage, 0);
						data.getValue().setSkillLevel(SkillType.boostScore, 0);
						data.getValue().setSkillLevel(SkillType.dodge, 0);
						data.getValue().setSkillLevel(SkillType.vampire, 0);
					}
					DatabaseManager.saveBangHoiData(data.getKey());
				}
				sendMessage(sender, "&aReset all &e" + type.toUpperCase() + " &athành công!");
				return false;
			}

			if (args[0].equalsIgnoreCase("delete")) {
				String bangHoi = args[1];
				if (!DatabaseManager.bangHoiDatabase.containsKey(args[1])) {
					sendMessage(sender, "&cBang hội &e" + bangHoi + " &ckhông tồn tại");
					return false;
				}

				if (!confirmCommand.contains(sender.getName())) {
					confirmCommand.add(sender.getName());
					sendMessage(sender, "&eVui lòng nhập lại lệnh một lần nữa để xác nhận");
					return false;
				}
				confirmCommand.remove(sender.getName());

				if (BangHoiManager.deleteBangHoi(bangHoi)) {
					sendMessage(sender, "&aXóa bang hội &e" + bangHoi + "&a thành công!");
				} else
					sendMessage(sender, "&cGặp lỗi trong quá trình xóa bang hội, vui lòng check console log!");
				return false;
			}

			if (args[0].equalsIgnoreCase("warnBangHoi")) {

				if (!DatabaseManager.bangHoiDatabase.containsKey(args[1])) {
					sendMessage(sender, "&cBang hội &e" + args[1] + " &ckhông tồn tại!");
					return false;
				}

				BangHoiManager.warnBangHoi(args[1]);
				return false;
			}

			if (args[0].equalsIgnoreCase("warnBangHoiByPlayerName")) {

				if (!DatabaseManager.playerDatabase.containsKey(args[1])) {
					sendMessage(sender, "&cDữ liệu của người chơi này không tồn tại");
					return false;
				}

				PlayerData data = DatabaseManager.getPlayerData(args[1]);
				if (data.getBangHoi() == null) {
					sendMessage(sender, "&cNgười chơi này không có bang hội!");
					return false;
				}
				BangHoiManager.warnBangHoi(data.getBangHoi());
				return false;
			}
			if (args[0].equalsIgnoreCase("banghoiwar")) {
				try {
					int time = Integer.parseInt(args[1]);
					sendMessage(sender, "&aĐã bắt đầu sự kiện/thay đổi thời gian sự kiện bang hội war trong &e" + time + " giây&f!");
					WarManager.runSK(sender, time);
					return false;
				} catch (Exception e) {
					sendMessage(sender, "&cVui lòng nhập số hợp lệ!");
					return false;
				}
			}
		}

		// reset
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("reset")) {

				String bangHoiName = args[1];
				String type = args[2];

				if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoiName)) {
					sendMessage(sender, "&cBang hội &e" + bangHoiName + " &ckhông tồn tại!");
					return false;
				}

				if (!types.contains(type)) {
					sendMessage(sender, "&cType &e" + type + "&c không hợp lệ");
					sendMessage(sender, "&fTypes hợp lệ: &ediem&f,&e warpoint&f,&e skill&f");
					return false;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(bangHoiName);

				if (type.equalsIgnoreCase("diem") || type.equalsIgnoreCase("score")) {
					bangHoiData.setBangHoiScore(0);
					DatabaseManager.bangHoi_diem.replace(bangHoiName, 0);
				}
				if (type.equalsIgnoreCase("warpoint"))
					bangHoiData.setBangHoiWarPoint(0);
				if (type.equalsIgnoreCase("skill")) {
					bangHoiData.setSkillLevel(SkillType.critDamage, 0);
					bangHoiData.setSkillLevel(SkillType.boostScore, 0);
					bangHoiData.setSkillLevel(SkillType.dodge, 0);
					bangHoiData.setSkillLevel(SkillType.vampire, 0);
				}
				DatabaseManager.saveBangHoiData(bangHoiName);

				sendMessage(sender, "&aReset &e" + type.toUpperCase() + "&a của bang hội &e" + bangHoiName + "&a thành công!");
				return false;
			}
		}

		// set, add, give, remove
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("set")
					|| args[0].equalsIgnoreCase("give")
					|| args[0].equalsIgnoreCase("remove")) {

				String bangHoiName = args[1];
				String type = args[2];

				if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoiName)) {
					sendMessage(sender, "&cBang hội &e" + bangHoiName + " &ckhông tồn tại!");
					return false;
				}

				if (!types.contains(type)) {
					sendMessage(sender, "&cType &e" + type + "&c không hợp lệ");
					sendMessage(sender, "&fTypes hợp lệ: &ediem&f,&e warpoint&f,&e skill&f");
					return false;
				}

				int value = 0;
				try {
					value = Integer.parseInt(args[3]);
				} catch (Exception e) {
					sendMessage(sender, "&cVui lòng nhập số hợp lệ!");
					return false;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(bangHoiName);

				if (args[0].equalsIgnoreCase("set")) {

					if (type.equalsIgnoreCase("skill")) {
						sendMessage(sender, "&eSử dụng sườn lệnh dưới đây để dùng cho set skill bang hội:");
						sendMessage(sender, "/banghoiad set <banghoi> skill <level skill 1> <level skill 2> <level skill 3> <level skill 4>");
						return false;
					}

					if (type.equalsIgnoreCase("diem") || type.equalsIgnoreCase("score")) {
						bangHoiData.setBangHoiScore(value);
						DatabaseManager.bangHoi_diem.put(bangHoiName, value);
					}

					if (type.equalsIgnoreCase("warpoint")) {
						bangHoiData.setBangHoiWarPoint(value);
					}
					DatabaseManager.saveBangHoiData(bangHoiName);
					sendMessage(sender, "&aĐã set &e" + type.toUpperCase() + " &acho bang hội &e" + bangHoiName + "&a thành công.");
					sendMessage(sender, "&aValue: &e" + value);
				}

				if (args[0].equalsIgnoreCase("give")) {

					if (type.equalsIgnoreCase("skill")) {
						sendMessage(sender, "&cKhông thể sử dụng type này cho give!");
						return false;
					}

					if (type.equalsIgnoreCase("diem") || type.equalsIgnoreCase("score")) {
						bangHoiData.addBangHoiScore(value);
						DatabaseManager.bangHoi_diem.put(bangHoiName, bangHoiData.getBangHoiScore() + value);
					}

					if (type.equalsIgnoreCase("warpoint")) {
						bangHoiData.addBangHoiWarPoint(value);
					}
					DatabaseManager.saveBangHoiData(bangHoiName);
					sendMessage(sender, "&aĐã cho &e" + type.toUpperCase() + " &avào bang hội &e" + bangHoiName + "&a thành công.");
					sendMessage(sender, "&aValue: &e" + value);
				}

				if (args[0].equalsIgnoreCase("remove")) {

					if (type.equalsIgnoreCase("skill")) {
						sendMessage(sender, "&cKhông thể sử dụng type này cho remove!");
						return false;
					}

					if (type.equalsIgnoreCase("diem") || type.equalsIgnoreCase("score")) {
						bangHoiData.removeBangHoiScore(value);
						DatabaseManager.bangHoi_diem.put(bangHoiName, bangHoiData.getBangHoiScore() - value);
					}

					if (type.equalsIgnoreCase("warpoint")) {
						bangHoiData.removeBangHoiWarPoint(value);
					}
					DatabaseManager.saveBangHoiData(bangHoiName);
					sendMessage(sender, "&aĐã trừ &e" + type.toUpperCase() + " &avào bang hội &e" + bangHoiName + "&a thành công.");
					sendMessage(sender, "&aValue: &e" + value);
				}
				return false;
			}
		}

		if (args.length == 7) {
			if (args[0].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("skill")) {

				String bangHoiName = args[1];

				if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoiName)) {
					sendMessage(sender, "&cBang hội &e" + bangHoiName + " &ckhông tồn tại!");
					return false;
				}

				try {
					Integer.parseInt(args[3]);
					Integer.parseInt(args[4]);
					Integer.parseInt(args[5]);
					Integer.parseInt(args[6]);
				} catch (Exception e) {
					sendMessage(sender, "&cVui lòng nhập số hợp lệ!");
					return false;
				}

				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(bangHoiName);
				sendMessage(sender, "&aĐã set skill cho bang hội &e" + bangHoiName + " &athành:");
				sendMessage(sender, "&aSkill 1 &b[Crit Damage]&a: &e" + args[3]);
				sendMessage(sender, "&aSkill 2 &b[BoostScore]&a: &e" + args[4]);
				sendMessage(sender, "&aSkill 3 &b[Dodge]&a: &e" + args[5]);
				sendMessage(sender, "&aSkill 4 &b[Vampire]&a: &e" + args[6]);

				bangHoiData.setSkillLevel(SkillType.critDamage, Integer.parseInt(args[3]));
				bangHoiData.setSkillLevel(SkillType.boostScore, Integer.parseInt(args[4]));
				bangHoiData.setSkillLevel(SkillType.dodge, Integer.parseInt(args[5]));
				bangHoiData.setSkillLevel(SkillType.vampire, Integer.parseInt(args[6]));

				DatabaseManager.saveBangHoiData(bangHoiName);
				return false;
			}
		}

		sendMessage(sender, "&cBang Hội Admin");
		sendMessage(sender, "&7" + BangHoi.plugin.getDescription().getVersion());
		sendMessage(sender, "");
		sendMessage(sender, "&e/banghoiad resetAll <type>");
		sendMessage(sender, "&e/banghoiad reset <banghoi> <type>");
		sendMessage(sender, "&e/banghoiad set <banghoi> <type> <value>");
		sendMessage(sender, "&e/banghoiad set <banghoi> skill <level skill 1> <level skill 2> <level skill 3> <level skill 4>");
		sendMessage(sender, "&e/banghoiad give <banghoi> <type> <value>");
		sendMessage(sender, "&e/banghoiad remove <banghoi> <type> <value>");
		sendMessage(sender, "&e/banghoiad delete <banghoi>");
		sendMessage(sender, "&e/banghoiad warnBangHoi <banghoi>");
		sendMessage(sender, "&e/banghoiad warnBangHoiByPlayerName <player>");
		sendMessage(sender, "&e/banghoiad bangHoiWar <time (giây)>");
		sendMessage(sender, "&e/banghoiad reload");
		sendMessage(sender, "");
		sendMessage(sender, "&fTypes hợp lệ: &ediem&f, &ewarpoint&f, &eskill");

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		List<String> completions = new ArrayList<>();
		List<String> commands = new ArrayList<>();

		if (args.length == 1) {
			commands.add("resetAll");
			commands.add("reset");
			commands.add("set");
			commands.add("give");
			commands.add("remove");
			commands.add("delete");
			commands.add("warnBangHoi");
			commands.add("warnBangHoiByPlayerName");
			commands.add("bangHoiWar");
			commands.add("reload");

			StringUtil.copyPartialMatches(args[0], commands, completions);
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("warnBangHoi")
					|| args[0].equalsIgnoreCase("delete")
					|| args[0].equalsIgnoreCase("set")
					|| args[0].equalsIgnoreCase("give")
					|| args[0].equalsIgnoreCase("remove")
					|| args[0].equalsIgnoreCase("reset")
					|| args[0].equalsIgnoreCase("resetAll"))
				if (!DatabaseManager.bangHoiDatabase.isEmpty())
					commands.addAll(DatabaseManager.bangHoiDatabase.keySet());
			if (args[0].equalsIgnoreCase("warnBangHoiByPlayerName")) {
				if (!DatabaseManager.playerDatabase.isEmpty()) {
					commands.addAll(DatabaseManager.playerDatabase.keySet());
				}
			}
			StringUtil.copyPartialMatches(args[1], commands, completions);
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set")
					|| args[0].equalsIgnoreCase("give")
					|| args[0].equalsIgnoreCase("remove"))
				commands.addAll(types);
			StringUtil.copyPartialMatches(args[2], commands, completions);
		}

		Collections.sort(completions);
		return completions;
	}

}