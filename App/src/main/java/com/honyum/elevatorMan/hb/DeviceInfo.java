package com.honyum.elevatorMan.hb;

import com.hanbang.netsdk.BaseNetControl;
import com.hanbang.netsdk.HBNetCtrl;

/**
 * Created by Administrator on 2016/4/12.
 */
public class DeviceInfo {
    /**
     * 设备的用户名
     */
    public String deviceUser = "";

    /**
     * 设备的密码
     */
    public String devicePsw = "";

    /**
     * 设备序列号
     */
    public String deviceSn = "";

    /**
     * 设备Id
     */
    public String deviceId = "";

    /**
     * 设备名
     */
    public String deviceName = "";

    /**
     * 设备局域网IP
     */
    public String deviceLanIp = "";

    /**
     * 设备的局域网端口
     */
    public int deviceLanPort;

    /**
     * 设备的威威Id
     */
    public String vveyeId = "";

    /**
     * 设备的威威远程端口
     */
    public int vveyeRemortPort = 0;

    /**
     * 设备域名
     */
    public    String deviceDomain = "";

    /**
     * 设备映射端口
     */
    public int domainPort = 0;

    /**
     * 设备的通道数
     */
    public    int channelCount;

    /**
     * 设备的网络库对象
     */
    public    HBNetCtrl hbNetCtrl;

    /**
     * 设备在线状态
     */
    public boolean isOnline = false;
    
    /**
     * 流媒体服务器IP
     */
    public String smsIp = "";
    
    /**
     * 流媒体服务器的端口
     */
    public int smsPort = 0;

    /**
     * 断线回调
     */
    public  BaseNetControl.NetDataCallback callback = new BaseNetControl.NetDataCallback() {
        @Override
        public void onNetData(DataType dataType, byte[] bytes, int i, int i1, long l) {

        }

        @Override
        public void onDisconnected() {
            //设备离线
            if (isOnline)
            {
                isOnline = false;

                //起线程注销设备
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        hbNetCtrl.logout();
                    }
                });
            }
        }
    };

    public String toString(){
        return deviceSn;
    }

}
