package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by 李有鬼 on 2017/1/17 0017
 */
public class BannerInfo implements Serializable {

    private String id;

    private String pic;

    private String picUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
