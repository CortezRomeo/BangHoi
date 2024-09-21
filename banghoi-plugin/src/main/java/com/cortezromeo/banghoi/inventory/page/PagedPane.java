package com.cortezromeo.banghoi.inventory.page;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.file.InventoryFile;
import com.cortezromeo.banghoi.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.Map.Entry;



public class PagedPane implements InventoryHolder {

    private Inventory inventory;

    private SortedMap<Integer, Page> pages = new TreeMap<>();
    private int currentIndex;
    private int pageSize;
    private String pageTitle;

    protected Button controlBack;
    protected Button controlNext;

    public PagedPane(int pageSize, int rows, String title) {
        Objects.requireNonNull(title, "Title không được để trống!");
        if (rows > 6) {
            throw new IllegalArgumentException("Độ dài cần <= 6, đang có " + rows);
        }
        if (pageSize > 6) {
            throw new IllegalArgumentException("Độ dài cần <= 6, đang có" + pageSize);
        }

        this.pageSize = pageSize;
        this.pageTitle = title;
        inventory = Bukkit.createInventory(this, rows * 9, BangHoi.nms.addColor(title));

        pages.put(0, new Page(pageSize));
    }

    public void addButton(Button button) {
        for (Entry<Integer, Page> entry : pages.entrySet()) {
            if (entry.getValue().addButton(button)) {
                if (entry.getKey() == currentIndex) {
                    reRender();
                }
                return;
            }
        }
        Page page = new Page(pageSize);
        page.addButton(button);
        pages.put(pages.lastKey() + 1, page);

        reRender();
    }

    public void removeButton(Button button) {
        for (Iterator<Entry<Integer, Page>> iterator = pages.entrySet().iterator(); iterator.hasNext();) {
            Entry<Integer, Page> entry = iterator.next();
            if (entry.getValue().removeButton(button)) {

                if (entry.getValue().isEmpty()) {
                    if (pages.size() > 1) {
                        iterator.remove();
                    }
                    if (currentIndex >= pages.size()) {
                        currentIndex--;
                    }
                }
                if (entry.getKey() >= currentIndex) {
                    reRender();
                }
                return;
            }
        }
    }

    public int getPageAmount() {
        return pages.size();
    }

    public int getCurrentPage() {
        return currentIndex + 1;
    }

    public void selectPage(int index) {
        if (index < 0 || index >= getPageAmount()) {
            throw new IllegalArgumentException("Error");
        }
        if (index == currentIndex) {
            return;
        }

        currentIndex = index;
        reRender();
    }

    public void reRender() {
        inventory.clear();
        pages.get(currentIndex).render(inventory);

        controlBack = null;
        controlNext = null;
        createControls(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected String checkInventoryName() {

        FileConfiguration invCfg = InventoryFile.get();

        if (pageTitle.equals(BangHoi.nms.addColor(invCfg.getString("gui.listBangHoi.title")))) {
            return "listbanghoi";
        } else if (pageTitle.equals(BangHoi.nms.addColor(invCfg.getString("gui.viewMembers.title"))))
            return "vm";

        return null;
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        FileConfiguration invCfg = InventoryFile.get();

        if (checkInventoryName().equals("listbanghoi")) {

            if (event.getSlot() == invCfg.getInt("gui.listBangHoi.items.prevPage.slot")) {
                if (controlBack != null) {
                    controlBack.onClick(event);
                }
                return;
            }
            if (event.getSlot() == invCfg.getInt("gui.listBangHoi.items.nextPage.slot")) {
                if (controlNext != null) {
                    controlNext.onClick(event);
                }
                return;
            }
        } else if (checkInventoryName().equals("vm")) {

            if (event.getSlot() == invCfg.getInt("gui.viewMembers.items.prevPage.slot")) {
                if (controlBack != null) {
                    controlBack.onClick(event);
                }
                return;
            }
            if (event.getSlot() == invCfg.getInt("gui.viewMembers.items.nextPage.slot")) {
                if (controlNext != null) {
                    controlNext.onClick(event);
                }
                return;
            }
        }
        pages.get(currentIndex).handleClick(event);

    }

    private String getLang(String str) {
        return InventoryFile.get().getString(str);
    }

    private void fillRow(int rowIndex, ItemStack itemStack, Inventory inventory) {
        int yMod = rowIndex * 9;
        for (int i = 0; i < 9; i++) {
            int slot = yMod + i;
            inventory.setItem(slot, itemStack);
        }
    }

    protected void createControls(Inventory inventory) {

        // create separator
        fillRow(inventory.getSize() / 9 - 2,
                InventoryUtil.getItem(InventoryFile.get().getString("borderItem.type"),
                        InventoryFile.get().getString("borderItem.value"),
                        (short) InventoryFile.get().getInt("borderItem.data"),
                        InventoryFile.get().getString("borderItem.name"),
                        InventoryFile.get().getStringList("borderItem.lore"), "", "", 0),
                inventory);

        int prevpageslot = 1;
        int nextpageslot = 1;
        int pageinfoslot = 1;
        int backSlot = InventoryFile.get().getInt("gui.viewMembers.items.back.slot");
        if (checkInventoryName().equals("listbanghoi")) {
            nextpageslot = InventoryFile.get().getInt("gui.listBangHoi.items.nextPage.slot");
            prevpageslot = InventoryFile.get().getInt("gui.listBangHoi.items.prevPage.slot");
            pageinfoslot = InventoryFile.get().getInt("gui.listBangHoi.items.pageInfo.slot");
        } else if (checkInventoryName().equals("vm")) {
            nextpageslot = InventoryFile.get().getInt("gui.viewMembers.items.nextPage.slot");
            prevpageslot = InventoryFile.get().getInt("gui.viewMembers.items.prevPage.slot");
            pageinfoslot = InventoryFile.get().getInt("gui.viewMembers.items.pageInfo.slot");

            ItemStack itemStack = getItem(getLang("back.type"), getLang("back.value"), getLang("back.name"),
                    InventoryFile.get().getStringList(("back.lore")));
            inventory.setItem(backSlot, itemStack);

        }

        if (getCurrentPage() > 1) {

            ItemStack itemStack = getItem(getLang("prevPage.type"), getLang("prevPage.value"), getLang("prevPage.name"),
                    InventoryFile.get().getStringList(("prevPage.lore")));
            controlBack = new Button(itemStack, event -> selectPage(currentIndex - 1));
            inventory.setItem(prevpageslot, itemStack);
        }

        if (getCurrentPage() < getPageAmount()) {
            ItemStack itemStack = getItem(getLang("nextPage.type"), getLang("nextPage.value"), getLang("nextPage.name"),
                    InventoryFile.get().getStringList(("nextPage.lore")));
            controlNext = new Button(itemStack, event -> selectPage(getCurrentPage()));
            inventory.setItem(nextpageslot, itemStack);
        }

        ItemStack itemStack = getItem(getLang("pageInfo.type"), getLang("pageInfo.value"), getLang("pageInfo.name"),
                InventoryFile.get().getStringList(("pageInfo.lore")));
        inventory.setItem(pageinfoslot, itemStack);

    }

    public void open(Player player) {
        reRender();
        player.openInventory(getInventory());
    }

    private static class Page {
        private List<Button> buttons = new ArrayList<>();
        private int maxSize;

        Page(int maxSize) {
            this.maxSize = maxSize;
        }

        void handleClick(InventoryClickEvent event) {

            if (event.getRawSlot() > event.getInventory().getSize()) {
                return;
            }

            if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                return;
            }
            if (event.getSlot() >= buttons.size()) {
                return;
            }
            Button button = buttons.get(event.getSlot());
            button.onClick(event);
        }

        boolean hasSpace() {
            return buttons.size() < maxSize * 9;
        }

        boolean addButton(Button button) {
            if (!hasSpace()) {
                return false;
            }
            buttons.add(button);

            return true;
        }

        boolean removeButton(Button button) {
            return buttons.remove(button);
        }

        void render(Inventory inventory) {
            for (int i = 0; i < buttons.size(); i++) {
                Button button = buttons.get(i);

                inventory.setItem(i, button.getItemStack());
            }
        }

        boolean isEmpty() {
            return buttons.isEmpty();
        }
    }

    public ItemStack getItem(String type, String value, String name, List<String> lore) {

        ItemStack material = new ItemStack(Material.BEDROCK);

        if (type.equalsIgnoreCase("customhead"))
            material = BangHoi.nms.getHeadItemFromBase64(value);

        if (type.equalsIgnoreCase("material"))
            material = new ItemStack(Material.valueOf(value));

        ItemMeta materialMeta = material.getItemMeta();

        // --------------------NAME---------------------------

        if (name != "" || name != null) {

            name = name.replace("%trang%", String.valueOf(getCurrentPage()));
            name = name.replace("%trangcuoi%", String.valueOf(getPageAmount()));

            materialMeta.setDisplayName(BangHoi.nms.addColor(name));
        }

        // ---------------------LORE-----------------------

        List<String> createLore = new ArrayList<String>();
        for (Object lores : lore) {
            createLore.add(ChatColor.translateAlternateColorCodes('&', lores.toString()));
        }

        List<String> newList = new ArrayList<String>();
        for (String string : createLore) {

            string = string.replace("%trang%", String.valueOf(getCurrentPage()));
            string = string.replace("%trangsau%", String.valueOf(getCurrentPage() + 1));
            string = string.replace("%trangtruoc%", String.valueOf(getCurrentPage() - 1));
            string = string.replace("%trangcuoi%", String.valueOf(getPageAmount()));

            newList.add(string);

        }

        materialMeta.setLore(newList);

        // -----------------------------------------------

        material.setItemMeta(materialMeta);
        return material;

    }

}