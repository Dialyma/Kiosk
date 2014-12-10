package com.dlohaiti.dlokiosknew.db;

public class KioskDatabaseUtils {
    private KioskDatabaseUtils() {
    }

    static String[] matches(int match) {
        return matches(String.valueOf(match));
    }

    static String[] matches(long match) {
        return matches(String.valueOf(match));
    }

    static String[] matches(String match) {
        return new String[]{match};
    }

    static String where(String columnName) {
        return String.format("%s=?", columnName);
    }

    static String whereWithLike(String columnName) {
        return String.format("%s like ?", columnName);
    }

}