package com.honyum.elevatorMan.data;

/**
 * Created by changhaozhang on 15/9/15.
 */
public class RemindMessage {

    private String id;

    private String date;

    private String content;

    public RemindMessage(String id, String date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
