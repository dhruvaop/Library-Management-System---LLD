package com.library.util;

import java.util.UUID;

public class IdGenerator {

    public static String generatePatronId() {
        return "P-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateBranchId() {
        return "B-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateLendingRecordId() {
        return "LR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateReservationId() {
        return "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
