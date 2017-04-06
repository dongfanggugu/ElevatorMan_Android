package com.honyum.elevatorMan.data;

import com.baidu.navisdk.util.common.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 维修工信息类
 *
 * @author chang
 */
public class WorkerInfo implements Serializable {

    private String name = "";

    private String tel = "";

    private String lat = "";

    private String lng = "";

    private String state = "0";    //维修工状态

    private String userId = "";

    private String headPic;

    private List<Point> points;

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    /**
     * 判断当前维修工是否为有效人员
     *
     * @return
     */
    public boolean isValidWorker() {
        return !StringUtils.isEmpty(name);
    }
}
