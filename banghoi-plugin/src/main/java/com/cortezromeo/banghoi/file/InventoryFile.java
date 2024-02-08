package com.cortezromeo.banghoi.file;

import com.cortezromeo.banghoi.BangHoi;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class InventoryFile {

    private static File file;
    private static FileConfiguration inventoryFile;

    public static void setup() {
        file = new File(BangHoi.plugin.getDataFolder() + "/inventory.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        inventoryFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return inventoryFile;
    }

    public static void fileExists() {
        file = new File(BangHoi.plugin.getDataFolder() + "/inventory.yml");
        inventoryFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            inventoryFile.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        inventoryFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void setupLang() {

        get().addDefault("borderItem.name", "");
        get().addDefault("borderItem.type", "material");
        get().addDefault("borderItem.value", "BLACK_STAINED_GLASS_PANE");
        get().addDefault("borderItem.data", 0);
        get().addDefault("borderItem.lore", new String[]{
                ""
        });

        InventoryFile.get().addDefault("nextPage.type", "customhead");
        InventoryFile.get().addDefault("nextPage.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");
        InventoryFile.get().addDefault("nextPage.data", 0);
        InventoryFile.get().addDefault("nextPage.name", "&3&lTrang &a%trang%&f/&c%trangcuoi%");
        InventoryFile.get().addDefault("nextPage.lore", new String[]{
                "",
                "&fTrang hiện tại: &b%trang%",
                "&fNhấn vào đây để sang trang &a%trangsau%",
                ""
        });

        InventoryFile.get().addDefault("pageInfo.type", "material");
        InventoryFile.get().addDefault("pageInfo.value", "BOOK");
        InventoryFile.get().addDefault("pageInfo.data", 0);
        InventoryFile.get().addDefault("pageInfo.name", "&bTôi đang ở đâu?");
        InventoryFile.get().addDefault("pageInfo.lore", new String[]{
                "",
                "&fBạn đang ở trang: &a%trang% &f/ &a%trangcuoi%",
                ""
        });

        InventoryFile.get().addDefault("prevPage.type", "customhead");
        InventoryFile.get().addDefault("prevPage.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
        InventoryFile.get().addDefault("prevPage.data", 0);
        InventoryFile.get().addDefault("prevPage.name", "&3&lTrang &a%trang%&f/&c%trangcuoi%");
        InventoryFile.get().addDefault("prevPage.lore", new String[]{
                "",
                "&fTrang hiện tại: &b%trang%",
                "&fNhấn vào đây để về trang &a%trangtruoc%",
                ""
        });

        InventoryFile.get().addDefault("back.type", "customhead");
        InventoryFile.get().addDefault("back.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ");
        InventoryFile.get().addDefault("back.data",0);
        InventoryFile.get().addDefault("back.name", "&cTrở về trang trước");
        InventoryFile.get().addDefault("back.lore", new String[]{
                "&eNhấn để trở về trang trước"
        });

        // ---

        InventoryFile.get().addDefault("gui.listBangHoi.rows", 6);
        InventoryFile.get().addDefault("gui.listBangHoi.title", "&0Bang hội");

        InventoryFile.get().addDefault("gui.listBangHoi.items.banghoi.type", "customhead");
        InventoryFile.get().addDefault("gui.listBangHoi.items.banghoi.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk2YWYzNmVhYWVkOGE3ZGJkMjcyN2ZkNGNkN2FmYmM2YzBhZmI4Yzc2MDYyOTRjOWNiNDAxMTQxYWFjMzc5In19fQ==");
        InventoryFile.get().addDefault("gui.listBangHoi.items.banghoi.data", 0);
        InventoryFile.get().addDefault("gui.listBangHoi.items.banghoi.name", "&#3ce8b7%ten%");
        InventoryFile.get().addDefault("gui.listBangHoi.items.banghoi.lore", new String[] {
                "&#85a832%diem% điểm &e[top: %top%]",
                "",
                "&fNgày thành lập: &e%ngaythanhlap%",
                "",
                "&fLãnh đạo: &a%lanhdao%",
                "&fSố thành viên: &b%sothanhvien%&7/&3%thanhvientoida%",
                "",
                "&fWarpoint: &b%warpoint%",
                "&fBị warn: &c%warn%&7/&42",
                "",
                "&eNhấn để xem thành viên!"
        });

        InventoryFile.get().addDefault("gui.listBangHoi.items.nextPage.slot", 52);
        InventoryFile.get().addDefault("gui.listBangHoi.items.pageInfo.slot", 49);
        InventoryFile.get().addDefault("gui.listBangHoi.items.prevPage.slot", 46);

        // ---

        InventoryFile.get().addDefault("gui.viewMembers.rows", 4);
        InventoryFile.get().addDefault("gui.viewMembers.title", "&0Xem thành viên");

        InventoryFile.get().addDefault("gui.viewMembers.items.member.type", "customhead");
        InventoryFile.get().addDefault("gui.viewMembers.items.member.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNhNDdiMTMxZGJjYmMzMGYzY2JlOTY0NzdmYTVlMzcyMjY4MmZhYjk5NWNkMmQyMmY3NWIxNWU5NDgyZmQyMyJ9fX0=");
        InventoryFile.get().addDefault("gui.viewMembers.items.member.data", 0);
        InventoryFile.get().addDefault("gui.viewMembers.items.member.name", "&6%name%");
        InventoryFile.get().addDefault("gui.viewMembers.items.member.lore", new String[]{
                "",
                "&fTrạng thái: &r%trangthai%",
                "&fChức vụ: &r%chucvu%",
                "&fNgày tham gia: &e%ngaythamgia%",
                ""
        });

        InventoryFile.get().addDefault("gui.viewMembers.items.nextPage.slot", 34);
        InventoryFile.get().addDefault("gui.viewMembers.items.pageInfo.slot", 31);
        InventoryFile.get().addDefault("gui.viewMembers.items.back.slot", 32);
        InventoryFile.get().addDefault("gui.viewMembers.items.prevPage.slot", 28);

        // ---

        InventoryFile.get().addDefault("gui.upgrade.rows", 3);
        InventoryFile.get().addDefault("gui.upgrade.title", "&0Nâng cấp bang hội");

        InventoryFile.get().addDefault("gui.upgrade.items.upgradeSlot.type", "material");
        InventoryFile.get().addDefault("gui.upgrade.items.upgradeSlot.value",
                "CHEST_MINECART");
        InventoryFile.get().addDefault("gui.upgrade.items.upgradeSlot.data", 0);
        InventoryFile.get().addDefault("gui.upgrade.items.upgradeSlot.name", "&3Nâng cấp slot");
        InventoryFile.get().addDefault("gui.upgrade.items.upgradeSlot.slot", 11);
        InventoryFile.get().addDefault("gui.upgrade.items.upgradeSlot.lore", new String[]{
                "",
                "&fNâng cấp slot cho bang hội",
                "",
                "&eNhấn vào đây để tùy chọn"
        });

        InventoryFile.get().addDefault("gui.upgrade.items.warPointShop.type", "customhead");
        InventoryFile.get().addDefault("gui.upgrade.items.warPointShop.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQyOGNkOTVmOTk2NGQxNTZkNjcyMGM4NGY4Njk4NzJiY2M2MTMwN2M0YmZmYTIyNDcyODZmYjMwNzdiZGRhYSJ9fX0=");
        InventoryFile.get().addDefault("gui.upgrade.items.warPointShop.data", 0);
        InventoryFile.get().addDefault("gui.upgrade.items.warPointShop.name", "&3Shop tính năng");
        InventoryFile.get().addDefault("gui.upgrade.items.warPointShop.slot", 15);
        InventoryFile.get().addDefault("gui.upgrade.items.warPointShop.lore", new String[]{
                "",
                "&fMua các tính năng cho bang hội như",
                "&fbuff thêm điểm, hiệu ứng, ...",
                "",
                "&eNhấn vào đây để tùy chọn"
        });

        // ---

        InventoryFile.get().addDefault("gui.upgradeSlot.rows", 3);
        InventoryFile.get().addDefault("gui.upgradeSlot.title", "&0Nâng cấp slot");

        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotXu.type", "customhead");
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotXu.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ2MjhhY2U3YzNhZmM2MWE0NzZkYzE0NDg5M2FhYTY0MmJhOTc2ZDk1MmI1MWVjZTI2YWJhZmI4OTZiOCJ9fX0=");
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotXu.data", 0);
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotXu.name", "&3Nâng cấp &f[XÀI XU]");
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotXu.slot", 11);
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotXu.lore", new String[]{
                "",
                "&fSố thành viên tối đa hiện tại là:",
                "   &a%slot%",
                "&fSố xu cần để nâng cấp là:",
                "   &3%xu%",
                "",
                "&eNhấn vào đây để nâng cấp"
        });

        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotWarpoint.type", "customhead");
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotWarpoint.value",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmYzIzODY2NTIzY2FhYThhOTUzNDU2NjEyN2E2ZjgzODlhZjNlNzZiOGUzYzMzYzI0NzNjYmE2ODg5YzQifX19");
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotWarpoint.data", 0);
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotWarpoint.name", "&3Nâng cấp &f[XÀI WARPOINT]");
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotWarpoint.slot", 15);
        InventoryFile.get().addDefault("gui.upgradeSlot.items.upgradeSlotWarpoint.lore", new String[]{
                "&eChỉ có lãnh đạo mới có thể sử dụng nút này",
                "",
                "&fSố thành viên tối đa hiện tại là:",
                "   &a%slot%",
                "&fSố warpoint cần để nâng cấp là:",
                "   &b%warpoint%",
                "",
                "&eNhấn vào đây để nâng cấp"
        });
        InventoryFile.get().addDefault("gui.upgradeSlot.items.back.slot", 22);
        // ---

        InventoryFile.get().addDefault("gui.warPointShop.rows", 3);
        InventoryFile.get().addDefault("gui.warPointShop.title", "&0Shop tính năng");

        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.type", "material");
        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.value",
                "IRON_SWORD");
        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.data", 0);
        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.name", "&cSát thương chí mạng &7[SKILL-1]");
        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.slot", 10);
        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.lore.locked", new String[]{
                "&eChỉ có lãnh đạo mới có thể sử dụng nút này",
                "",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e25%",
                "&ftỉ lệ gây chí mạng (Gấp đôi sát thương)",
                "",
                "&fYêu cầu: &b%skill1WarPoint% Warpoint",
                "",
                "&eNhấn vào đây để nâng cấp"
        });
        InventoryFile.get().addDefault("gui.warPointShop.items.1CritDamage.lore.unlocked", new String[]{
                "&a&lĐÃ MỞ KHÓA",
                "",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e25%",
                "&ftỉ lệ gây chí mạng (Gấp đôi sát thương)",
                "",
        });

        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.type", "material");
        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.value",
                "DRAGON_BREATH");
        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.data", 0);
        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.name", "&dBonus điểm cộng &7[SKILL-2]");
        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.slot", 12);
        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.lore.locked", new String[]{
                "&eChỉ có lãnh đạo mới có thể sử dụng nút này",
                "",
                "&fCộng thêm một điểm khi giết người trong bang hội war",
                "",
                "&fYêu cầu: &b%skill2WarPoint% Warpoint",
                "",
                "&eNhấn vào đây để nâng cấp"
        });
        InventoryFile.get().addDefault("gui.warPointShop.items.2BoostScore.lore.unlocked", new String[]{
                "&a&lĐÃ MỞ KHÓA",
                "",
                "&fCộng thêm một điểm khi giết người trong bang hội war",
                "",
        });

        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.type", "material");
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.value",
                "SHIELD");
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.data", 0);
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.name", "&bKháng đòn đánh &7[SKILL-3]");
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.slot", 14);
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.lore.level1.locked", new String[]{
                "&eChỉ có lãnh đạo mới có thể sử dụng nút này",
                "",
                "&7Level 1:",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e15%",
                "&ftỉ lệ né đòn",
                "",
                "&7Level 2:",
                "&fTất cả thuộc tính của level 1, nhưng sẽ",
                "&fgây lại số sát thương tương ứng cho người",
                "&fgây sát thương",
                "",
                "&fYêu cầu: &b%skill3WarPoint-level1% Warpoint",
                "",
                "&eNhấn vào đây để mở khóa level 1"
        });
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.lore.level1.unlocked", new String[]{
                "&eChỉ có lãnh đạo mới có thể sử dụng nút này",
                "",
                "&a&l[ĐÃ MỞ KHÓA] Level 1:",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e15%",
                "&ftỉ lệ né đòn",
                "",
                "&7Level 2:",
                "&fTất cả thuộc tính của level 1, nhưng sẽ",
                "&fgây lại số sát thương tương ứng cho người",
                "&fgây sát thương",
                "",
                "&fYêu cầu: &b%skill3WarPoint-level2% Warpoint",
                "",
                "&eNhấn vào đây để mở khóa level 2"
        });
        InventoryFile.get().addDefault("gui.warPointShop.items.3Dodge.lore.level2.unlocked", new String[]{
                "&a&lĐÃ MỞ KHÓA",
                "",
                "&a&l[ĐÃ MỞ KHÓA] Level 1:",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e15%",
                "&ftỉ lệ né đòn",
                "",
                "&a&l[ĐÃ MỞ KHÓA] Level 2:",
                "&fTất cả thuộc tính của level 1, nhưng sẽ",
                "&fgây lại số sát thương tương ứng cho người",
                "&fgây sát thương",
                "",
        });

        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.type", "material");
        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.value",
                "REDSTONE");
        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.data", 0);
        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.name", "&aHồi máu &7[SKILL-4]");
        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.slot", 16);
        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.lore.locked", new String[]{
                "&eChỉ có lãnh đạo mới có thể sử dụng nút này",
                "",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e15%",
                "&ftỉ lệ hồi lại &a20% lượng máu tối đa",
                "",
                "&fYêu cầu: &b%skill4WarPoint% Warpoint",
                "",
                "&eNhấn vào đây để nâng cấp"
        });
        InventoryFile.get().addDefault("gui.warPointShop.items.4Vampire.lore.unlocked", new String[]{
                "&a&lĐÃ MỞ KHÓA",
                "",
                "&f&nTrong thời gian bang hội war&f, tất",
                "&fcả thành viên trong bang hội sẽ có &e15%",
                "&ftỉ lệ hồi lại &a20% lượng máu tối đa",
                "",
        });

        InventoryFile.get().addDefault("gui.warPointShop.items.back.slot", 22);

        //
        InventoryFile.get().options().copyDefaults(true);
        InventoryFile.save();
    }

}
