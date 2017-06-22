package com.honyum.elevatorMan.data.mydata;

import java.io.Serializable;

/**
 * Created by LiYouGui on 2017/6/6.
 */

public class KnowledgeInfo implements Serializable {

    private String title;

    private String keywords;

    private String content;

    private String kntype;

    public String getKntype() {
        return kntype;
    }

    public void setKntype(String kntype) {
        this.kntype = kntype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
