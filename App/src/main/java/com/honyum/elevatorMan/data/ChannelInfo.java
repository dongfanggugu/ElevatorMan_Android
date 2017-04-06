package com.honyum.elevatorMan.data;

/**
 * Created by changhaozhang on 2017/4/6.
 */
public class ChannelInfo extends Atom {

    private String id;

    private String text;

    private boolean toRead = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isToRead() {
        return toRead;
    }

    public void setToRead(boolean toRead) {
        this.toRead = toRead;
    }
}
