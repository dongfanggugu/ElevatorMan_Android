package com.honyum.elevatorMan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2017/12/6.
 */

public class IconInfosTag implements Serializable {
    private static final long serialVersionUID = -5797401123377007420L;
    private String tag;
    private ArrayList<IconInfo> iconsInfo;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<IconInfo> getIconsInfo() {
        return iconsInfo;
    }

    public void setIconsInfo(ArrayList<IconInfo> iconsInfo) {
        this.iconsInfo = iconsInfo;
    }
}
