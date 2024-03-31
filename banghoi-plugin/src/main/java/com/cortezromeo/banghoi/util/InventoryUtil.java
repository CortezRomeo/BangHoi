package com.cortezromeo.banghoi.util;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.UpgradeFile;
import com.cortezromeo.banghoi.manager.BangHoiManager;
import com.cortezromeo.banghoi.manager.DatabaseManager;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryUtil {

    public static ItemStack getItem(String type, String value, short itemData, String name, List<String> lore, String replace,
                                    String dataReplace, int dataReplace2) {

        AtomicReference<ItemStack> material = new AtomicReference<>(new ItemStack(Material.BEDROCK));

        if (type.equalsIgnoreCase("customhead") || type.equalsIgnoreCase("playerhead"))
            material.set(BangHoi.nms.getHeadItem(value));

        if (type.equalsIgnoreCase("material"))
            material.set(BangHoi.nms.createItemStack(value, 1, itemData));

        ItemMeta materialMeta = material.get().getItemMeta();

        if (replace.equals("bangHoiInfo")) {
            BangHoiData data = DatabaseManager.getBangHoiData(dataReplace);
            name = name.replace("%ten%", BangHoiManager.getBangHoiName(data.getBangHoiName()));
        }

        if (replace.equals("thanhVienInfo"))
            name = name.replace("%name%", dataReplace);

        materialMeta.setDisplayName(BangHoi.nms.addColor(name));

        List<String> newList = new ArrayList<String>();
        if (replace.equals("bangHoiInfo"))
            newList.add(BangHoi.nms.addColor("&8:" + dataReplace));

        for (String string : lore) {

            if (replace.equals("bangHoiInfo")) {

                BangHoiData data = DatabaseManager.getBangHoiData(dataReplace);

                Date currentDate = new Date(data.getNgayThanhLap());
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                String strDate = dateFormat.format(currentDate);

                string = string.replace("%ten%", data.getBangHoiName());
                string = string.replace("%top%", String.valueOf(dataReplace2));
                string = string.replace("%tencustom%",
                        (data.getTenCustom() == null ? "&7Không có" : data.getTenCustom()));
                string = string.replace("%lanhdao%", data.getBangHoiFounder());
                string = string.replace("%diem%", String.valueOf(data.getBangHoiScore()));
                string = string.replace("%warn%", String.valueOf(data.getBangHoiWarn()));
                string = string.replace("%warpoint%", String.valueOf(data.getBangHoiWarPoint()));
                string = string.replace("%ngaythanhlap%", strDate);
                string = string.replace("%sothanhvien%", String.valueOf(data.getThanhVien().size()));
                string = string.replace("%thanhvientoida%", String.valueOf(data.getSoLuongToiDa()));

            }

            if (replace.equals("thanhVienInfo")) {
                PlayerData data = DatabaseManager.getPlayerData(dataReplace);

                Date currentDate = new Date(data.getNgayThamGia());
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                String strDate = dateFormat.format(currentDate);

                string = string.replace("%trangthai%",
                        (Bukkit.getPlayer(dataReplace) != null ? "&aONLINE" : "&cOFFLINE"));
                if (data.getChucVu() != null)
                    string = string.replace("%chucvu%", (data.getChucVu().equals("Leader") ? "&6Lãnh đạo" : "&aMember"));
                string = string.replace("%ngaythamgia%", strDate);

            }

            if (replace.equals("upgrade")) {

                FileConfiguration upgradeFileCfg = UpgradeFile.get();

                int slot = DatabaseManager.getBangHoiData(dataReplace).getSoLuongToiDa();
                int requiredPoint = upgradeFileCfg
                        .getInt("maxMemberSlot.playerPoints." + (DatabaseManager.getBangHoiData(dataReplace).getSoLuongToiDa() + 1));
                if (requiredPoint == 0)
                    requiredPoint = upgradeFileCfg.getInt("maxMemberSlot.playerPoints.else");

                int requiredWarPoint = upgradeFileCfg
                        .getInt("maxMemberSlot.warPoint." + (DatabaseManager.getBangHoiData(dataReplace).getSoLuongToiDa() + 1));
                if (requiredWarPoint == 0)
                    requiredWarPoint = upgradeFileCfg.getInt("maxMemberSlot.warPoint.else");

                string = string.replace("%slot%", String.valueOf(slot));
                string = string.replace("%xu%", String.valueOf(requiredPoint));
                string = string.replace("%warpoint%", String.valueOf(requiredWarPoint));

            }

            if (replace.equals("warPointShop")) {
                string = string.replace("%skill1WarPoint%", String.valueOf(BangHoiManager.getWarPointCost(1, 1)));
                string = string.replace("%skill2WarPoint%", String.valueOf(BangHoiManager.getWarPointCost(2, 1)));
                string = string.replace("%skill3WarPoint-level1%", String.valueOf(BangHoiManager.getWarPointCost(3, 1)));
                string = string.replace("%skill3WarPoint-level2%", String.valueOf(BangHoiManager.getWarPointCost(3, 2)));
                string = string.replace("%skill4WarPoint%", String.valueOf(BangHoiManager.getWarPointCost(4, 1)));
            }

            newList.add(BangHoi.nms.addColor(string));
        }
        materialMeta.setLore(newList);

        material.get().setItemMeta(materialMeta);
        return material.get();

    }

}
