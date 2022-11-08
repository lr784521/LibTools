package com.huayun.lib_tools.util.log;

import com.elvishew.xlog.LogConfiguration;


import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

/**
 * 日志工具
 */
public class LogUtil {

    public static void init(boolean openLog,String logTag){
        LogConfiguration configuration = new LogConfiguration.Builder()
                .logLevel(openLog ? LogLevel.ERROR : LogLevel.NONE) // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                .tag(logTag) // 指定 TAG，默认为 "X-LOG"
                .build();
        XLog.init(configuration);
    }

    /**
     * 打印普通日志
     * @param log
     */
    public static void xLoge(String log) {
        XLog.e(log);
    }

    /**
     * 专用于打印 JSON
     * @param json
     */
    public static void xLogJson(String json) {
        XLog.json(json);
    }
}
