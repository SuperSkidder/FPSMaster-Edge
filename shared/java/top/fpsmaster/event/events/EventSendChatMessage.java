package top.fpsmaster.event.events;

import top.fpsmaster.event.CancelableEvent;
import top.fpsmaster.event.Event;

public class EventSendChatMessage extends CancelableEvent {
    public String msg;

    public EventSendChatMessage(String msg) {
        this.msg = msg;
    }
}
