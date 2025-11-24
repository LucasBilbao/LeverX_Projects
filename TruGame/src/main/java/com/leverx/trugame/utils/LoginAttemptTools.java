package com.leverx.trugame.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAttemptTools {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME = 5 * 60 * 1000;

    private static final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private static final Map<String, Long> lockTimeCache = new ConcurrentHashMap<>();

    public static void resetLoginTimeout(String username) {
        attemptsCache.remove(username);
        lockTimeCache.remove(username);
    }

    public static void loginFailed(String email) {
        int attempts = attemptsCache.getOrDefault(email, 0) + 1;
        attemptsCache.put(email, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(email, System.currentTimeMillis() + LOCK_TIME);
        }
    }

    public static boolean isBlocked(String username) {
        Long lockTime = lockTimeCache.get(username);

        if (lockTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > lockTime) {
            resetLoginTimeout(username);
            return false;
        }

        return true;
    }

    public static Long getMillsToUnlock(String username) {
        return lockTimeCache.get(username);
    }

}
