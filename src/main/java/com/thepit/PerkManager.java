package com.thepit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PerkManager {

    // Track which perks a player has unlocked
    private static final Map<UUID, Set<Perk>> unlockedPerks = new HashMap<>();

    // Track which slots a player has unlocked
    private static final Map<UUID, Set<PerkSlot>> unlockedSlots = new HashMap<>();

    // Track which perk is selected (ready to assign to slots)
    private static final Map<UUID, Perk> selectedPerk = new HashMap<>();

    // Track which perk is equipped in each slot (PerkSlot -> Perk)
    private static final Map<UUID, Map<PerkSlot, Perk>> equippedPerks = new HashMap<>();

    // ============================================
    // PERK UNLOCK/PURCHASE METHODS
    // ============================================

    public static void unlockPerk(UUID uuid, Perk perk) {
        unlockedPerks.computeIfAbsent(uuid, k -> new HashSet<>()).add(perk);
    }

    public static boolean isPerkUnlocked(UUID uuid, Perk perk) {
        return unlockedPerks.getOrDefault(uuid, new HashSet<>()).contains(perk);
    }

    public static Set<Perk> getUnlockedPerks(UUID uuid) {
        return new HashSet<>(unlockedPerks.getOrDefault(uuid, new HashSet<>()));
    }

    // ============================================
    // SELECTED PERK METHODS
    // ============================================

    public static void selectPerk(UUID uuid, Perk perk) {
        if (isPerkUnlocked(uuid, perk)) {
            selectedPerk.put(uuid, perk);
        }
    }

    public static Perk getSelectedPerk(UUID uuid) {
        return selectedPerk.get(uuid);
    }

    public static void deselectPerk(UUID uuid) {
        selectedPerk.remove(uuid);
    }

    // ============================================
    // SLOT UNLOCK METHODS
    // ============================================

    public static boolean isSlotUnlocked(UUID uuid, PerkSlot slot) {
        // All slots are always unlocked
        return true;
    }

    public static Set<PerkSlot> getUnlockedSlots(UUID uuid) {
        // All slots always available
        HashSet<PerkSlot> allSlots = new HashSet<>();
        allSlots.add(PerkSlot.SLOT_1);
        allSlots.add(PerkSlot.SLOT_2);
        allSlots.add(PerkSlot.SLOT_3);
        return allSlots;
    }

    // ============================================
    // EQUIP/SLOT METHODS
    // ============================================

    public static void equipPerkInSlot(UUID uuid, PerkSlot slot, Perk perk) {
        if (!isPerkUnlocked(uuid, perk) || !isSlotUnlocked(uuid, slot)) {
            return; // Can't equip if perk or slot is locked
        }
        equippedPerks.computeIfAbsent(uuid, k -> new HashMap<>()).put(slot, perk);
    }

    public static Perk getEquippedPerkInSlot(UUID uuid, PerkSlot slot) {
        return equippedPerks.getOrDefault(uuid, new HashMap<>()).get(slot);
    }

    public static void unequipSlot(UUID uuid, PerkSlot slot) {
        Map<PerkSlot, Perk> slots = equippedPerks.getOrDefault(uuid, new HashMap<>());
        slots.remove(slot);
    }

    public static Map<PerkSlot, Perk> getAllEquippedPerks(UUID uuid) {
        return new HashMap<>(equippedPerks.getOrDefault(uuid, new HashMap<>()));
    }
}

