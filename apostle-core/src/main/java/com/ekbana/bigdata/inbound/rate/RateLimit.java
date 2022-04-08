package com.ekbana.bigdata.inbound.rate;

public class RateLimit {
    public static class Interval{
        public static String YEAR="year";
        public static String MONTH="month";
        public static String DAY="day";
        public static String HOUR="hour";
        public static String MINUTE="minute";
    }

    public static class TIME_INTERVAL{
        public static Long YEAR=1000L;
        public static Long MONTH=1000L;
        public static Long DAY=1000L;
        public static Long HOUR=1000L;
        public static Long MINUTE=1000L;
    }
}
