package com.thepit;

import com.thepit.Perks.PerkInfo;
import com.thepit.Perks.PerkMenu;
import com.thepit.Perks.PerkRegistry;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import static org.spigotmc.SpigotConfig.maxHealth;

public class Events implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        // Keep vanilla knockback + animation
        event.setDamage(0); // prevents vanilla damage but keeps KB

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
        double healthBefore = victim.getHealth();
        double healthAfter = Math.max(0, healthBefore - finalDamage);

        // EXECUTIONER
        int execLevel = getExecutionerLevel(attacker.getItemInHand());

        if (execLevel > 0) {
            double threshold = getExecutionerThreshold(execLevel);

            if (healthAfter <= threshold) {

                double trueDamage = getExecutionerDamage(execLevel);
                double newHealth = victim.getHealth() - trueDamage;

                // lethal execution
                if (newHealth <= 0) {

                    // particles (killer only)
                    attacker.playEffect(
                            victim.getLocation().add(0, 1, 0),
                            Effect.ITEM_BREAK,
                            Material.NETHER_WARTS.getId()
                    );

                    // sound (killer only)
                    float pitch = 0.5f + (float) (Math.random() * 0.4);
                    attacker.playSound(
                            victim.getLocation(),
                            Sound.VILLAGER_DEATH,
                            1.0f,
                            pitch
                    );

                    // force death

                    // run your kill handler
                    handleKill(attacker, victim);

                    attacker.sendMessage("§cExecutioner! §7(Executed)");
                    return;
                }

                // non-lethal executioner true damage
                victim.setHealth(newHealth);
                attacker.sendMessage("§cExecutioner! §7(" + trueDamage + " true dmg)");
                return;
            }
        }

        // Apply normal custom damage
        victim.setHealth(healthAfter);


        // death
        if (healthAfter <= 0) {
            handleKill(attacker, victim);
            return;
        }

        // apply damage
        victim.setHealth(healthAfter);

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
        killerStats.addXP(totalXP, killer);

        if (killerStats.hasEquipped("ghead")) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner("kenyon03");
            meta.setDisplayName(ChatColor.GOLD + "Golden Head");
            head.setItemMeta(meta);

            killer.getInventory().addItem(head);
        }

        killer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "KILL! " +
                ChatColor.RESET + ChatColor.GRAY + "on " + victim.getDisplayName() +
                ChatColor.AQUA + " +" + totalXP + " XP " + ChatColor.GOLD + "+" + totalGold + "g ");

        TextComponent msg = new TextComponent(
                ChatColor.RED + "" + ChatColor.BOLD + "YOU DIED! " +
                        ChatColor.GRAY + "(Click for Kill Recap)"
        );

        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/killrecap"));
        victim.spigot().sendMessage(msg);
        FileConfiguration config = Main.getInstance().getConfig();

        World world = Bukkit.getWorld(config.getString("player-spawn.world"));
        double x = config.getDouble("player-spawn.x");
        double y = config.getDouble("player-spawn.y");
        double z = config.getDouble("player-spawn.z");
        float yaw = (float) config.getDouble("player-spawn.yaw");
        float pitch = (float) config.getDouble("player-spawn.pitch");

        Location spawn = new Location(world, x, y, z, yaw, pitch);

        victim.teleport(spawn);
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
        Stats stats = StatsManager.getStats(player.getUniqueId());

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
















    public int getExecutionerLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return 0;

        for (String line : meta.getLore()) {
            line = ChatColor.stripColor(line);

            if (line.startsWith("Executioner ")) {
                String roman = line.replace("Executioner ", "").trim();
                return romanToInt(roman);
            }
        }
        return 0;
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


    public double getExecutionerThreshold(int level) {
        switch (level) {
            case 1: return 2.0; // 1 heart
            case 2: return 3.0; // 1.5 hearts
            case 3: return 4.0; // 2 hearts
            default:
                return level * 1.5; // scaling for levels 4+
        }
    }

    public double getExecutionerDamage(int level) {
        switch (level) {
            case 1: return 2.0;
            case 2: return 3.0;
            case 3: return 4.0;
            default:
                return level * 2.0; // scaling for levels 4+
        }
    }













}