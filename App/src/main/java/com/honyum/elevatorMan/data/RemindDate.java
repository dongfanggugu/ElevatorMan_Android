package com.honyum.elevatorMan.data;

import java.util.Date;

/**
 * Created by changhaozhang on 15/9/14.
 */
public class RemindDate implements Comparable<RemindDate> {

    public static final int TYPE_SEMI_MONTH_0 = 0;
    public static final int TYPE_MONTH_1 = 1;
    public static final int TYPE_SEASON_2 = 2;
    public static final int TYPE_SEMI_YEAR_3 = 3;
    public static final int TYPE_YEAR_4 = 4;

    private Date date;

    private int type;

    public RemindDate(Date date, int type) {
        this.date = date;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    @Override
    public int compareTo(RemindDate o) {
        if (this.date.after(o.getDate())) {
            return 1;
        } else {
            return -1;
        }
    }
}
