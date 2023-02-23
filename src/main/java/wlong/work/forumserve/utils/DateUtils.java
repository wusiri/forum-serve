package wlong.work.forumserve.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 获取当天日期
     *
     * @return 当天日期
     */
    public static String getTodayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}
