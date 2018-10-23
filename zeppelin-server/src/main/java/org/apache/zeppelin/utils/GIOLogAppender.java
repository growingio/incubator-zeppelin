package org.apache.zeppelin.utils;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * author CliffLeopard
 * time   2018/10/23:14:58
 * email  gaoguanling@growingio.com
 */
public class GIOLogAppender extends DailyRollingFileAppender {
    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {
        return this.getThreshold().equals(priority);
    }
}