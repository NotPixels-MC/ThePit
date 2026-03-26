package com.thepit;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

public class Events implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        // Keep vanilla knockback + red flash
        event.setDamage(0);

        // 1. Base weapon damage
        double baseDamage = getWeaponDamage(attacker.getItemInHand());

        // 2. Critical hit check (1.8 logic)
        boolean isCrit = isCriticalHit(attacker);

        if (isCrit) {
            baseDamage *= 1.5; // 50% more damage like vanilla crits
            attacker.sendMessage("§6CRIT! §e+" + (baseDamage * 0.5) + " dmg");
        }

        // 3. Lore check (contains "test")
        if (hasLoreContaining(attacker.getItemInHand(), "test")) {
            baseDamage += 2; // example bonus
            attacker.sendMessage("§dLore bonus! §e+2 damage");
        }

        // 4. Armor reduction
        double armorPoints = getArmorPoints(victim);
        double reduction = armorPoints * 0.04;
        if (reduction > 0.80) reduction = 0.80;

        // 5. Final damage
        double finalDamage = baseDamage * (1 - reduction);

        // 6. Apply custom damage
        double newHealth = victim.getHealth() - finalDamage;

        // 7. Fake death
        if (newHealth <= 0) {
            handleKill(attacker, victim);
            return;
        }

        victim.setHealth(newHealth);

        // Debug
        attacker.sendMessage("§aYou dealt §c" + finalDamage + " §ato " + victim.getName());
        victim.sendMessage("§cYou took " + finalDamage + " damage");
    }

    // -------------------------
    // CRITICAL HIT CHECK
    // -------------------------
    private boolean isCriticalHit(Player p) {
        return p.getFallDistance() > 0 &&
                !p.isOnGround() &&
                !p.isInsideVehicle() &&
                !p.isSprinting() &&
                !p.isBlocking();
    }

    // -------------------------
    // LORE CHECK
    // -------------------------
    private boolean hasLoreContaining(ItemStack item, String text) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        for (String line : meta.getLore()) {
            if (line.toLowerCase().contains(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // -------------------------
    // FAKE KILL
    // -------------------------
    private void fakeKill(Player killer, Player victim) {
        victim.setHealth(victim.getMaxHealth());

        killer.sendMessage("§eYou killed §c" + victim.getName());
        victim.sendMessage("§cYou were killed by §e" + killer.getName());
    }

    // -------------------------
    // WEAPON DAMAGE
    // -------------------------
    private double getWeaponDamage(ItemStack item) {
        if (item == null) return 1;

        switch (item.getType()) {
            case WOOD_SWORD: return 4;
            case STONE_SWORD: return 5;
            case IRON_SWORD: return 6;
            case DIAMOND_SWORD: return 7;

            case WOOD_AXE: return 5;
            case STONE_AXE: return 6;
            case IRON_AXE: return 7;
            case DIAMOND_AXE: return 8;

            default: return 1;
        }
    }

    // -------------------------
    // ARMOR POINTS
    // -------------------------
    private double getArmorPoints(Player p) {
        PlayerInventory inv = p.getInventory();
        double points = 0;

        points += getArmorValue(inv.getHelmet());
        points += getArmorValue(inv.getChestplate());
        points += getArmorValue(inv.getLeggings());
        points += getArmorValue(inv.getBoots());

        return points;
    }

    private double getArmorValue(ItemStack item) {
        if (item == null) return 0;

        switch (item.getType()) {
            case LEATHER_HELMET: return 1;
            case LEATHER_CHESTPLATE: return 3;
            case LEATHER_LEGGINGS: return 2;
            case LEATHER_BOOTS: return 1;

            case GOLD_HELMET: return 2;
            case GOLD_CHESTPLATE: return 5;
            case GOLD_LEGGINGS: return 3;
            case GOLD_BOOTS: return 1;

            case CHAINMAIL_HELMET: return 2;
            case CHAINMAIL_CHESTPLATE: return 5;
            case CHAINMAIL_LEGGINGS: return 4;
            case CHAINMAIL_BOOTS: return 1;

            case IRON_HELMET: return 2;
            case IRON_CHESTPLATE: return 6;
            case IRON_LEGGINGS: return 5;
            case IRON_BOOTS: return 2;

            case DIAMOND_HELMET: return 3;
            case DIAMOND_CHESTPLATE: return 8;
            case DIAMOND_LEGGINGS: return 6;
            case DIAMOND_BOOTS: return 3;

            default: return 0;
        }
    }

    private int getArmorGoldValue(Player victim) {
        int gold = 0;

        ItemStack[] armor = victim.getInventory().getArmorContents();
        for (ItemStack piece : armor) {
            if (piece == null) continue;

            Material type = piece.getType();

            switch (type) {
                case DIAMOND_HELMET:
                case DIAMOND_CHESTPLATE:
                case DIAMOND_LEGGINGS:
                case DIAMOND_BOOTS:
                    gold += 4;
                    break;

                case IRON_HELMET:
                case IRON_CHESTPLATE:
                case IRON_LEGGINGS:
                case IRON_BOOTS:
                    gold += 2;
                    break;

                default:
                    break;
            }
        }

        return gold;
    }

    private void handleKill(Player killer, Player victim) {

        // Reset victim health
        victim.setHealth(victim.getMaxHealth());

        // Stats
        Stats killerStats = StatsManager.getStats(killer.getUniqueId());
        Stats victimStats = StatsManager.getStats(victim.getUniqueId());

        killerStats.addKill();
        victimStats.addDeath();

        int streak = killerStats.getKillstreak();

        // XP
        int baseXP = 5;
        int bonusXP = streak;
        int totalXP = baseXP + bonusXP;
        // GOLD SYSTEM
        int baseGold = 10;

// First 3 kills of streak give +4 each
        int streakBonus = Math.min(streak, 3) * 4;

// Armor bonus
        int armorBonus = getArmorGoldValue(victim);

// Final gold
        int totalGold = baseGold + streakBonus + armorBonus;

// Add gold to stats
        killerStats.addGold(totalGold);

        killerStats.addXP(totalXP);

        killer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "KILL! " +
                ChatColor.RESET + ChatColor.GRAY + "on " + victim.getDisplayName() +
                ChatColor.AQUA + " +" + totalXP + " XP " + ChatColor.GOLD + "+" + totalGold + "g ");

        // Gold placeholder
        int gold = 0;

        // Create recap
        KillRecapManager.createRecap(victim, killer, totalXP, gold, streak);

        // Clickable message
        TextComponent msg = new TextComponent(
                ChatColor.RED + "" + ChatColor.BOLD + "YOU DIED! " +
                        ChatColor.GRAY + "(Click for Kill Recap)"
        );

        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/killrecap"));

        victim.spigot().sendMessage(msg);


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ScoreboardTask.removePlayer(event.getPlayer().getUniqueId());
    }




}