package com.vicky.blog.repository.firebase;

import java.util.UUID;

class FirebaseUtil {
    
    static long getUniqueLong() {
        long randomPositiveLong = Math.abs(UUID.randomUUID().getMostSignificantBits());
        return randomPositiveLong;
    }
}
