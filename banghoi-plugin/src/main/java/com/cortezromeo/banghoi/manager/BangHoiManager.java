package com.cortezromeo.banghoi.manager;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.depend.VaultDepend;
import com.cortezromeo.banghoi.file.MessageFile;
import com.cortezromeo.banghoi.storage.banghoidata.BangHoiData;
import com.cortezromeo.banghoi.storage.playerdata.PlayerData;
import com.cortezromeo.banghoi.util.MessageUtil;
import com.cryptomorin.xseries.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BangHoiManager {

    private static FileConfiguration mse = MessageFile.get();

    public static String getBangHoiName(String bangHoi) {

        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi))
            return "";

        BangHoiData bangHoiData = DatabaseManager.getBangHoiData(bangHoi);

        if (bangHoiData.getTenCustom() == null)
            return bangHoiData.getBangHoiName();

        return bangHoiData.getTenCustom();

    }

    public static void bangHoiAlert(String bangHoi, String message) {

        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi))
            return;

        BangHoiData data = DatabaseManager.getBangHoiData(bangHoi);

        for (String str : data.getThanhVien())
            if (Bukkit.getPlayer(str) != null) {
                Player p = Bukkit.getPlayer(str);
                MessageUtil.sendMessage(p, "");
                p.sendMessage(BangHoi.nms.addColor(
                        mse.getString("thongBaoRieng.PREFIX").replace("%name%", getBangHoiName(bangHoi))
                                + message));
            }
    }

    public static void createBangHoi(Player p, String bangHoiName) {

        if (bangHoiName == null) {
            MessageUtil.sendMessage(p, mse.getString("tenBangHoiKhongHopLe"));
            return;
        }

        FileConfiguration config = BangHoi.plugin.getConfig();
        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());

        if (playerData.getBangHoi() != null) {
            MessageUtil.sendMessage(p, mse.getString("daCoBangHoi"));
            return;
        }

        if (VaultDepend.econ.getBalance(p) < config.getInt("bang-hoi-options.tao-bang-hoi")) {
            MessageUtil.sendMessage(p, mse.getString("khongDuTien").replace("%money%",
                    String.valueOf(config.getInt("bang-hoi-options.tao-bang-hoi"))));
            return;
        }

        if (DatabaseManager.bangHoiDatabase.containsKey(bangHoiName)) {
            MessageUtil.sendMessage(p, mse.getString("bangHoiDaTonTai").replace("%banghoi%", bangHoiName));
            return;
        }

        List<String> blacklist_chars = BangHoi.plugin.getConfig().getStringList("bang-hoi-options.ky-tu-cam");
        blacklist_chars.add("/");
        blacklist_chars.add("\\");
        blacklist_chars.add(":");
        blacklist_chars.add("*");
        blacklist_chars.add("?");
        blacklist_chars.add("\"");
        blacklist_chars.add(">");
        blacklist_chars.add("<");
        blacklist_chars.add("|");
        for (String chars : blacklist_chars)
            if (bangHoiName.contains(chars)) {
                for (String str : mse.getStringList("tenCoKyTuKhongHopLe"))
                    MessageUtil.sendMessage(p, str);
                return;
            }

        List<String> blacklist_name = BangHoi.plugin.getConfig().getStringList("bang-hoi-options.ten-cam");
        for (String bn : blacklist_name) {
            if (StringUtils.containsIgnoreCase(bangHoiName, bn)) {
                MessageUtil.sendMessage(p, mse.getString("tenKhongHopLe"));
                return;
            }
        }

        String name_removeColor = ChatColor.stripColor(bangHoiName);
        int min_length = config.getInt("bang-hoi-options.so-ky-tu-toi-thieu");
        int max_length = config.getInt("bang-hoi-options.so-ky-tu-toi-da");

        if (name_removeColor.length() < min_length) {
            MessageUtil.sendMessage(p, mse.getString("tenQuaNgan").replace("%number%", String.valueOf(min_length)));
            return;
        }

        if (name_removeColor.length() > max_length) {
            MessageUtil.sendMessage(p, mse.getString("tenQuaDai").replace("%number%", String.valueOf(max_length)));
            return;
        }

        //DatabaseManager.loadBangHoiData(bangHoiName);
        //BangHoiData BangHoiData = DatabaseManager.getBangHoiData(bangHoiName);

        Date d = new Date();
        long dateLong = d.getTime();
        List<String> members = new ArrayList<>();
        members.add(p.getName());

        BangHoiData bangHoiData = new BangHoiData(bangHoiName, null, p.getName()
                , 0, 0, 0, BangHoi.plugin.getConfig().getInt("bang-hoi-options.max-thanh-vien-mac-dinh"),
                dateLong, members, null
        , 0, 0, 0, 0);
        DatabaseManager.bangHoiDatabase.put(bangHoiName, bangHoiData);
        DatabaseManager.bangHoi_diem.put(bangHoiName, 0);

        playerData.setBangHoi(bangHoiName);
        playerData.setChucVu("Leader");
        playerData.setNgayThamGia(dateLong);

        DatabaseManager.saveBangHoiData(bangHoiName);
        DatabaseManager.savePlayerData(p.getName());

        VaultDepend.econ.withdrawPlayer(p, config.getInt("bang-hoi-options.tao-bang-hoi"));
        MessageUtil.sendMessage(p, mse.getString("taoBangHoi").replace("%name%", bangHoiName));

        DatabaseManager.bangHoiInvitingPlayers.remove(p.getName());

        DebugManager.debug("CREATING BANG HOI", bangHoiName + " has been created");
        DebugManager.debug("CREATING BANG HOI", "leader: " + p.getName());
        Date currentDate = new Date(dateLong);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String strDate = dateFormat.format(currentDate);
        DebugManager.debug("CREATING BANG HOI", "date: " + dateLong + " (" + strDate + ")");
    }

    public static void warnBangHoi(String bangHoiName) {

        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoiName)) {
            return;
        }

        BangHoiData data = DatabaseManager.getBangHoiData(bangHoiName);

        if (data.getBangHoiWarn() == 0) {

            MessageUtil.sendBroadCast("&cXỬ PHẠT BANG HỘI > &fBang hội " + getBangHoiName(bangHoiName) + "&f đã bị &c1 WARN&f.");
            bangHoiAlert(bangHoiName,
                    "&fBang hội của bạn vừa bị &c&l1 WARN&f và đã bị reset điểm, thêm một warn nữa sẽ bị xóa bang hội!");
            data.setBangHoiWarn(1);
            data.setBangHoiScore(0);

            DatabaseManager.saveBangHoiData(bangHoiName);

            return;
        }

        if (data.getBangHoiWarn() >= 1) {
            MessageUtil.sendBroadCast("&cXỬ PHẠT BANG HỘI > &fBang hội " + getBangHoiName(bangHoiName) + "&f đã bị giải tán vì có &c2 WARN&f.");
            bangHoiAlert(bangHoiName, "&fBang hội đã bị giải tán vì có &c&l2 WARN&f.");
            deleteBangHoi(bangHoiName);
        }

    }

    public static void addWarPoint(String bangHoi, int warPoint) {
        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi))
            return;

        DatabaseManager.getBangHoiData(bangHoi).addWarPoint(warPoint);
        BangHoiManager.bangHoiAlert(bangHoi,
                MessageFile.get().getString("thongBaoRieng.congWarpoint").replace("%warpoint%", String.valueOf(warPoint)));
        DatabaseManager.saveBangHoiData(bangHoi);
    }

    public static void leaveBangHoi(Player p) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());

        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (playerData.getChucVu() != null)
            if (playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("leaderKhongTheRoi").replaceAll("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        BangHoiData BangHoiData = DatabaseManager.getBangHoiData(playerData.getBangHoi());
        String bangHoi = BangHoiData.getBangHoiName();
        BangHoiData.removeThanhVien(p.getName());

        playerData.setBangHoi(null);
        playerData.setChucVu(null);
        playerData.setNgayThamGia(0);

        DatabaseManager.saveBangHoiData(bangHoi);
        DatabaseManager.savePlayerData(p.getName());

        MessageUtil.sendMessage(p, mse.getString("roiBangHoi").replace("%name%", getBangHoiName(bangHoi)));
        bangHoiAlert(bangHoi, mse.getString("thongBaoRieng.playerRoiBangHoi").replace("%player%", p.getName()));

        DebugManager.debug("PLAYER LEAVING BANG HOI", p.getName() + " left " + bangHoi);
    }

    public static void disbandBangHoi(Player p) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (playerData.getChucVu() != null)
            if (!playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("memberXoaBangHoi").replace("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        String bangHoiName = playerData.getBangHoi();
        BangHoiData BangHoiData = DatabaseManager.getBangHoiData(playerData.getBangHoi());

        List<String> members = BangHoiData.getThanhVien();
        for (String member : members) {

            PlayerData memberData = DatabaseManager.getPlayerData(member);

            memberData.setBangHoi(null);
            memberData.setChucVu(null);
            memberData.setNgayThamGia(0);

            DatabaseManager.savePlayerData(member);

            Player memberPlayer = Bukkit.getPlayer(member);

            if (memberPlayer != null && !member.equals(p.getName()))
                MessageUtil.sendMessage(memberPlayer, mse.getString("memberBangHoiGiaiTan")
                        .replaceAll("%name%", getBangHoiName(bangHoiName)).replaceAll("%player%", p.getName()));

        }


        DatabaseManager.bangHoiDatabase.remove(bangHoiName);
        DatabaseManager.bangHoi_diem.remove(bangHoiName);
        DatabaseManager.bangHoi_customName.remove(bangHoiName);

        File bangHoiFile = new File(BangHoi.plugin.getDataFolder() + "/bangHoiData/" + bangHoiName + ".yml");
        if (bangHoiFile.delete())
            MessageUtil.sendMessage(p, mse.getString("leaderBangHoiGiaiTan").replace("%name%", bangHoiName));
        else
            p.sendMessage("Gặp lỗi trong quá trình giải tán, liên hệ admin để xử lý");

    }

    public static void setBangHoiIcon(Player p, String material) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (playerData.getChucVu() != null)
            if (!playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("memberChinhIcon").replace("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        if (material.equalsIgnoreCase("AIR")) {
            MessageUtil.sendMessage(p, mse.getString("iconKhongHopLe"));
            return;
        }

        String bangHoiName = playerData.getBangHoi();
        BangHoiData bangHoiData = DatabaseManager.getBangHoiData(playerData.getBangHoi());

        XMaterial.matchXMaterial(material).map(XMaterial::parseMaterial).map(item -> {
            bangHoiData.setBangHoiIcon(material);
            DatabaseManager.saveBangHoiData(bangHoiName);
            MessageUtil.sendMessage(p, mse.getString("chinhIcon").replace("%material%", material.toUpperCase()));
            BangHoiManager.bangHoiAlert(bangHoiName, mse.getString("thongBaoRieng.chinhIcon")
                    .replaceAll("%material%", material.toUpperCase())
                    .replaceAll("%leader%", p.getName()));
            return item;
        }).orElseGet(() -> {
            MessageUtil.sendMessage(p, mse.getString("iconKhongHopLe"));
            p.sendMessage("https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html");
            return null;
        });

    }

    public static void deleteBangHoi(String bangHoiName) {

        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoiName)) {
            return;
        }

        BangHoiData BangHoiData = DatabaseManager.getBangHoiData(bangHoiName);
        List<String> members = BangHoiData.getThanhVien();
        for (String member : members) {

            PlayerData memberData = DatabaseManager.getPlayerData(member);

            memberData.setBangHoi(null);
            memberData.setChucVu(null);
            memberData.setNgayThamGia(0);

            DatabaseManager.savePlayerData(member);
        }

        DatabaseManager.bangHoiDatabase.remove(bangHoiName);
        DatabaseManager.bangHoi_diem.remove(bangHoiName);
        DatabaseManager.bangHoi_customName.remove(bangHoiName);

        File bangHoiFile = new File(BangHoi.plugin.getDataFolder() + "/bangHoiData/" + bangHoiName + ".yml");
        if (bangHoiFile.delete())
            MessageUtil.log("Đã xóa bang hội " + bangHoiName);
        DebugManager.debug("DELETING BANG HOI", bangHoiName + " has been deleted");
    }

    public static void inviteBangHoi(Player p, Player target) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (p.equals(target)) {
            MessageUtil.sendMessage(p, mse.getString("moiBanThan"));
            return;
        }

        if (target == null) {
            MessageUtil.sendMessage(p, mse.getString("nguoiChoiKhongTonTai"));
            return;
        }

        PlayerData targetData = DatabaseManager.getPlayerData(target.getName());

        if (DatabaseManager.bangHoiInvitingPlayers.containsKey(target.getName())) {
            MessageUtil.sendMessage(p, mse.getString("playerDaCoLoiMoi").replace("%player%", target.getName()));
            return;
        }

        if (playerData.getChucVu() != null)
            if (!playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("memberMoiThanhVien").replace("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        if (targetData.getBangHoi() != null) {
            MessageUtil.sendMessage(p, mse.getString("playerDaCoBangHoi").replace("%player%", target.getName()));
            return;
        }

        BangHoiData BangHoiData = DatabaseManager.getBangHoiData(playerData.getBangHoi());
        if (BangHoiData.getThanhVien().size() >= BangHoiData.getSoLuongToiDa()) {
            MessageUtil.sendMessage(p,
                    mse.getString("dayThanhVien").replace("%number%", String.valueOf(BangHoiData.getSoLuongToiDa())));
            return;
        }

        int time = BangHoi.plugin.getConfig().getInt("bang-hoi-options.thoi-gian-moi");

        MessageUtil.sendMessage(p, mse.getString("leaderMoi").replaceAll("%player%", target.getName()).replaceAll("%time%",
                String.valueOf(time)));
        MessageUtil.sendMessage(target,
                mse.getString("targetMoi").replaceAll("%player%", p.getName())
                        .replaceAll("%name%", getBangHoiName(playerData.getBangHoi()))
                        .replaceAll("%time%", String.valueOf(time)));
        DatabaseManager.bangHoiInvitingPlayers.put(target.getName(), p.getName());

        new BukkitRunnable() {

            @Override
            public void run() {
                if (DatabaseManager.bangHoiInvitingPlayers.containsKey(target.getName())) {

                    if (p.isOnline())
                        MessageUtil.sendMessage(p, mse.getString("leaderQuaThoiGian").replace("%player%", target.getName()));

                    if (target.isOnline())
                        MessageUtil.sendMessage(target, mse.getString("memberQuaThoiGian"));

                    DatabaseManager.bangHoiInvitingPlayers.remove(target.getName());
                }
            }
        }.runTaskLater(BangHoi.plugin, 20 * 30);

    }

    public static void acceptBangHoi(Player p) {

        if (!DatabaseManager.bangHoiInvitingPlayers.containsKey(p.getName())) {
            MessageUtil.sendMessage(p, mse.getString("khongCoLoiMoi"));
            return;
        }

        String bangHoi = DatabaseManager.getPlayerData(DatabaseManager.bangHoiInvitingPlayers.get(p.getName())).getBangHoi();

        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi)) {
            MessageUtil.sendMessage(p, mse.getString("bangHoiNayKhongTonTai"));
            DatabaseManager.bangHoiInvitingPlayers.remove(p.getName());
            return;
        }

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());

        if (playerData.getBangHoi() != null) {
            MessageUtil.sendMessage(p, mse.getString("dangOTrongBangHoiKhac"));
            DatabaseManager.bangHoiInvitingPlayers.remove(p.getName());
            return;
        }

        BangHoiData BangHoiData = DatabaseManager.getBangHoiData(bangHoi);
        if (BangHoiData.getThanhVien().size() >= BangHoiData.getSoLuongToiDa()) {
            MessageUtil.sendMessage(p, mse.getString("memberDayThanhVien").replace("%name%", getBangHoiName(bangHoi)));
            DatabaseManager.bangHoiInvitingPlayers.remove(p.getName());
            return;
        }

        Date d = new Date();
        long dateLong = d.getTime();

        playerData.setBangHoi(bangHoi);
        playerData.setChucVu("Member");
        playerData.setNgayThamGia(dateLong);
        BangHoiData.addThanhVien(p.getName());

        DatabaseManager.savePlayerData(p.getName());
        DatabaseManager.saveBangHoiData(bangHoi);

        MessageUtil.sendMessage(p, mse.getString("memberGiaNhapThanhCong").replace("%name%", getBangHoiName(bangHoi)));

        Player leader = Bukkit.getPlayer(DatabaseManager.bangHoiInvitingPlayers.get(p.getName()));
        if (leader != null)
            MessageUtil.sendMessage(leader, mse.getString("leaderGiaNhapThanhCong").replace("%player%", p.getName()));

        bangHoiAlert(bangHoi, mse.getString("thongBaoRieng.leaderMoiThanhVien")
                .replaceAll("%leader%", BangHoiData.getBangHoiFounder()).replaceAll("%player%", p.getName()));
        DatabaseManager.bangHoiInvitingPlayers.remove(p.getName());

        if (BangHoiData.getThanhVien().size() >= BangHoiData.getSoLuongToiDa()) {
            for (String invitingPlayersQueue : DatabaseManager.bangHoiInvitingPlayers.keySet())
                if (DatabaseManager.bangHoiInvitingPlayers.get(invitingPlayersQueue).contains(BangHoiData.getBangHoiFounder())) {
                    DatabaseManager.bangHoiInvitingPlayers.remove(invitingPlayersQueue);
                    Player leaderPlayer = Bukkit.getPlayer(BangHoiData.getBangHoiFounder());
                    if (leaderPlayer != null)
                        MessageUtil.sendMessage(leaderPlayer, mse.getString("leaderDayThanhVien").replace("%player%", invitingPlayersQueue));
                }
        }

        DebugManager.debug("PLAYER JOINING BANG HOI", p.getName() + " joined " + bangHoi);
    }

    public static void denyBangHoi(Player p) {

        if (!DatabaseManager.bangHoiInvitingPlayers.containsKey(p.getName())) {
            MessageUtil.sendMessage(p, mse.getString("khongCoLoiMoi"));
            return;
        }

        DatabaseManager.bangHoiInvitingPlayers.remove(p.getName());
        MessageUtil.sendMessage(p, mse.getString("memberHuyLoiMoi"));

        Player leader = Bukkit.getPlayer(DatabaseManager.bangHoiInvitingPlayers.get(p.getName()));
        if (leader != null)
            MessageUtil.sendMessage(leader, mse.getString("leaderHuyLoiMoi").replace("%player%", p.getName()));
    }

    public static void kickBangHoi(Player p, String target) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (p.getName().equalsIgnoreCase(target)) {
            MessageUtil.sendMessage(p, mse.getString("duoiBanThan"));
            return;
        }

        if (!DatabaseManager.playerDatabase.containsKey(target)) {
            MessageUtil.sendMessage(p, mse.getString("duLieuKhongTonTai").replace("%player%", target));
            return;
        }

        PlayerData targetData = DatabaseManager.getPlayerData(target);
        if (targetData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("duLieuKhongTonTai").replace("%player%", target));
            return;
        }

        if (playerData.getChucVu() != null)
            if (!playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("memberDuoi").replace("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        BangHoiData BangHoiData = DatabaseManager.getBangHoiData(playerData.getBangHoi());

        if (!targetData.getBangHoi().equals(playerData.getBangHoi())) {
            MessageUtil.sendMessage(p, mse.getString("playerKhongTrongBangHoi").replace("%player%", target));
            return;
        }

        targetData.setBangHoi(null);
        targetData.setChucVu(null);
        targetData.setNgayThamGia(0);
        BangHoiData.removeThanhVien(target);

        DatabaseManager.savePlayerData(target);
        DatabaseManager.saveBangHoiData(playerData.getBangHoi());

        MessageUtil.sendMessage(p, mse.getString("leaderDuoi").replace("%player%", target));
        bangHoiAlert(playerData.getBangHoi(), mse.getString("thongBaoRieng.leaderDuoiThanhVien")
                .replaceAll("%leader%", BangHoiData.getBangHoiFounder()).replaceAll("%player%", target));

        if (Bukkit.getPlayer(target) != null)
            MessageUtil.sendMessage(Bukkit.getPlayer(target), mse.getString("memberBiDuoi")
                    .replaceAll("%name%", getBangHoiName(playerData.getBangHoi())).replaceAll("%player%", p.getName()));

    }

    public static void setLeaderBangHoi(Player p, String target) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (p.getName().equalsIgnoreCase(target)) {
            MessageUtil.sendMessage(p, mse.getString("chuyenQuyenSelf"));
            return;
        }

        if (!DatabaseManager.playerDatabase.containsKey(target)) {
            MessageUtil.sendMessage(p, mse.getString("duLieuKhongTonTai").replace("%player%", target));
            return;
        }

        PlayerData targetData = DatabaseManager.getPlayerData(target);
        if (targetData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("duLieuKhongTonTai").replace("%player%", target));
            return;
        }

        if (playerData.getChucVu() != null)
            if (!playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("memberChuyenQuyen").replace("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        if (!targetData.getBangHoi().equals(playerData.getBangHoi())) {
            MessageUtil.sendMessage(p, mse.getString("playerKhongTrongBangHoi").replace("%player%", target));
            return;
        }

        playerData.setChucVu("Member");
        DatabaseManager.getBangHoiData(playerData.getBangHoi()).setBangHoiFounder(target);
        targetData.setChucVu("Leader");

        DatabaseManager.saveBangHoiData(playerData.getBangHoi());
        DatabaseManager.savePlayerData(p.getName());
        DatabaseManager.savePlayerData(target);

        MessageUtil.sendMessage(p, mse.getString("leaderChuyenQuyen").replace("%player%", target));
        bangHoiAlert(playerData.getBangHoi(), mse.getString("thongBaoRieng.leaderChuyenQuyen")
                .replaceAll("%leader%", p.getName()).replaceAll("%player%", target));

        if (Bukkit.getPlayer(target) != null)
            MessageUtil.sendMessage(Bukkit.getPlayer(target), mse.getString("memberDuocChuyenQuyen")
                    .replaceAll("%name%", getBangHoiName(playerData.getBangHoi())).replaceAll("%player%", p.getName()));

    }

    public static void viewInfo(Player p, String bangHoi) {

        if (!DatabaseManager.bangHoiDatabase.containsKey(bangHoi)) {
            MessageUtil.sendMessage(p, mse.getString("bangHoiKhongTonTai").replace("%name%", bangHoi));
            return;
        }

        BangHoiData bangHoiData = DatabaseManager.getBangHoiData(bangHoi);

        Date currentDate = new Date(bangHoiData.getNgayThanhLap());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String strDate = dateFormat.format(currentDate);

        for (String str : mse.getStringList("thongTinBangHoi")) {
            str = str.replace("%ten%", bangHoiData.getBangHoiName());
            str = str.replace("%tencustom%",
                    (bangHoiData.getTenCustom() == null ? "&7Không có" : bangHoiData.getTenCustom()));
            str = str.replace("%lanhdao%", bangHoiData.getBangHoiFounder());
            str = str.replace("%warn%", String.valueOf(bangHoiData.getBangHoiWarn()));
            str = str.replace("%warpoint%", String.valueOf(bangHoiData.getBangHoiWarPoint()));
            str = str.replace("%diem%", String.valueOf(bangHoiData.getBangHoiScore()));
            str = str.replace("%ngaythanhlap%", strDate);
            str = str.replace("%sothanhvien%", String.valueOf(bangHoiData.getThanhVien().size()));
            str = str.replace("%thanhvientoida%", String.valueOf(bangHoiData.getSoLuongToiDa()));
            str = str.replace("%skill1%", (bangHoiData.getSkillLevel(1) == 0 ? "&cCHƯA MỞ KHÓA" : "&aĐÃ MỞ KHÓA"));
            str = str.replace("%skill2%", (bangHoiData.getSkillLevel(2) == 0 ? "&cCHƯA MỞ KHÓA" : "&aĐÃ MỞ KHÓA"));
            str = str.replace("%skill3%", (bangHoiData.getSkillLevel(3) == 0 ? "&cCHƯA MỞ KHÓA" : "&aĐÃ MỞ KHÓA"));
            str = str.replace("%skill4%", (bangHoiData.getSkillLevel(4) == 0 ? "&cCHƯA MỞ KHÓA" : "&aĐÃ MỞ KHÓA"));


            MessageUtil.sendMessage(p, str);
        }

        for (String member : bangHoiData.getThanhVien())
            MessageUtil.sendMessage(p,
                    mse.getString("thongTinBangHoi-thanhVien").replaceAll("%ten%", member)
                            .replaceAll("%trangthai%",
                                    (Bukkit.getPlayer(member) != null ? " &aONLINE" : " &cOFFLINE"))
                            .replaceAll("%lanhdao%", (bangHoiData.getBangHoiFounder().contains(member) ? " &6(Lãnh đạo)" : "")));

    }

    public static void toggleChat(Player p) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());
        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (!DatabaseManager.bangHoiChattingPlayers.contains(p.getName())) {
            DatabaseManager.bangHoiChattingPlayers.add(p.getName());
            MessageUtil.sendMessage(p, MessageFile.get().getString("batChat"));
        } else {
            while (DatabaseManager.bangHoiChattingPlayers.contains(p.getName())) {
                DatabaseManager.bangHoiChattingPlayers.remove(p.getName());
            }
            MessageUtil.sendMessage(p, MessageFile.get().getString("tatChat"));
        }
    }

    public static void setCustomName(Player p, String customName) {

        PlayerData playerData = DatabaseManager.getPlayerData(p.getName());

        if (playerData.getBangHoi() == null) {
            MessageUtil.sendMessage(p, mse.getString("khongCoBangHoi"));
            return;
        }

        if (playerData.getChucVu() != null)
            if (!playerData.getChucVu().equals("Leader")) {
                MessageUtil.sendMessage(p,
                        mse.getString("memberSetTenCustom").replace("%name%", getBangHoiName(playerData.getBangHoi())));
                return;
            }

        if (!p.hasPermission("banghoi.setcustomname")) {
            MessageUtil.sendMessage(p, mse.getString("khongCoQuyen-tenCustom"));
            return;
        }

        if (!p.hasPermission("banghoi.setcustomname.color"))
            if (customName.contains("&")) {
                MessageUtil.sendMessage(p, mse.getString("khongCoQuyen-tenCustom-Color"));
                return;
            }

        if (DatabaseManager.bangHoiDatabase.containsKey(customName)) {
            MessageUtil.sendMessage(p, mse.getString("tenCustomTrungTenBangHoi").replace("%name%", customName));
            return;
        }

        List<String> blacklist_chars = BangHoi.plugin.getConfig().getStringList("bang-hoi-options.ky-tu-cam");
        blacklist_chars.remove("&");

        for (String chars : blacklist_chars)
            if (customName.contains(chars)) {
                for (String str : mse.getStringList("tenCoKyTuKhongHopLe"))
                    MessageUtil.sendMessage(p, str);
                return;
            }

        List<String> blacklist_name = BangHoi.plugin.getConfig().getStringList("ten_cam");
        for (String bn : blacklist_name) {
            if (StringUtils.containsIgnoreCase(customName, bn)) {
                MessageUtil.sendMessage(p, mse.getString("tenKhongHopLe"));
                return;
            }
        }

        String customName_removeColor = BangHoi.nms.stripColor(customName);
        FileConfiguration config = BangHoi.plugin.getConfig();
        int min_length = config.getInt("bang-hoi-options.so-ky-tu-toi-thieu");
        int max_length = config.getInt("bang-hoi-options.so-ky-tu-toi-da");

        if (customName_removeColor.length() < min_length) {
            MessageUtil.sendMessage(p, mse.getString("tenQuaNgan").replace("%number%", String.valueOf(min_length)));
            return;
        }

        if (customName_removeColor.length() > max_length) {
            MessageUtil.sendMessage(p, mse.getString("tenQuaDai").replace("%number%", String.valueOf(max_length)));
            return;
        }

        if (DatabaseManager.bangHoi_customName.containsValue(customName)) {
            MessageUtil.sendMessage(p, mse.getString("tenCustomDaTonTai").replace("%name%", customName));
            return;
        }

        BangHoiData bangHoiData = DatabaseManager.getBangHoiData(playerData.getBangHoi());
        bangHoiData.setTenCustom(customName);
        DatabaseManager.saveBangHoiData(playerData.getBangHoi());

        MessageUtil.sendMessage(p, mse.getString("leaderSetTenCustom").replace("%name%", customName));
        bangHoiAlert(playerData.getBangHoi(), mse.getString("thongBaoRieng.leaderSetCustomName")
                .replaceAll("%leader%", p.getName()).replaceAll("%name%", customName));
    }

    public static int getWarPointCost(int skill, int level) {
        FileConfiguration config = BangHoi.plugin.getConfig();

        if (skill == 1)
            return config.getInt("skill.1CritDamage");

        if (skill == 2)
            return config.getInt("skill.2BoostScore");

        if (skill == 3)
            if (level == 1)
                return config.getInt("skill.3Dodge.1");
             else
                return config.getInt("skill.3Dodge.2");

        if (skill == 4)
            return config.getInt("skill.4Vampire");

        return 0;
    }
}
