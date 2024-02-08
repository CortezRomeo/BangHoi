package com.cortezromeo.banghoi.command;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class BangHoiAdminCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private BangHoi plugin;

	private List<String> confirmCommand = new ArrayList<>();

	public BangHoiAdminCommand(BangHoi plugin) {
		this.plugin = plugin;
		plugin.getCommand("banghoiadmin").setExecutor((CommandExecutor) this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (sender instanceof Player) {

			Player p = (Player) sender;

			if (!p.hasPermission("banghoi.admin")) {
				MessageUtil.sendMessage(p, MessageFile.get().getString("noPermission"));
				return false;
			}
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("resetdiem")) {

				if (!confirmCommand.contains(sender.getName())) {
					confirmCommand.add(sender.getName());
					sender.sendMessage("Nhập lại lệnh một lần nữa để xác nhận!");
					return false;
				}
				confirmCommand.remove(sender.getName());

				for (Entry<String, BangHoiData> data : DatabaseManager.bangHoiDatabase.entrySet()) {
					data.getValue().setBangHoiScore(0);
					DatabaseManager.bangHoi_diem.replace(data.getKey(), 0);
				}
				sender.sendMessage("Reset điểm bang hội thành công!");
				return false;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				BangHoi.plugin.reloadConfig();
				InventoryFile.reload();
				MessageFile.reload();
				sender.sendMessage("Reload thành công!");
				return false;
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("warnBangHoi")) {
				BangHoiManager.warnBangHoi(args[1]);
				return false;
			}
			if (args[0].equalsIgnoreCase("warnBangHoiByPlayerName")) {

				if (!DatabaseManager.playerDatabase.containsKey(args[1])) {
					sender.sendMessage("Dữ liệu của người chơi này không tồn tại");
					return false;
				}

				PlayerData data = DatabaseManager.getPlayerData(args[1]);
				if (data.getBangHoi() == null) {
					sender.sendMessage("Người chơi này không có bang hội!");
					return false;
				}

				BangHoiManager.warnBangHoi(data.getBangHoi());

				return false;
			}
			if (args[0].equalsIgnoreCase("banghoiwar")) {
				try {

					int time = Integer.parseInt(args[1]);
					sender.sendMessage("Đã bắt đầu sự kiện/thay đổi thời gian sự kiện bang hội war trong " + time + " giây!");
					WarManager.runSK(sender, time);

					return false;
				} catch (Exception e) {
					sender.sendMessage("Vui lòng nhập số!");
					return false;
				}
			}
		}

		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("givewarpoint")) {

				String bangHoi = args[1];

				if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi)) {
					sender.sendMessage("Bang hội không tồn tại!");
					return false;
				}

				try {
					int warPoint = Integer.parseInt(args[2]);
					DatabaseManager.bangHoiDatabase.get(bangHoi).addWarPoint(warPoint);
					sender.sendMessage("Add thành công " + args[1] + " cho bang hội " + args[2] + " thành công");
				} catch (Exception e) {
					sender.sendMessage("Vui lòng nhập số hợp lệ!");
				}
				return false;
			}
		}

		if (args.length == 5) {
			if (args[0].equalsIgnoreCase("skill")) {
				if (!(sender instanceof Player)) {
					return false;
				}

				if (DatabaseManager.playerDatabase.get(sender.getName()).getBangHoi() == null) {
					return false;
				}
				BangHoiData bangHoiData = DatabaseManager.getBangHoiData(DatabaseManager.getPlayerData(sender.getName()).getBangHoi());

				try {
					//bangHoiData.setWarPoint(500);
					bangHoiData.setSkillLevel(1, Integer.parseInt(args[1]));
					bangHoiData.setSkillLevel(2, Integer.parseInt(args[2]));
					bangHoiData.setSkillLevel(3, Integer.parseInt(args[3]));
					bangHoiData.setSkillLevel(4, Integer.parseInt(args[4]));

					for (int i = 1; i <= 4; i++)
						sender.sendMessage("Skill " + i + ": " + args[i]);
					return false;
				} catch (Exception e) {
					sender.sendMessage("Sai input");
					return false;
				}
			}
		}

		sender.sendMessage("/banghoiad resetdiem - Reset diểm của toàn bang hội");
		sender.sendMessage("/banghoiad skill <skill1> <skill2> <skill3> <skill4>");
		sender.sendMessage("/banghoiad givewarpoint <banghoi> <warpoint>");
		sender.sendMessage("/banghoiad warnBangHoi <banghoi>");
		sender.sendMessage("/banghoiad warnBangHoiByPlayerName <playerName>");
		sender.sendMessage("/banghoiad bangHoiWar <time (giây)>");
		sender.sendMessage("/banghoiad reload");

		return false;
	}

}