package com.ekbana.bigdata.inbound.rate;

public class RateLimit {
    public static class Interval {
        public static String YEAR = "year";
        public static String MONTH = "month";
        public static String DAY = "day";
        public static String HOUR = "hour";
        public static String MINUTE = "minute";
    }

    public static class TIME_INTERVAL {
        public static Long MINUTE = 60L;
        public static Long HOUR = 60 * MINUTE;
        public static Long DAY = 24 * HOUR;
        public static Long MONTH = 30 * DAY;
        public static Long YEAR = 365 * DAY;
    }
}
