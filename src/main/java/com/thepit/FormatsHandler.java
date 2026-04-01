package com.thepit;

public class FormatsHandler {

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

}
