package com.huayun.lib_tools.util.log;

import com.elvishew.xlog.XLog;

public class LogUtil {
    public static void xLoge(String log) {
        XLog.e(log);
    }
    public static void xLogJson(String json) {
        XLog.json(json);
    }
}
