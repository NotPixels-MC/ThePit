package com.thepit;

import com.thepit.Megastreaks.MegastreakMenu;
import com.thepit.Megastreaks.MegastreakTypes;
import com.thepit.Perks.PerkInfo;
import com.thepit.Perks.PerkMenu;
import com.thepit.Perks.PerkRegistry;
import com.thepit.Utils.EnchantUtils;
import com.thepit.Utils.XPUtils;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Events implements Listener {

    private final Map<UUID, Map<UUID, Double>> damageMap = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        // Check if victim is a bot

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        Stats attackerstats = Main.getInstance().getStats(attacker.getUniqueId());

        // Keep vanilla knockback + animation
        event.setDamage(0); // prevents vanilla damage but keeps KB

        doPerunDamage(victim, attacker);
        getSpikeyDamage(attacker);

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

        baseDamage = baseDamage * getSharpDamage(attacker);
        baseDamage = baseDamage * getPainFocusDamage(attacker);


        // armor reduction
        double armorPoints = getArmorPoints(victim);
        double reduction = armorPoints * 0.04;
        if (reduction > 0.80) reduction = 0.80;

        double finalDamage = baseDamage * (1 - reduction);
        DoLifestealHeals(attacker, baseDamage);
        // after you calculate finalDamage, insert this:

        // capture BEFORE damage
        double healthBefore = victim.getHealth();
        double healthAfter = Math.max(0, healthBefore - finalDamage);
        // Track damage for assists
        damageMap.putIfAbsent(victim.getUniqueId(), new HashMap<>());
        Map<UUID, Double> attackers = damageMap.get(victim.getUniqueId());

        attackers.put(attacker.getUniqueId(),
                attackers.getOrDefault(attacker.getUniqueId(), 0.0) + finalDamage
        );
        // death

        // apply damage
        if (healthAfter <= 0) {
            event.setCancelled(true);
            healthAfter = victim.getMaxHealth();
            handleKill(attacker, victim);
            return;
        } else {
        victim.setHealth(healthAfter); }

        if (checkExecutioner(attacker, victim, healthAfter)) {
            event.setCancelled(true);
            healthAfter = victim.getMaxHealth();
            handleKill(attacker, victim);
            float pitch = 0.5f + (float)(Math.random() * 0.3);

            attacker.playSound(
                    attacker.getLocation(),
                    Sound.VILLAGER_DEATH,
                    2.0f,
                    pitch
            );


            for (int i = 0; i < 30; i++) {

                // Random offset between -0.2 and +0.2
                double offsetX = (Math.random() * 0.4) - 0.2;

                double offsetZ = (Math.random() * 0.4) - 0.2;

                Location loc = victim.getLocation().clone().add(offsetX, 1, offsetZ);

                attacker.spigot().playEffect(
                        loc,
                        Effect.TILE_DUST,
                        Material.REDSTONE_BLOCK.getId(),
                        0,      // data
                        0, 0, 0, // offsetX, offsetY, offsetZ (0 = no spread)
                        0,      // speed (0 = no velocity)
                        1,      // particle count per burst
                        50      // radius (ignored for single-player)
                );

            }

            return;
        }


        // hearts
        int maxHearts = (int) Math.ceil(victim.getMaxHealth() / 2.0);
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

    private boolean isBlockingSword(Player p) {
        return p.isBlocking();
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
            case GOLD_SWORD: return 6.5;

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

                case CHAINMAIL_HELMET:
                    gold += 1;
                    break;

                default:
                    break;
            }
        }

        return gold;
    }

    private void handleKill(Player killer, Player victim) {

        Stats killerStats = Main.getInstance().getStats(killer.getUniqueId());
        Stats victimStats = Main.getInstance().getStats(victim.getUniqueId());

        killerStats.addKill();
        victimStats.addDeath();

        int streak = killerStats.getKillstreak();

        // Megastreak activation
        MegastreakTypes selected = killerStats.getSelectedMegastreak();

        if (streak == selected.getRequiredKills()) {
            killerStats.setMegastreak(selected);
            killer.sendMessage("§6MEGASTREAK ACTIVATED: §e" + selected.name());
            killer.getWorld().strikeLightningEffect(killer.getLocation());
        }


        // Calculate rewards
        RewardResult rewards = calculateRewards(killer, victim);

        killerStats.addGold(rewards.gold);
        killerStats.addXP(rewards.xp, killer);

        // Assist rewards
        Map<UUID, Double> attackers = damageMap.get(victim.getUniqueId());
        if (attackers != null) {

            double maxHealth = victim.getMaxHealth();

            for (Map.Entry<UUID, Double> entry : attackers.entrySet()) {
                UUID assisterId = entry.getKey();
                double damageDone = entry.getValue();

                if (assisterId.equals(killer.getUniqueId())) continue;

                Player assister = Bukkit.getPlayer(assisterId);
                if (assister == null) continue;

                double assistPercent = damageDone / maxHealth;

                if (assistPercent >= 0.01) {
                    handleAssistKill(assister, victim, assistPercent);
                }
            }

            damageMap.remove(victim.getUniqueId());
        }

        // Ghead perk
        if (killerStats.hasEquipped("ghead")) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner("kenyon03");
            meta.setDisplayName(ChatColor.GOLD + "Golden Head");
            head.setItemMeta(meta);
            killer.getInventory().addItem(head);
        }

        // Sound + message
        killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 2.0f, 1.8f);

        killer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "KILL! " +
                ChatColor.RESET + ChatColor.GRAY + "on " + victim.getDisplayName() +
                ChatColor.AQUA + " +" + rewards.xp + " XP " +
                ChatColor.GOLD + "+" + rewards.gold + "g ");

        EnchantUtils.handleDeathInventory(victim);

        victim.getInventory().setChestplate (new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        victim.getInventory().setLeggings (new ItemStack(Material.CHAINMAIL_LEGGINGS));
        victim.getInventory().setBoots (new ItemStack(Material.CHAINMAIL_BOOTS));
        victim.getInventory().setItem (0, new ItemStack(Material.IRON_SWORD));

// Respawn victim
        respawnPlayer(victim);

        // Respawn victim
        respawnPlayer(victim);
    }

    private void handleAssistKill(Player assister, Player victim, double assistPercent) {

        Stats assisterStats = Main.getInstance().getStats(assister.getUniqueId());
        Stats victimStats = Main.getInstance().getStats(victim.getUniqueId());


        // Base rewards (same as kill, but no streak bonuses)
        int baseXP = 5;
        int baseGold = 10;

        // Armor gold bonus still applies
        int armorBonus = getArmorGoldValue(victim);

        int totalGold = baseGold + armorBonus;
        int totalXP = baseXP;

        // Apply assist scaling
        totalGold = (int)(totalGold * assistPercent);
        totalXP = (int)(totalXP * assistPercent);

        // Low‑level penalty
        if (victimStats.getLevel() <= 20) {
            totalGold = (int)(totalGold * 0.9);
            totalXP = (int)(totalXP * 0.9);
        }

        // Give rewards
        assisterStats.addGold(totalGold);
        assisterStats.addXP(totalXP, assister);

        // Send assist message
        assister.sendMessage(String.format(
                "§aASSIST! §e%d%% on [%s] +%dXP +%dg",
                (int)(assistPercent * 100),
                victim.getName(),
                totalXP,
                totalGold
        ));
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Stats stats = Main.getInstance().getStats(player.getUniqueId());


        String title = event.getView().getTitle();

        /*
         * ============================
         *  UPGRADE MENU (perk slots)
         * ============================
         */
        if (title.equals("Upgrades")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            int level = stats.getLevel();

            if (slot == 12) {
                if (level < 10) player.sendMessage("§c§lLOCKED! §7This perk slot is locked.");
                else new PerkMenu().openPerkMenu(player, 1);
            }

            if (slot == 13) {
                if (level < 35) player.sendMessage("§c§lLOCKED! §7This perk slot is locked.");
                else new PerkMenu().openPerkMenu(player, 2);
            }

            if (slot == 14) {
                if (level < 70) player.sendMessage("§c§lLOCKED! §7This perk slot is locked.");
                else new PerkMenu().openPerkMenu(player, 3);
            }

            if (slot == 15) {
                // Open killstreak menu
                MegastreakMenu.open(player);
                return;
            }

            return;
        }

        /*
         * ============================
         *  PERK MENU (6-row)
         * ============================
         */
        if (!title.equals("Choose a perk")) return;

        event.setCancelled(true);
        int clicked = event.getRawSlot();

        // Back button
        if (clicked == 49) {
            player.closeInventory();
            new UpgradesMenu().openUpgradesMenu(player);
            return;
        }

        // Ignore filler glass
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.STAINED_GLASS_PANE) return;

        // Determine which perk was clicked by matching icon + name
        PerkInfo chosen = null;

        for (PerkInfo perk : PerkRegistry.perks.values()) {
            if (clickedItem.getType() == perk.icon) {
                String name = clickedItem.getItemMeta().getDisplayName().replace("§a", "").replace("§c", "");
                if (name.equalsIgnoreCase(perk.displayName)) {
                    chosen = perk;
                    break;
                }
            }
        }

        if (chosen == null) return;

        // Which perk slot is being edited?
        Integer perkSlot = PerkMenu.editingSlot.get(player.getUniqueId());
        if (perkSlot == null) {
            player.sendMessage("§cError: No perk slot selected.");
            return;
        }

        /*
         * ============================
         *  UNLOCK / SELECT LOGIC
         * ============================
         */

        boolean unlocked = stats.hasUnlocked(chosen.id);

        // Unlocking
        if (!unlocked) {

            if (stats.getLevel() < chosen.requiredLevel) {
                player.sendMessage("§cYou must be level §b" + chosen.requiredLevel + " §cto unlock this perk.");
                return;
            }

            if (stats.getGold() < chosen.price) {
                player.sendMessage("§cYou need §6" + chosen.price + "g §cto unlock this perk.");
                return;
            }

            // Prestige requirement
            if (stats.getPrestige() < chosen.requiredPrestige) {
                player.sendMessage("§cYou must be prestige §b" + chosen.requiredPrestige + " §cto unlock this perk.");
                return;
            }

            stats.setGold(stats.getGold() - chosen.price);
            stats.unlockPerk(chosen.id);

            player.sendMessage("§aUnlocked §e" + chosen.displayName + "§a!");
        }

        // Selecting
        stats.setPerk(perkSlot, chosen.id);
        player.sendMessage("§aEquipped §e" + chosen.displayName + " §ato slot §e" + perkSlot);

        player.closeInventory();
        PerkMenu.editingSlot.remove(player.getUniqueId());
    }

    public int romanToInt(String roman) {
        roman = roman.toUpperCase();
        int sum = 0;
        int prev = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int value = 0;
            switch (roman.charAt(i)) {
                case 'I': value = 1; break;
                case 'V': value = 5; break;
                case 'X': value = 10; break;
                case 'L': value = 50; break;
                case 'C': value = 100; break;
                case 'D': value = 500; break;
                case 'M': value = 1000; break;
            }

            if (value < prev) sum -= value;
            else sum += value;

            prev = value;
        }
        return sum;
    }

    //REWARDS
    private RewardResult calculateRewards(Player killer, Player victim) {

        Stats killerStats = Main.getInstance().getStats(killer.getUniqueId());
        Stats victimStats = Main.getInstance().getStats(victim.getUniqueId());
        MegastreakTypes ms = killerStats.getMegastreak();

        int maxXP = 400;
        int maxGold = 2500;
        int streak = killerStats.getKillstreak();
        int XPBoostLevel = getXPBoost(killer);

        // XP calculation

        int baseXP = 5;
        int streakXPBonus = 0;

        streakXPBonus += XPUtils.getStreakXP(streak, getSweaty(killer));

        int GivenXP = baseXP + streakXPBonus;
        if (ms != null && streak >= ms.getRequiredKills()) {
            GivenXP = (int)(GivenXP * (1 + ms.getExtraXP() / 100.0));
        }

        if (GivenXP > maxXP) GivenXP = maxXP;

        int totalXP = GivenXP;
        if (XPBoostLevel > 0) {
            totalXP = (int)(totalXP * (1 + 0.1 * XPBoostLevel));
        }

        // Gold calculation
        int baseGold = 10;
        int armorBonus = getArmorGoldValue(victim);

        int totalGold = baseGold + armorBonus;

        if (ms != null) {
            totalGold = (int)(totalGold * (1 + ms.getExtraGOLD() / 100.0));
        }

        if (totalGold > maxGold) totalGold = maxGold;

        // Low-level penalty
        if (victimStats.getLevel() <= 20) {
            totalXP = (int)(totalXP * 0.9);
            totalGold = (int)(totalGold * 0.9);
        }


        if (ms != null && streak >= ms.getRequiredKills()) {
            totalGold = (int)(totalGold * (1 + ms.getExtraGOLD() / 100.0));
        }


        return new RewardResult(totalXP, totalGold);
    }

    private static class RewardResult {
        public final int xp;
        public final int gold;

        public RewardResult(int xp, int gold) {
            this.xp = xp;
            this.gold = gold;
        }
    }

    private void respawnPlayer(Player victim) {
        FileConfiguration config = Main.getInstance().getConfig();

        World world = Bukkit.getWorld(config.getString("player-spawn.world"));
        if (world == null) {
            victim.sendMessage("§cSpawn world not found! Teleporting to default world.");
            world = Bukkit.getWorlds().get(0);
        }

        Location spawn = new Location(
                world,
                config.getDouble("player-spawn.x"),
                config.getDouble("player-spawn.y"),
                config.getDouble("player-spawn.z"),
                (float) config.getDouble("player-spawn.yaw"),
                (float) config.getDouble("player-spawn.pitch")
        );

        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            victim.spigot().respawn();
            victim.teleport(spawn);
            victim.setHealth(victim.getMaxHealth());
        });
    }

    public boolean checkExecutioner(Player attacker, Player victim, double healthAfter) {
        if
                (hasLoreContaining(attacker.getItemInHand(), "executioner i".toLowerCase()) && healthAfter < 1.0
        ||
                hasLoreContaining(attacker.getItemInHand(), "executioner) ii".toLowerCase()) && healthAfter < 1.5
        ||
                hasLoreContaining(attacker.getItemInHand(), "executioner iii".toLowerCase()) && healthAfter < 2.0)

        {
            return true;
        }

        return false;
    }

    public int getSweaty(Player p) {
        int level = 0;

        if (hasLoreContaining(p.getItemInHand(), "sweaty i".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "sweaty ii".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "sweaty iii".toLowerCase())) level += 1;

        if (hasLoreContaining(p.getInventory().getLeggings(), "sweaty i".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getInventory().getLeggings(), "sweaty ii".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getInventory().getLeggings(), "sweaty iii".toLowerCase())) level += 1;

        return level;
    }

    public int getXPBoost(Player p) {
        int level = 0;

        if (hasLoreContaining(p.getItemInHand(), "xp boost i".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "xp boost ii".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "xp boost iii".toLowerCase())) level += 1;

        if (hasLoreContaining(p.getInventory().getLeggings(), "xp boost i".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getInventory().getLeggings(), "xp boost ii".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getInventory().getLeggings(), "xp boost iii".toLowerCase())) level += 1;

        return level;

    }

    public int getPerunLevel(Player p) {
        int level = 0;

        if (hasLoreContaining(p.getItemInHand(), "s wrath i".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "s wrath ii".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "s wrath iii".toLowerCase())) level += 1;

        return level;

    }

    public int getLifestealLevel(Player p) {
        int level = 0;

        if (hasLoreContaining(p.getItemInHand(), "lifesteal i".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "lifesteal ii".toLowerCase())) level += 1;
        if (hasLoreContaining(p.getItemInHand(), "lifesteal iii".toLowerCase())) level += 1;

        return level;

    }

    public double getSpikeyDamage(Player p) {
        double damage = 0.0;

        if (hasLoreContaining(p.getInventory().getHelmet(), "spikey i".toLowerCase())) damage = 0.125;
        if (hasLoreContaining(p.getInventory().getHelmet(), "spikey ii".toLowerCase())) damage = 0.25;
        if (hasLoreContaining(p.getInventory().getHelmet(), "spikey iii".toLowerCase())) damage = 0.5;
        p.playSound(p.getLocation(), Sound.VILLAGER_HIT, 0.35f, 2.0f);

        return damage ;
    }

    public Double getSharpDamage(Player p) {
        double damage = 1.0;

        if (hasLoreContaining(p.getItemInHand(), "sharp i".toLowerCase())) damage = 1.04;
        if (hasLoreContaining(p.getItemInHand(), "sharp ii".toLowerCase())) damage = 1.07;
        if (hasLoreContaining(p.getItemInHand(), "sharp iii".toLowerCase())) damage = 1.12;

        return damage;
    }

    public Double getPainFocusDamage(Player p) {
        double damage = 1.0;
        double missingHealth = p.getMaxHealth() - p.getHealth();

        if (hasLoreContaining(p.getItemInHand(), "pain focus i".toLowerCase())) damage = missingHealth * 0.01;
        if (hasLoreContaining(p.getItemInHand(), "pain focus ii".toLowerCase())) damage = missingHealth * 0.02;
        if (hasLoreContaining(p.getItemInHand(), "pain focus iii".toLowerCase())) damage = missingHealth * 0.05;

        return damage;
    }

    public void doPerunDamage(Player victim, Player attacker) {
        int level = getPerunLevel(attacker);
        Stats attackerstats = Main.getInstance().getStats(attacker.getUniqueId());

// Only increment combo if attacker actually has Perun
        if (level > 0) {
            attackerstats.addCombo();
        }

        int combo = attackerstats.getPeruncombo();

// ----------------------
// PERUN I (5 hits)
// ----------------------
        if (level == 1 && combo == 5) {

            attackerstats.resetCombo();

            victim.getWorld().strikeLightningEffect(victim.getLocation());

            if (victim.getHealth() < 1.5) {
                handleKill(attacker, victim);
            } else {
                victim.setHealth(victim.getHealth() - 1.5);
            }
        }

// ----------------------
// PERUN II (4 hits)
// ----------------------
        else if (level == 2 && combo == 4) {

            attackerstats.resetCombo();

            victim.getWorld().strikeLightningEffect(victim.getLocation());

            if (victim.getHealth() < 2.0) {
                handleKill(attacker, victim);
            } else {
                victim.setHealth(victim.getHealth() - 2.0);
            }
        }

// ----------------------
// PERUN III (4 hits + armor bonus)
// ----------------------
        else if (level == 3 && combo == 4) {

            attackerstats.resetCombo();

            double extraPerun = 1.0;

            // Helmet
            if (victim.getInventory().getHelmet() != null &&
                    victim.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) {
                extraPerun += 1.0;
            }

            // Chestplate
            if (victim.getInventory().getChestplate() != null &&
                    victim.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) {
                extraPerun += 1.0;
            }

            // Leggings
            if (victim.getInventory().getLeggings() != null &&
                    victim.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
                extraPerun += 1.0;
            }

            // Boots
            if (victim.getInventory().getBoots() != null &&
                    victim.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) {
                extraPerun += 1.0;
            }

            victim.getWorld().strikeLightningEffect(victim.getLocation());

            if (victim.getHealth() < extraPerun) {
                handleKill(attacker, victim);
            } else {
                victim.setHealth(victim.getHealth() - extraPerun);
            }
        }
    }

    public void DoLifestealHeals(Player attacker, double damageDealt) {
        int level = getLifestealLevel(attacker);

        if (level == 0) return;

        double healAmount = 0.0;

        if (level == 1) healAmount = 0.04 * damageDealt;
        if (level == 2) healAmount = 0.08 * damageDealt;
        if (level == 3) healAmount = 0.13 * damageDealt;

        if (healAmount > 1.5) healAmount = 1.5;

        double healthCheck = 20.0;
        healthCheck -= healAmount;
        if (attacker.getHealth() - healthCheck <= 0.0) return;
        attacker.setHealth(attacker.getHealth() + healAmount);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerLooseHunger(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerUseItem(PlayerItemDamageEvent e) {
        e.setCancelled(true);
    }

}

