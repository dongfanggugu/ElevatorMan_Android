package com.honyum.elevatorMan.utils;

import com.honyum.elevatorMan.data.MaintRecInfo;

import java.util.List;

/**
 * Created by Star on 2017/6/19.
 */

public class DataTravelUtils {
    private static List<MaintRecInfo> mydata;
    public static void setMiantRecInfoData(List<MaintRecInfo>  adtas)
    {
        mydata =adtas;
    }
    public static List<MaintRecInfo> getMiantRecInfoData()
    {
       return mydata;
    }


}
