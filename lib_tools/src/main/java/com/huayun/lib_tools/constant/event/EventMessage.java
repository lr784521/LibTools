package com.huayun.lib_tools.constant.event;

/**
 * EventBus通信类
 */
public class EventMessage {

    /**
     * 消息码
     */
    private int eventCode = -1;
    /**
     * 消息参数
     */
    private Object data;

    public EventMessage(int eventCode) {
        this(eventCode, null);
    }

    public EventMessage(int eventCode, Object data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public int getEventCode() {
        return eventCode;
    }

    public Object getData() {
        return data;
    }

}
