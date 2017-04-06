package com.honyum.elevatorMan.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2015/11/10.
 */
public class MainHelpData extends Atom {

    private List<String> sm;

    private List<String> m;

    private List<String> s;

    private List<String> sy;

    private List<String> y;

    public void setSm(List<String> sm) {
        this.sm = sm;
    }

    public void setM(List<String> m) {
        this.m = m;
    }

    public void setS(List<String> s) {
        this.s = s;
    }

    public void setSy(List<String> sy) {
        this.sy = sy;
    }

    public void setY(List<String> y) {
        this.y = y;
    }



    public List<String> getSm() {
        return sm;
    }

    public List<String> getM() {
        return m;
    }

    public List<String> getS() {
        return s;
    }

    public List<String> getSy() {
        return sy;
    }

    public List<String> getY() {
        return y;
    }

    /**
     * 根据json字符串返回对象
     * @param json
     * @return
     */
    public static MainHelpData getMainHelpData(String json) {
        return (MainHelpData) parseFromJson(MainHelpData.class, json);
    }
}
