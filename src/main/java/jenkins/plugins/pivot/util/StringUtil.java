/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.pivot.util;

import hudson.Util;
import java.util.UUID;

/**
 *
 * @author zijiang
 */
public class StringUtil {

    private static long HOUR_TO_MILLISEC = 3600000;
    private static long MIN_TO_MILLISEC = 60000;
    private static long SEC_TO_MILLISEC = 1000;

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNullOrEmpty(String... args) {
        for (String s : args) {
            if (StringUtil.isNullOrEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    public static String formatDuration(long ms) {
        long hour = ms / HOUR_TO_MILLISEC;
        long min = (ms - hour * HOUR_TO_MILLISEC) / MIN_TO_MILLISEC;
        long sec = (ms - hour * HOUR_TO_MILLISEC - min * MIN_TO_MILLISEC) / SEC_TO_MILLISEC;
        return String.format("%sh %sm %ss", hour, min, sec);
    }

    public static String formatShortDuration(long ms) {
        long hour = ms / HOUR_TO_MILLISEC;
        long min = (ms - hour * HOUR_TO_MILLISEC) / MIN_TO_MILLISEC;
        return String.format("%sh %sm", hour, min);
    }

    public static boolean isPositiveInteger(String value) {
        if (Util.fixEmpty(value) == null) {
            return false;
        }
        try {
            int val = Integer.parseInt(value);
            if (val <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String removeProtocalHead(String url) {
        if (url == null) {
            return null;
        }
        return url.replaceFirst("^.*?://", "");
    }

    public static String generateAgentKey(String token, int round) {
        return token.concat(String.format("_%s", round));
    }

    public static String replaceSpace(String s) {
        return s.trim().replaceAll(" ", "-");
    }

    public static UUID tryParseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static boolean checkFileName(String fileName) {
        return fileName.matches("[^/\\\\<>*?|\"]+");
    }
}
