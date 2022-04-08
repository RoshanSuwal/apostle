package com.ekbana.bigdata.entity.emails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailEntry {
    private String key;
    private ArrayList<Integer> type;

    public static final int CALLS_LEFT = 1;
    public static final int GRACE_CALLS_STARTED = 2;
    public static final int DAYS_LEFT_TO_EXPIRE = 3;
    public static final int GRACE_CALLS_LEFT = 4;
    public static final int KEY_EXPIRED = 5;
    public static final int SERVER_NOT_FOUND=500;

    public static final int _30_DAYS_LEFT_TO_EXPIRE=31;
    public static final int _15_DAYS_LEFT_TO_EXPIRE=32;
    public static final int _7_DAYS_LEFT_TO_EXPIRE=33;
}
