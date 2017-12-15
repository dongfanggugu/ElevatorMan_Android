package com.honyum.elevatorMan.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Star on 2017/12/5.
 */

public class IconInfos implements Serializable {
    private static final long serialVersionUID = -5523533539389055972L;
    private List<IconInfosTag> infos;

    public List<IconInfosTag> getInfos() {
        return infos;
    }

    public void setInfos(List<IconInfosTag> infos) {
        this.infos = infos;
    }
}
