package com.thepit.Perks;

import com.thepit.Main;
import com.thepit.Stats;
import com.thepit.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PerkEffects implements Listener {

    private final Main plugin;

    public PerkEffects(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        applyPermanentPerks(e.getPlayer());
        giveSpawnItems(e.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            applyPermanentPerks(e.getPlayer());
            giveSpawnItems(e.getPlayer());
        }, 1L);
    }

    // ============================
    //  PERMANENT PASSIVE PERKS
    // ============================
    private void applyPermanentPerks(Player p) {
        Stats stats = Main.getInstance().getStats(p.getUniqueId());


        // Thick (+2 hearts)
        if (stats.hasEquipped("thick")) {
            p.setMaxHealth(24);
        } else {
            p.setMaxHealth(20);
        }

        // Gladiator, Marathon, etc. will go here later
    }

    // ============================
    //  SPAWN ITEMS PERKS
    // ============================
    private void giveSpawnItems(Player p) {
        Stats stats = Main.getInstance().getStats(p.getUniqueId());


        // Fishing Rod
        if (stats.hasEquipped("rod")) {
            p.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
        }

        // Lava Bucket
        if (stats.hasEquipped("lava")) {
            p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
        }

        // Mineman
        if (stats.hasEquipped("mineman")) {
            ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
            pick.addEnchantment(Enchantment.DIG_SPEED, 4);
            p.getInventory().addItem(pick);

            p.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 24));
        }

        // Safety First
        if (stats.hasEquipped("safety")) {
            p.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        }
    }
}