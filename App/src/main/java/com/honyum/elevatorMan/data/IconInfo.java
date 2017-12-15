package com.honyum.elevatorMan.data;

import java.io.Serializable;

/**
 * Created by Star on 2017/11/29.
 */

public class IconInfo implements Serializable {


    private static final long serialVersionUID = 2659828778681591440L;
    private String id = "";
    private String name = "";
    private int image = 0;
    private String action = "";
    private boolean editAble = false;
    private boolean selected = false;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEditAble() {
        return editAble;
    }

    public void setEditAble(boolean editAble) {
        this.editAble = editAble;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IconInfo)) return false;
        IconInfo iconInfo = (IconInfo) o;
        if (id.equals(iconInfo.id)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
