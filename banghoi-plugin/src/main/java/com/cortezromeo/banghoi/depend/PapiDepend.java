package com.cortezromeo.banghoi.depend;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PapiDepend extends PlaceholderExpansion {
	public String getAuthor() {
		return "Cortez Romeo";
	}

	public String getIdentifier() {
		return "banghoi";
	}

	public String getVersion() {
		return "1.0";
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null) {
			return "";
		}

		if (identifier.startsWith("top_")) {

			int top = Integer.parseInt(identifier.replace("top_", ""));
			if (top <= 0)
				return "-";

			for (int i = 1; i < top + 1; i++) {
				Map<String, Integer> sortedMap = DatabaseManager.bangHoi_diem.entrySet().stream()
						.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors
								.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				int amount = 0;
				for (Map.Entry<String, Integer> en : sortedMap.entrySet()) {
					amount++;

					if (amount == top)
						return BangHoi.nms.addColor(BangHoi.plugin.getConfig().getString("placeholderAPI.top")
								.replaceAll("%number%", String.valueOf(top))
								.replaceAll("%name%", BangHoiManager.getBangHoiName(en.getKey()))
								.replaceAll("%value%", String.valueOf(en.getValue())));
				}
			}
		}

		// Player data

		PlayerData pData = DatabaseManager.getPlayerData(player.getName());
		if (pData.getBangHoi() == null)
			return "";

		if (identifier.equals("originalname"))
			return BangHoi.nms.addColor(pData.getBangHoi());

		if (identifier.equals("name"))
			return BangHoi.nms.addColor(BangHoiManager.getBangHoiName(pData.getBangHoi()));

		if (identifier.equals("chucvu"))
			return BangHoi.nms.addColor(pData.getChucVu());

		if (identifier.equals("ngaythamgia"))
			return BangHoi.nms.addColor(String.valueOf(pData.getNgayThamGia()));

		if (identifier.equals("diem"))
			return BangHoi.nms.addColor(String.valueOf(DatabaseManager.getBangHoiData(pData.getBangHoi()).getBangHoiScore()));

		if (identifier.equals("warpoint"))
			return BangHoi.nms.addColor(String.valueOf(DatabaseManager.getBangHoiData(pData.getBangHoi()).getBangHoiWarPoint()));

		if (identifier.equals("warn"))
			return BangHoi.nms.addColor(String.valueOf(DatabaseManager.getBangHoiData(pData.getBangHoi()).getBangHoiWarn()));

		if (identifier.equals("ngaythanhlap"))
			return BangHoi.nms.addColor(String.valueOf(DatabaseManager.getBangHoiData(pData.getBangHoi()).getNgayThanhLap()));

		if (identifier.equals("thanhvien"))
			return BangHoi.nms.addColor(String.valueOf(DatabaseManager.getBangHoiData(pData.getBangHoi()).getThanhVien()));

		return "";
	}
}