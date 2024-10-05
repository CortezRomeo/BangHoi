package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.api.event.WarEndEvent;
import com.cortezromeo.banghoi.api.event.WarStartEvent;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.util.StringUtil;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class WarManager {

	public static boolean runTask;
	public static boolean eventStarted;
	public static int eventTime;
	public static int maxEventTime;

	public static HashMap<Player, BossBar> bb = new HashMap<>();
	public static HashMap<String, Integer> top = new HashMap<>();
	public static HashMap<String, Integer> playerKills = new HashMap<>();
	public static HashMap<String, Double> playerDmgs = new HashMap<>();
	public static HashMap<String, Integer> playerDies = new HashMap<>();


	public static BossBar getBossBar(Player player) {

		if (!bb.containsKey(player))
			return null;

		return bb.get(player);

	}

	public static void createBossBar(Player player) {

		if (!eventStarted)
			return;

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (getBossBar(players) == null) {

				FileConfiguration config = BangHoi.plugin.getConfig();

				BossBar b = Bukkit.createBossBar(
						BangHoi.nms.addColor(config.getString("bang-hoi-war.bossbar.title").replace("%timeformat%",
								StringUtil.timeFormat(eventTime))),
						BarColor.valueOf(config.getString("bang-hoi-war.bossbar.color")),
						BarStyle.valueOf(config.getString("bang-hoi-war.bossbar.style")), new BarFlag[0]);
				b.setProgress((double) eventTime / (double) maxEventTime);
				b.addPlayer(players);
				b.setVisible(true);

				bb.put(players, b);

			}
		}
	}

	public static void sendMessageEventStatus(Player p) {

		FileConfiguration mse = MessageFile.get();

		if (eventStarted) {
			p.playSound(p.getLocation(),
					Sound.valueOf(BangHoi.plugin.getConfig().getString("bang-hoi-war.sound.name")),
					BangHoi.plugin.getConfig().getInt("bang-hoi-war.sound.volume"), 1);

			for (String string : MessageFile.get().getStringList("bangHoiWar.suKienDangBatDau")) {

				for (int i = 0; i < 10; i++) {
					string = string.replace("%top_" + i + "_name%", BangHoiManager.getBangHoiName(getTopBangHoiName(i)));
					string = string.replace("%top_" + i + "_score%",
							String.valueOf(getTopBangHoiScore(i)));
				}
				string = string.replace("%timeformat%", StringUtil.timeFormat(eventTime));

				p.sendMessage(BangHoi.nms.addColor(mse.getString("bangHoiWar.PREFIX") + string));
			}
		} else {
			for (String str : mse.getStringList("bangHoiWar.suKienChuaBatDau")) {
				str = str.replace("%number%",
						String.valueOf(BangHoi.plugin.getConfig().getInt("bang-hoi-war.so-nguoi-choi-toi-thieu")));

				p.sendMessage(BangHoi.nms.addColor(mse.getString("bangHoiWar.PREFIX") + str));
			}
		}
	}

	public static void runTask(int eventTime) {
		WarManager.eventTime = eventTime;

		if (!runTask) {
			runTask = true;

			new BukkitRunnable() {

				@Override
				public void run() {

					if (!runTask) {
						cancel();
						return;
					}

					Date d = new Date();
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					String strDate = dateFormat.format(d);

					List<String> khungGio = BangHoi.plugin.getConfig().getStringList("bang-hoi-war.khung-gio");
					for (String kg : khungGio) {
						if (strDate.equals(kg) && !eventStarted) {

							if (Bukkit.getOnlinePlayers().size() < BangHoi.plugin.getConfig()
									.getInt("bang-hoi-war.so-nguoi-choi-toi-thieu")) {
								Bukkit.broadcastMessage(BangHoi.nms.addColor(MessageFile.get().getString("bangHoiWar.PREFIX")
										+ MessageFile.get().getString("bangHoiWar.khongDuNguoi")));
							} else {
								cancel();
								runTask = false;

								runSK(Bukkit.getConsoleSender(), eventTime);
								WarManager.runTask(eventTime);
								WarStartEvent event = new WarStartEvent(eventTime);
								Bukkit.getServer().getPluginManager().callEvent(event);
								return;
							}
						}
					}
				}
			}.runTaskTimer(BangHoi.plugin, 0, 20);
		}
	}

	public static void runSK(CommandSender sender, int eventTime) {

		WarManager.eventTime = eventTime;
		WarManager.maxEventTime = eventTime;

		FileConfiguration mse = MessageFile.get();
		FileConfiguration config = BangHoi.plugin.getConfig();

		if (eventStarted) {
			eventBoardCast(mse.getString("bangHoiWar.thayDoiThoiGian").replace("%player%", sender.getName())
					.replace("%timeformat%", StringUtil.timeFormat(eventTime)));
			return;
		} else {
			eventStarted = true;

			for (String str : mse.getStringList("bangHoiWar.suKienBatDau")) {
				str = str.replace("%timeformat%", StringUtil.timeFormat(eventTime));
				eventBoardCast(str);
			}

			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.valueOf(config.getString("bang-hoi-war.sound.name")),
						config.getInt("bang-hoi-war.sound.volume"), 1);
				createBossBar(player);
				for (String command : config.getStringList("bang-hoi-war.commands"))
					dispatchCommand(player, command);
			}
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				if (WarManager.eventTime <= 0 || !eventStarted) {
					for (String str : mse.getStringList("bangHoiWar.suKienKetThuc")) {

						for (int i = 0; i < 10; i++) {
							str = str.replace("%top_" + i + "_name%", BangHoiManager.getBangHoiName(WarManager.getTopBangHoiName(i)));
							str = str.replace("%top_" + i + "_score%",
									String.valueOf(WarManager.getTopBangHoiScore(i)));

							str = str.replace("%topdmg_" + i + "_name%", WarManager.getTopDmgName(i));
							str = str.replace("%topdmg_" + i + "_score%",
									String.valueOf(Math.round(WarManager.getTopDmgScore(i))));
						}
						eventBoardCast(str);
					}

					WarEndEvent event = new WarEndEvent(top, playerKills, playerDmgs, playerDies);
					Bukkit.getServer().getPluginManager().callEvent(event);

					for (int i = 0; i < 10; i++) {
						String bangHoi = WarManager.getTopBangHoiName(i);
						if (!bangHoi.equals("<không có>")) {
							if (i == 1) {
								BangHoiManager.addWarPoint(bangHoi, 30);
							}
							if (i == 2) {
								BangHoiManager.addWarPoint(bangHoi, 20);
							}
							if (i == 3) {
								BangHoiManager.addWarPoint(bangHoi, 10);
							}
							if (i == 4 || i == 5) {
								BangHoiManager.addWarPoint(bangHoi, 5);
							}
						}
					}

					for (Player players : Bukkit.getOnlinePlayers()) {
						players.playSound(players.getLocation(), XSound.ENTITY_DRAGON_FIREBALL_EXPLODE.parseSound(), 1, 2);
						if (getBossBar(players) != null) {
							BossBar b = WarManager.getBossBar(players);

							b.removeAll();
							WarManager.bb.remove(players);
						}
					}

					eventStarted = false;
					top.clear();
					cancel();
					return;
				}

				if (WarManager.eventTime > 0) {
					WarManager.eventTime = WarManager.eventTime - 1;

					for (Player players : Bukkit.getOnlinePlayers()) {
						if (getBossBar(players) != null) {

							BossBar b = getBossBar(players);
							b.setProgress((double) WarManager.eventTime / (double) WarManager.maxEventTime);
							b.setTitle(BangHoi.nms.addColor(config.getString("bang-hoi-war.bossbar.title")
									.replace("%timeformat%", StringUtil.timeFormat(WarManager.eventTime))));

							if (WarManager.eventTime <= 15) {
								if (b.getColor() == BarColor.RED)
									b.setColor(BarColor.YELLOW);
								else
									b.setColor(BarColor.RED);
							}
						}
					}
				}

			}
		}.runTaskTimer(BangHoi.plugin, 0, 20);
	}

	public static boolean inWarWorld(Player player) {
		if (player.getWorld().getName().equals(BangHoi.plugin.getConfig().getString("bang-hoi-war.world")))
			return true;
		for (String worldName : BangHoi.plugin.getConfig().getStringList("bang-hoi-war.multiple-worlds"))
			player.getWorld().getName().equals(worldName);
		return false;
	}

	public static String getTopBangHoiName(int top) {
		String NA = "<không có>";

		if (!eventStarted)
			return NA;

		Map<String, Integer> sortedMap = WarManager.top.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		int i = 0;
		for (Map.Entry<String, Integer> en : sortedMap.entrySet()) {
			if (WarManager.top.get(en.getKey()) == null)
				continue;
			++i;

			if (i == top)
				return en.getKey();

		}

		return NA;
	}

	public static String getTopDmgName(int top) {

		String NA = "<không có>";

		if (!eventStarted)
			return NA;

		Map<String, Double> sortedMap = WarManager.playerDmgs.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		int i = 0;
		for (Map.Entry<String, Double> en : sortedMap.entrySet()) {
			if (WarManager.playerDmgs.get(en.getKey()) == null)
				continue;
			++i;

			if (i == top)
				return en.getKey();

		}

		return NA;
	}

	public static int getTopBangHoiScore(int top) {

		if (!eventStarted || WarManager.top.get(getTopBangHoiName(top)) == null)
			return 0;

		return WarManager.top.get(getTopBangHoiName(top));
	}

	public static double getTopDmgScore(int top) {

		if (!eventStarted || WarManager.playerDmgs.get(getTopDmgName(top)) == null)
			return 0;

		return WarManager.playerDmgs.get(getTopDmgName(top));
	}

	public static void eventBoardCast(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(BangHoi.nms.addColor(MessageFile.get().getString("bangHoiWar.PREFIX") + message));
		}
	}

	public static void dispatchCommand(Player player, String command) {
		String MATCH = "(?ium)^(player:|op:|console:|)(.*)$";
		new BukkitRunnable() {
			@Override
			public void run() {
				final String type = command.replaceAll(MATCH, "$1").replace(":","").toLowerCase();
				final String cmd = command.replaceAll(MATCH, "$2").replaceAll("(?ium)([{]Player[}])", player.getName());
				switch (type){
					case "op":
						if(player.isOp()){
							player.performCommand(cmd);
						} else {
							player.setOp(true);
							player.performCommand(cmd);
							player.setOp(false);
						}
						break;
					case "":
					case "player":
						player.performCommand(cmd);
						break;
					case "console":
					default:
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
						break;
				}
			}
		}.runTask(BangHoi.plugin);
	}

}
