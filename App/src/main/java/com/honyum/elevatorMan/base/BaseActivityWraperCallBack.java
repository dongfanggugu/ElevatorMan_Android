package com.honyum.elevatorMan.base;

/**
 * Created by Star on 2017/6/15.
 */

public interface BaseActivityWraperCallBack {
    void updateView();
    public class CallBackInfo<T>{
        int state;
        T object;
    }
}
