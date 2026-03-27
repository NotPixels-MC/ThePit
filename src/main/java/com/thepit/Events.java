package com.thepit;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Events implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        // keep vanilla knockback
        event.setDamage(0);

        double baseDamage = getWeaponDamage(attacker.getItemInHand());

        // crit
        if (isCriticalHit(attacker)) {
            baseDamage *= 1.5;
            attacker.sendMessage("§6CRIT! §e+" + (baseDamage * 0.5) + " dmg");
        }

        // lore bonus
        if (hasLoreContaining(attacker.getItemInHand(), "test")) {
            baseDamage += 2;
            attacker.sendMessage("§dLore bonus! §e+2 damage");
        }

        // armor reduction
        double armorPoints = getArmorPoints(victim);
        double reduction = armorPoints * 0.04;
        if (reduction > 0.80) reduction = 0.80;

        double finalDamage = baseDamage * (1 - reduction);

        // capture BEFORE damage
        double maxHealth = victim.getMaxHealth();
        double healthBefore = victim.getHealth();
        double healthAfter = Math.max(0, healthBefore - finalDamage);

        // death
        if (healthAfter <= 0) {
            handleKill(attacker, victim);
            return;
        }

        // apply damage
        victim.setHealth(healthAfter);

        // hearts
        int maxHearts = (int) Math.ceil(maxHealth / 2.0);
        int heartsBefore = (int) Math.ceil(healthBefore / 2.0);
        int heartsAfter = (int) Math.ceil(healthAfter / 2.0);
        int heartsLost = heartsBefore - heartsAfter;

        StringBuilder bar = new StringBuilder();

        // dark red = hearts left
        for (int i = 0; i < heartsAfter; i++) {
            bar.append("§4❤");
        }

        // bright red = hearts lost from this hit
        for (int i = 0; i < heartsLost; i++) {
            bar.append("§c❤");
        }

        // black = missing hearts
        for (int i = 0; i < (maxHearts - heartsAfter - heartsLost); i++) {
            bar.append("§0❤");
        }

        String actionBar = "§eDMG: §c" + finalDamage + "   §eHP: " + bar.toString();
        sendActionBar(attacker, actionBar);
    }

    private boolean isCriticalHit(Player p) {
        return p.getFallDistance() > 0 &&
                !p.isOnGround() &&
                !p.isInsideVehicle() &&
                !p.isSprinting() &&
                !p.isBlocking();
    }

    private boolean hasLoreContaining(ItemStack item, String text) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        for (String line : meta.getLore()) {
            if (line.toLowerCase().contains(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

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

        victim.setHealth(victim.getMaxHealth());

        Stats killerStats = StatsManager.getStats(killer.getUniqueId());
        Stats victimStats = StatsManager.getStats(victim.getUniqueId());

        killerStats.addKill();
        victimStats.addDeath();

        int streak = killerStats.getKillstreak();

        int baseXP = 5;
        int bonusXP = streak;
        int totalXP = baseXP + bonusXP;

        int baseGold = 10;
        int streakBonus = Math.min(streak, 3) * 4;
        int armorBonus = getArmorGoldValue(victim);
        int totalGold = baseGold + streakBonus + armorBonus;

        killerStats.addGold(totalGold);
        killerStats.addXP(totalXP);

        killer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "KILL! " +
                ChatColor.RESET + ChatColor.GRAY + "on " + victim.getDisplayName() +
                ChatColor.AQUA + " +" + totalXP + " XP " + ChatColor.GOLD + "+" + totalGold + "g ");

        KillRecapManager.createRecap(victim, killer, totalXP, totalGold, streak);

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

    public void sendActionBar(Player p, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}