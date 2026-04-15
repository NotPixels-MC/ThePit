package com.thepit.Mystics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MysticSounds {

    private final Plugin plugin;

    public MysticSounds(Plugin plugin) {
        this.plugin = plugin;
    }

    private void addSound(int tick, String sound, double volume, double pitch, Location loc) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(loc, sound, (float) volume, (float) pitch);
            }
        }, tick);
    }


    //This code is borrowed from PitSim's source code. I didn't have the time to write it myself, and I wanted to make sure the sound was exactly the same as Pit. Thanks PitSim for providing the source code for this sound effect, and thanks to the PitSim developers for making such an amazing plugin that I can learn from and use in my own plugin!
    public void playFreshDrop(Location loc) {
        int tick = 0;

        addSound(tick, "random.orb", 1.0, 1.7936507, loc);
        addSound(tick, "random.click", 0.25, 0.82539684, loc);
        addSound(tick, "random.successful_hit", 0.4, 0.82539684, loc);
        addSound(tick, "note.harp", 0.65, 0.82539684, loc);
        addSound(tick, "random.click", 0.25, 1.0476191, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.0476191, loc);
        addSound(tick, "note.harp", 0.65, 1.0476191, loc);
        addSound(tick, "random.click", 0.25, 1.1111112, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.1111112, loc);
        addSound(tick, "note.harp", 0.65, 1.1111112, loc);
        addSound(tick, "random.click", 0.25, 1.4126984, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.4126984, loc);
        addSound(tick, "note.harp", 0.65, 1.4126984, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 0.82539684, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.0476191, loc);

        tick += 1;
        addSound(tick, "random.click", 0.25, 0.93650794, loc);
        addSound(tick, "random.successful_hit", 0.4, 0.93650794, loc);
        addSound(tick, "note.harp", 0.65, 0.93650794, loc);
        addSound(tick, "random.click", 0.25, 1.1746032, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.1746032, loc);
        addSound(tick, "note.harp", 0.65, 1.1746032, loc);
        addSound(tick, "random.click", 0.25, 1.2539682, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.2539682, loc);
        addSound(tick, "note.harp", 0.65, 1.2539682, loc);
        addSound(tick, "random.click", 0.25, 1.5873016, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.5873016, loc);
        addSound(tick, "note.harp", 0.65, 1.5873016, loc);
        addSound(tick, "random.pop", 0.95, 1.1111112, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 0.93650794, loc);
        addSound(tick, "random.pop", 0.95, 1.4126984, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.1746032, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.2539682, loc);

        tick += 1;
        addSound(tick, "random.click", 0.25, 1.0476191, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.0476191, loc);
        addSound(tick, "note.harp", 0.65, 1.0476191, loc);
        addSound(tick, "random.click", 0.25, 1.3333334, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.3333334, loc);
        addSound(tick, "note.harp", 0.65, 1.3333334, loc);
        addSound(tick, "random.click", 0.25, 1.4126984, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.4126984, loc);
        addSound(tick, "note.harp", 0.65, 1.4126984, loc);
        addSound(tick, "random.click", 0.25, 1.7777778, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.7777778, loc);
        addSound(tick, "note.harp", 0.65, 1.7777778, loc);
        addSound(tick, "random.pop", 0.95, 1.5873016, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.0476191, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.3333334, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.4126984, loc);

        tick += 1;
        addSound(tick, "random.click", 0.25, 1.1746032, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.1746032, loc);
        addSound(tick, "note.harp", 0.65, 1.1746032, loc);
        addSound(tick, "random.click", 0.25, 1.4920635, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.4920635, loc);
        addSound(tick, "note.harp", 0.65, 1.4920635, loc);
        addSound(tick, "random.click", 0.25, 1.5873016, loc);
        addSound(tick, "random.successful_hit", 0.4, 1.5873016, loc);
        addSound(tick, "note.harp", 0.65, 1.5873016, loc);
        addSound(tick, "random.click", 0.25, 2.0, loc);
        addSound(tick, "random.successful_hit", 0.4, 2.0, loc);
        addSound(tick, "note.harp", 0.65, 2.0, loc);
        addSound(tick, "random.pop", 0.95, 1.7777778, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.1746032, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.4920635, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 1.5873016, loc);

        tick += 1;
        addSound(tick, "random.pop", 0.95, 2.0, loc);
    }
}
