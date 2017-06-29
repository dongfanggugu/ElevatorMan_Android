package com.honyum.elevatorMan.hb;

import com.hanbang.ydtsdk.YdtDeviceParam;
import com.hanbang.ydtsdk.YdtNetSDK;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class AccountInfo {
    //单实例帐户对象
    private static AccountInfo accountInfo;

    final static String DEVELOPER_ID = "4b7ebe16c99af1daf3b37d7e01536c9f";
    final static String APP_ID = "8tqukmaM7g4alfdBDTsV_w";
    final static String APP_KEY = "737lRF8GWFyrC3DhK2TnXkLvPi8fsCXBxOCoSsP99tGPrmBD";

    /**
     * 账户下设备信息
     */
    List<DeviceInfo> mDeviceList = new ArrayList<DeviceInfo>(  );

    //一点通库对象，保证所有使用一点通接口都是通过同一个实例调用。
    YdtNetSDK ydtNetSDK = new YdtNetSDK();

    private AccountInfo()
    {
        //初始化一点通库
        //DEVELOPER_ID - 开发者ID，在开发者平台申请获取。
        //APP_ID - 应用ID，在开发者平台申请获取。
        //APP_KEY - 应用ID对应的Key，在开发者平台获取。
        //imei - 安卓设备的唯一标识码。
        ydtNetSDK.init(DEVELOPER_ID, APP_ID, APP_KEY, "");
    }

    public static AccountInfo getInstance()
    {
        if (null == accountInfo)
        {
            accountInfo = new AccountInfo();
        }
        return accountInfo;
    }

    public YdtNetSDK getYdtNetSDK()
    {
        return ydtNetSDK;
    }

    public void setYdtDeviceInfos( List<YdtDeviceParam> ydtDeviceInfos )
    {
        DeviceInfo deviceInfo = null;
        for ( int i = 0;i < ydtDeviceInfos.size(); i++ )
        {
            deviceInfo = null;
            YdtDeviceParam ydtDeviceInfo = ydtDeviceInfos.get( i );
            for (DeviceInfo devInfo : mDeviceList)
            {
                if (devInfo.deviceSn.equals(ydtDeviceInfo.devSN))
                {
                    //设备已经存在
                    deviceInfo = devInfo;
                    break;
                }
            }

            if (null == deviceInfo)
            {
                deviceInfo = new DeviceInfo();
                mDeviceList.add( deviceInfo );
            }

            deviceInfo.deviceId = ydtDeviceInfo.devId;
            deviceInfo.channelCount = ydtDeviceInfo.devChannelCount;
            deviceInfo.deviceDomain = ydtDeviceInfo.devDomain;
            deviceInfo.deviceName = ydtDeviceInfo.devName;
            deviceInfo.devicePsw = ydtDeviceInfo.devPassword;
            deviceInfo.deviceSn = ydtDeviceInfo.devSN;
            deviceInfo.deviceUser = ydtDeviceInfo.devUser;
            deviceInfo.domainPort = ydtDeviceInfo.devDomainPort;
            if ( "VV".equals( ydtDeviceInfo.devNetType ) )
            {
                deviceInfo.vveyeId = ydtDeviceInfo.devVNIp;
                deviceInfo.vveyeRemortPort = ydtDeviceInfo.devVNPort;
            }
        }

    }

    public List<DeviceInfo> getYdtDeviceInfos()
    {
        return mDeviceList;
    }

    public void addDeviceToList(DeviceInfo deviceInfo)
    {
        for (DeviceInfo devInfo : mDeviceList)
        {
            if (devInfo.deviceSn.equals(deviceInfo.deviceSn))
            {
                //设备已经存在
                return;
            }
        }

        mDeviceList.add(deviceInfo);
    }

    public void removeDeviceFromList(String deviceSn)
    {
        for (DeviceInfo devInfo : mDeviceList)
        {
            if (devInfo.deviceSn.equals(deviceSn))
            {
                mDeviceList.remove(devInfo);
                return;
            }
        }
    }

}
