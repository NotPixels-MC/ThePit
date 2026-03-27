package com.thepit;

public enum PerkSlot {
    SLOT_1("Perk Slot 1", 1),
    SLOT_2("Perk Slot 2", 2),
    SLOT_3("Perk Slot 3", 3);

    private final String name;
    private final int slotNumber;

    PerkSlot(String name, int slotNumber) {
        this.name = name;
        this.slotNumber = slotNumber;
    }

    public String getSlotName() {
        return name;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}

