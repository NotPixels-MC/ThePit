package com.thepit.Utils;

import com.thepit.Enchants.EnchantTypes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantUtils {

    public static String toRoman(int number) {
        String[] romans = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                result.append(romans[i]);
            }
        }
        return result.toString();
    }

    public static void setLives(ItemStack item, int lives, int maxLives) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        // Remove old lives line
        lore.removeIf(line -> ChatColor.stripColor(line).startsWith("Lives:"));

        // Choose color based on remaining lives
        String lifeColor = lives < 4 ? "§c" : "§a"; // red if <4, green otherwise

        // Insert new lives line at the top
        lore.add(0, "§7Lives: " + lifeColor + lives + "§7/§7" + maxLives);

        meta.setLore(lore);
        item.setItemMeta(meta);
    }



    public static void applyEnchant(ItemStack item, EnchantTypes enchant, int level) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        String roman = toRoman(level);

        // Remove old version
        lore.removeIf(line -> ChatColor.stripColor(line).startsWith(ChatColor.stripColor(enchant.getDisplayName())));

        lore.add("");

        // Add enchant line
        lore.add("§9" + enchant.getDisplayName() + " " + roman);

        // Add wrapped tier description
        String desc = enchant.getDescription(level);
        for (String wrapped : wrapLore(desc, 30)) {
            lore.add("§7" + wrapped);
        }


        meta.setLore(lore);
        item.setItemMeta(meta);
    }


    public static List<String> wrapLore(String text, int maxLength) {
        List<String> lines = new ArrayList<>();

        // Detect placeholder
        if (text.contains("%wrap%")) {
            String[] split = text.split("%wrap%", 2);

            // Add the part before %wrap% as-is (if not empty)
            if (!split[0].isEmpty()) {
                lines.add(split[0].trim());
            }

            // Wrap the part after %wrap%
            if (split.length > 1 && !split[1].isEmpty()) {
                lines.addAll(wrapLore(split[1].trim(), maxLength));
            }

            return lines;
        }

        // --- Normal wrapping logic below ---
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (ChatColor.stripColor(line.toString() + word).length() > maxLength) {
                lines.add(line.toString());
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }

        if (line.length() > 0) {
            lines.add(line.toString());
        }

        return lines;
    }



    public static void handleDeathInventory(Player player) {
        PlayerInventory inv = player.getInventory();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);

            if (item == null || item.getType() == Material.AIR) continue;

            // Check if item has lives
            int[] lives = getLives(item);

            if (lives != null) {
                boolean shouldBreak = decreaseLives(item);

                if (shouldBreak) {
                    inv.setItem(i, null);
                }

                continue; // skip deletion logic
            }

            // Check if item is "Kept on death"
            if (isKeptOnDeath(item)) continue;

            // Otherwise delete it
            inv.setItem(i, null);
        }
    }



    public static boolean shouldKeepItem(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;

        for (String line : item.getItemMeta().getLore()) {
            String stripped = ChatColor.stripColor(line).toLowerCase();

            if (stripped.contains("Kept on death.")) return true;
            if (stripped.contains("Lives:")) return true;
        }

        return false;
    }

    public static boolean isKeptOnDeath(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        for (String line : meta.getLore()) {
            if (ChatColor.stripColor(line).equalsIgnoreCase("Kept on death")) {
                return true;
            }
        }
        return false;
    }


    public static int[] getLives(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;

        for (String line : item.getItemMeta().getLore()) {
            String stripped = ChatColor.stripColor(line).toLowerCase();

            if (stripped.startsWith("lives:")) {
                // Format: Lives: 12/100
                String[] parts = stripped.replace("lives:", "").trim().split("/");
                int lives = Integer.parseInt(parts[0]);
                int maxLives = Integer.parseInt(parts[1]);
                return new int[]{lives, maxLives};
            }
        }
        return null;
    }

    public static boolean decreaseLives(ItemStack item) {
        int[] data = getLives(item);
        if (data == null) return false;

        int lives = data[0];
        int maxLives = data[1];

        lives -= 1;

        if (lives <= 0) {
            return true; // signal to delete item
        }

        // Update lore with new lives
        setLives(item, lives, maxLives);
        return false;
    }

    public static ItemStack makeItemUnbreakable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        // Correct 1.8.8 call
        meta.spigot().setUnbreakable(true);

        item.setItemMeta(meta);
        return item;
    }



}
