package com.chattylabs.sdk.android.core;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static Random rand = new Random();
    private static LinkedHashMap<String, Integer> lastRandMap = new LinkedHashMap<>();

    public static String getRandom(String key, String[] strings) {
        int a;
        if (!lastRandMap.containsKey(key)) { lastRandMap.put(key, 0); }
        do {
            a = rand.nextInt(strings.length);
        } while (a == lastRandMap.get(key));
        lastRandMap.put(key, a);
        return strings[a];
    }

    public static String[] split(String str, int size) {
        return (size<1 || str==null) ? null : str.split("(?<=\\G.{"+size+"})");
    }

    /**
     * Replaces with callback, with no limit to the number of replacements.
     * Probably what you want most of the time.
     */
    public static String replace(String pattern, String subject, Callback callback) {
        return replace(Pattern.compile(pattern), subject, -1, null, callback);
    }

    public static String replace(Pattern pattern, String subject, Callback callback) {
        return replace(pattern, subject, -1, null, callback);
    }

    public static String replace(String pattern, String subject, int limit, Callback callback) {
        return replace(Pattern.compile(pattern), subject, limit, null, callback);
    }

    /**
     * @param pattern  The regular expression pattern to search on.
     * @param subject  The string to be replaced.
     * @param limit    The maximum number of replacements to make. A negative value
     *                 indicates replace all.
     * @param count    If this is not null, it will be set to the number of
     *                 replacements made.
     * @param callback Callback function
     */
    public static String replace(Pattern pattern, String subject, int limit,
                                 AtomicInteger count, Callback callback) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = pattern.matcher(subject);
        int i;
        for (i = 0; (limit < 0 || i < limit) && matcher.find(); i++) {
            String replacement = callback.matchFound(matcher.toMatchResult());
            replacement = Matcher.quoteReplacement(replacement); //probably what you want...
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        if (count != null) {
            count.set(i);
        }
        return sb.toString();
    }

    public interface Callback {
        /**
         * This function is called when a match is made. The string which was matched
         * can be obtained via match.group(), and the individual groupings via
         * match.group(n).
         */
        String matchFound(MatchResult match);
    }
}
