package com.thepit.Prestige;

public enum PrestigeData {

    P0(0, 10000),
    P1(1, 20000),
    P2(2, 20000),
    P3(3, 20000),
    P4(4, 30000),
    P5(5, 35000),
    P6(6, 40000),
    P7(7, 45000),
    P8(8, 50000),
    P9(9, 60000),
    P10(10, 70000),
    P11(11, 80000),
    P12(12, 90000),
    P13(13, 100000),
    P14(14, 125000),
    P15(15, 150000),
    P16(16, 175000),
    P17(17, 200000),
    P18(18, 250000),
    P19(19, 300000),
    P20(20, 350000),
    P21(21, 400000),
    P22(22, 500000),
    P23(23, 600000),
    P24(24, 700000),
    P25(25, 800000),
    P26(26, 900000),
    P27(27, 1000000),
    P28(28, 1000000),
    P29(29, 1000000),
    P30(30, 1000000),
    P31(31, 1000000),
    P32(32, 1000000),
    P33(33, 1000000),
    P34(34, 1000000),
    P35(35, 2000000),
    P36(36, 2000000),
    P37(37, 2000000),
    P38(38, 2000000),
    P39(39, 2000000),
    P40(40, 2000000),
    P41(41, 2000000),
    P42(42, 2000000),
    P43(43, 2000000),
    P44(44, 2000000),
    P45(45, 2000000),
    P46(46, 2000000),
    P47(47, 2000000),
    P48(48, 2000000),
    P49(49, 2000000),
    P50(50, -1); // No gold requirement

    private final int prestige;
    private final int goldRequired;

    PrestigeData(int prestige, int goldRequired) {
        this.prestige = prestige;
        this.goldRequired = goldRequired;
    }

    public int getPrestige() {
        return prestige;
    }

    public int getGoldRequired() {
        return goldRequired;
    }

    public static PrestigeData get(int prestige) {
        for (PrestigeData data : values()) {
            if (data.prestige == prestige) return data;
        }
        return null;
    }
}