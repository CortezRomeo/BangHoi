package com.cortezromeo.banghoi.command;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.ClanRank;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.inventory.ListBangHoiInventory;
import com.cortezromeo.banghoi.inventory.UpgradeInventory;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.manager.WarManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BangHoiCommand implements CommandExecutor, TabExecutor {
	@SuppressWarnings("unused")
	private BangHoi plugin;
	private List<String> DeleteingConfirmPlayers = new ArrayList<>();

	public BangHoiCommand(BangHoi plugin) {
		this.plugin = plugin;
		plugin.getCommand("banghoi").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("/banghoi chỉ dành cho player. Hãy xài /banghoiad");
			return false;
		}

		Player p = (Player) sender;

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("roi")) {
				BangHoiManager.leaveBangHoi(p);
				return false;
			} else if (args[0].equalsIgnoreCase("disband") || args[0].equalsIgnoreCase("delete")
					|| args[0].equalsIgnoreCase("xoa")) {

				if (!DeleteingConfirmPlayers.contains(p.getName())) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("xacNhanLenh"));
					DeleteingConfirmPlayers.add(p.getName());
					return false;
				} else {
					BangHoiManager.disbandBangHoi(p);
					DeleteingConfirmPlayers.remove(p.getName());
				}
				return false;
			} else if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("yes")
					|| args[0].equalsIgnoreCase("chapnhan")) {
				BangHoiManager.acceptBangHoi(p);
				return false;
			} else if (args[0].equalsIgnoreCase("list")) {
				ListBangHoiInventory.openInventory(p);
				return false;
			} else if (args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("reject")
					|| args[0].equalsIgnoreCase("huy")) {
				BangHoiManager.denyBangHoi(p);
				return false;
			} else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("thongtinbanghoi")
					|| args[0].equalsIgnoreCase("thongtin")) {

				PlayerData data = DatabaseManager.getPlayerData(p.getName());

				if (data.getBangHoi() == null) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return false;
				}

				BangHoiManager.viewInfo(p, data.getBangHoi());
				return false;

			} else if (args[0].equalsIgnoreCase("event") || args[0].equalsIgnoreCase("war")
					|| args[0].equalsIgnoreCase("wars") || args[0].equalsIgnoreCase("sukien")) {
				WarManager.sendMessageEventStatus(p);
				return false;
			} else if (args[0].equals("chat")) {
				BangHoiManager.toggleChat(p);
				return false;
			} else if (args[0].equals("upgrade") || args[0].equalsIgnoreCase("up")
					|| args[0].equalsIgnoreCase("nangcap")) {

				PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
				if (playerData.getBangHoi() == null) {
					MessageUtil.sendMessage(p, MessageFile.get().getString("khongCoBangHoi"));
					return false;
				}

				p.openInventory(UpgradeInventory.inventory(p));
				return false;
			}
		}

		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("setcustomname")) {

				StringBuilder builder = new StringBuilder();
				for (int i = 1; i < args.length; i++)
					builder.append(args[i]).append(" ");
				builder.deleteCharAt(builder.length() - 1);

				String customName = builder.toString();
				BangHoiManager.setCustomName(p, customName);
				return false;
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("tao")) {
				BangHoiManager.createBangHoi(p, args[1]);
				return false;
			} else if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("moi")) {
				BangHoiManager.inviteBangHoi(p, Bukkit.getPlayer(args[1]));
				return false;
			} else if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("duoi")) {
				BangHoiManager.kickBangHoi(p, args[1]);
				return false;
			} else if (args[0].equalsIgnoreCase("setleader") || args[0].equalsIgnoreCase("chuyenquyen")) {
				BangHoiManager.setLeaderBangHoi(p, args[1]);
				return false;
			} else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("thongtinbanghoi")
					|| args[0].equalsIgnoreCase("thongtin")) {
				BangHoiManager.viewInfo(p, args[1]);
				return false;
			}  else if (args[0].equalsIgnoreCase("seticon")) {
				BangHoiManager.setBangHoiIcon(p, args[1]);
				return false;
			} else if (args[0].equalsIgnoreCase("setmanager")) {
				BangHoiManager.setManager(p, args[1]);
				return false;
			} else if (args[0].equalsIgnoreCase("removemanager")) {
				BangHoiManager.removeManager(p, args[1]);
				return false;
			}
		}

		PlayerData data = DatabaseManager.getPlayerData(p.getName());
		if (data.getBangHoi() == null)
			for (String str : MessageFile.get().getStringList("lenhChinh.guest"))
				p.sendMessage(BangHoi.nms.addColor(str));
		else if (data.getChucVu().equals(ClanRank.LEADER))
			for (String str : MessageFile.get().getStringList("lenhChinh.leader"))
				p.sendMessage(BangHoi.nms.addColor(str));
		else if (data.getChucVu().equals(ClanRank.MANAGER))
			for (String str : MessageFile.get().getStringList("lenhChinh.manager"))
				p.sendMessage(BangHoi.nms.addColor(str));
		else
			for (String str : MessageFile.get().getStringList("lenhChinh.member"))
				p.sendMessage(BangHoi.nms.addColor(str));
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return null;
		}

		List<String> completions = new ArrayList<>();
		List<String> commands = new ArrayList<>();

		Player p = (Player) sender;

		PlayerData playerData = DatabaseManager.getPlayerData(p.getName());

		if (args.length == 1) {
			commands.add("info");
			commands.add("war");
			commands.add("list");

			if (playerData.getBangHoi() != null) {
				commands.add("chat");
				commands.add("upgrade");
				commands.add("leave");
			} else {
				commands.add("create");
				commands.add("accept");
				commands.add("deny");
			}

			if (playerData.getChucVu() != null && !playerData.getChucVu().equals(ClanRank.MEMBER)) {
				commands.add("invite");
				commands.add("kick");
				if (playerData.getChucVu().equals(ClanRank.LEADER)) {
					commands.add("setcustomname");
					commands.add("setleader");
					commands.add("delete");
					commands.add("seticon");
					commands.add("setmanager");
					commands.add("removemanager");
				}
			}

			StringUtil.copyPartialMatches(args[0], commands, completions);
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("info"))
				if (!DatabaseManager.bangHoiDatabase.isEmpty())
					commands.addAll(DatabaseManager.bangHoiDatabase.keySet());

			if (playerData.getChucVu() != null && !playerData.getChucVu().equals(ClanRank.MEMBER)) {
				if (args[0].equalsIgnoreCase("kick")) {
					List<String> members = DatabaseManager.getBangHoiData(playerData.getBangHoi()).getThanhVien();
					commands.addAll(members);
				}

				if (args[0].equalsIgnoreCase("invite"))
					for (Player player : Bukkit.getOnlinePlayers())
						commands.add(player.getName());

				if (playerData.getChucVu().equals(ClanRank.LEADER)) {
					if (args[0].equalsIgnoreCase("seticon")) {
						for (Material material : Material.values()) {
							commands.add(material.toString());
						}
					}
					if (args[0].equalsIgnoreCase("setleader") || args[0].equalsIgnoreCase("removemanager") || args[0].equalsIgnoreCase("setmanager")) {
						List<String> managers = DatabaseManager.getBangHoiData(playerData.getBangHoi()).getManagers();
						commands.addAll(managers);
					}
				}
			}
			StringUtil.copyPartialMatches(args[1], commands, completions);
		}

		Collections.sort(completions);
		return completions;
	}

}