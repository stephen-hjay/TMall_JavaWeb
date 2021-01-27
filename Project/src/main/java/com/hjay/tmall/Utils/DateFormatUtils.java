package com.hjay.tmall.Utils;

import java.sql.Timestamp;
import java.util.Date;


/*
因为在实体类中日期类型的属性，使用的都是java.util.Date类。
而为了在MySQL中的日期格式里保存时间信息，必须使用datetime类型的字段，而jdbc要获取datetime类型字段的信息，
需要采用java.sql.Timestamp来获取，否则只会保留日期信息，而丢失时间信息。

 */

public class DateFormatUtils {
    public static Timestamp date2timestamp(Date d) {
        if (null == d)
            return null;
        return new Timestamp(d.getTime());
    }

    public static Date timestamp2date(Timestamp t) {
        if (null == t)
            return null;
        return new Date(t.getTime());
    }
}
