package com.example.manager;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hanbang.netsdk.BaseNetControl.NetDataCallback;
import com.hanbang.netsdk.NetParamDef.PTZAction;
import com.hanbang.netsdk.HBNetCtrl;
import com.hanbang.netsdk.RecordFileParam;

/**
 * 设备管理类
 * @author sun lin
 *
 */
public class DeviceManager
{
	/**
	 * 设备网络连接对象
	 */
	HBNetCtrl mHbNetCtrl = new HBNetCtrl();
	
	/**
	 * 设备在线状态,true - 在线，false - 不在线.
	 */
	boolean mbDeviceOnline;
	
	//设备登录信息
	String mUserName;
	String mPassword;
	String mAddress;
	int mPort;
	
	/**
	 * 设备名
	 */
	String mDeviceName;
	
	/**
	 * 通道数 
	 */
	int mnChannelCount;
	
	/**
	 * 重连定时器
	 */
	Timer mDisconnectTimer;


    private static DeviceManager mDeviceManager = null;

    /**
     * 使用单例模式获取对象
     * @return
     */
    public static DeviceManager getInstance() {
        if (null == mDeviceManager) {
            mDeviceManager = new DeviceManager();
        }
        return mDeviceManager;
    }
	
	/**
	 * 设备断线重连回调
	 */
	private NetDataCallback callback = new NetDataCallback()
	{
		
		@Override
		public void onNetData(DataType type, byte[] data, int nOffset,
				int nValidLength, long nTimestamp)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onDisconnected()
		{
			// TODO Auto-generated method stub
			//处理断线重连
			if (mbDeviceOnline)
			{
				//设置重连定时器
				setTimer();
			}
		}
	};
	
	public HBNetCtrl getHBNetCtrl()
	{
		return mHbNetCtrl;
	}
	
	/**
	 * 登录设备
	 * @param userName
	 * 		设备的用户名。
	 * @param password
	 * 		设备的密码。
	 * @param address
	 * 		设备ip或域名。
	 * @param port
	 * 		设备地址对应的端口。
	 * @return
	 * 		成功返回0，失败返回错误码。
	 */
	public int login(String userName, String password, String address, int port)
	{
		int nResult = mHbNetCtrl.login(userName, password, address, port, callback);
		if (0 == nResult)
		{
			mbDeviceOnline = true;
			
			//登录成功，关闭重连定时器
			cancelTimer();
			
			//记录登录信息
			mUserName = userName;
			mPassword = password;
			mAddress = address;
			mPort = port;
			
			//获取设备信息
			getDeviceInfo();
		}
		return nResult;
	}
	
	/**
	 * 注销设备
	 */
	public void logout()
	{		
		//关闭重连定时器
		cancelTimer();
		
		//注销
		doLogout();
	}
	
	//注销设备
	void doLogout()
	{
		mbDeviceOnline = false;
		
		//关闭所有预览、回放
		for (int i = 0; i < mnChannelCount; i++)
		{
			stopPreview(i);
			stopPlayback(i);			
		}
		
		//关闭对讲
		stopTalkback();
		
		//注销设备
		mHbNetCtrl.logout();
	}
	
	/**
	 * 开启预览
	 * @param nChannel
	 * 		通道号。
	 * @param nStreamType
	 * 		码流类型， 0 - 主码流，1 - 子码流。
	 * @param callback
	 * 		预览数据回调。
	 * @return
	 * 		成功返回0，失败返回错误码。
	 */
	public int startPreview(int nChannel, int nStreamType, NetDataCallback callback)
	{
		//先关闭上一次的预览
		stopPreview(nChannel);
		
		//开启预览
		int nResult = mHbNetCtrl.startPreview(nChannel, nStreamType, callback);
		return nResult;
	}
	
	/**
	 * 关闭预览
	 * @param nChannel
	 * 		通到号。
	 */
	public void stopPreview(int nChannel)
	{
		mHbNetCtrl.stopPreview(nChannel);
	}
	
	/**
	 * 云台控制
	 * @param nChannel
	 * 		通道号。
	 * @param action
	 * 		控制行为。
	 */
	public void controlPtz(int nChannel, PTZAction action)
	{
		mHbNetCtrl.controlPTZ(nChannel, action, 127, 0);
	}
	
	/**
	 * 开启回放
	 * @param nChannel
	 * 		通道号。
	 * @param startTime
	 * 		开始时间。
	 * @param callback
	 * 		回放数据回调 。
	 * @return
	 * 		成功返回0，失败返回错误码。
	 */
	public int startPlayback(int nChannel, Calendar startTime, NetDataCallback callback)
	{
		//构造结束时间，不能垮天
		Calendar stopTime = Calendar.getInstance();
		stopTime.set(startTime.get(Calendar.YEAR), 
				startTime.get(Calendar.MONTH), 
				startTime.get(Calendar.DAY_OF_MONTH), 
				23, 59, 59);
		
		//开启回放
		int nResult = mHbNetCtrl.startPlayback(nChannel, startTime, stopTime, callback);
		return nResult;
	}
	
	/**
	 * 请求回放帧数据
	 * @param nChannel
	 * 		通道号。
	 * @param nCount
	 * 		块数。
	 */
	public void getPlaybackData(int nChannel, int nCount)
	{
		mHbNetCtrl.getPlaybackData(nChannel, nCount);
	}
	
	/**
	 * 关闭回放
	 * @param nChannel
	 * 		通道号。
	 */
	public void stopPlayback(int nChannel)
	{
		mHbNetCtrl.stopPlayback(nChannel);
	}
	
	/**
	 * 查询录像文件
	 * @param nChannel
	 * 		通道号。
	 * @param startTime
	 * 		开始时间。
	 * @return
	 * 		成功返回录像文件链表，失败返回null。
	 */
	public List<RecordFileParam> findRecordFile(int nChannel, Calendar startTime)
	{
		return mHbNetCtrl.findRecordFile(nChannel, 0xFF, startTime);
	}
	
	/**
	 * 发送语音对讲数据
	 * @param data
	 * 		编码后的音频数据。
	 */
	public void sendVoiceData(byte[] data)
	{
		mHbNetCtrl.sendVoiceData(data);
	}
	
	/**
	 * 关闭语音对讲
	 */
	public void stopTalkback()
	{
		mHbNetCtrl.stopVoiceTalkback();
	}
	
	/**
	 * 获取设备序列号
	 * @return 序列号，获取失败返回空字符串。
	 */
	public String getSerialNo()
	{
		return mHbNetCtrl.getSerialNo();
	}
	
	/**
	 * 获取设备型号
	 * @return 成功返回设备型号，失败返回空字符串。
	 */
	public String getDeviceMode()
	{
		//注意：调用该接口之前，必须保证HBNetCtrl.getDeviceName()被调用过。
		//本例中，在登录成功之后，立即调用HBNetCtrl.getDeviceName()。
		return mHbNetCtrl.getDeviceMode();
	}
	
	/**
	 * 获取设备软件版本
	 * @return 成功返回软件版本，失败返回空字符串。
	 */
	public String getDeviceVersion()
	{
		//注意：调用该接口之前，必须保证HBNetCtrl.getDeviceName()被调用过。
		//本例中，在登录成功之后，立即调用HBNetCtrl.getDeviceName()。
		return mHbNetCtrl.getSoftwareVersion();
	}
	
	/**
	 * 获取设备名
	 * @return 成功返回设备名，失败返回空字符串。
	 */
	public String getDeviceName()
	{
		return mDeviceName;
	}
	
	/**
	 * 设置设备名
	 * @param name
	 * @return
	 */
	public boolean setDeviceName(String name)
	{
		//参数校验
		if (null == name || 0 == name.length() || !mbDeviceOnline)
		{
			return false;
		}
		
		//设置设备名
		boolean bSuccess = mHbNetCtrl.setDeviceName(name);
		if (bSuccess)
		{
			//设置成功，记录设备名
			mDeviceName = name;
		}
		
		return bSuccess;
	}
	
	/**
	 * 获取设备通道数
	 * @return
	 */
	public int getChannelCount()
	{
		return mnChannelCount;
	}
	
	void getDeviceInfo()
	{
		//获取设备支持的通道数
		mnChannelCount = mHbNetCtrl.getChannelCount();	
		
		//获取设备名
		mDeviceName = mHbNetCtrl.getDeviceName();
	}
	
	private void setTimer()
	{
		//取消上一次定时器
		cancelTimer();
		
		//设置定时器
		mDisconnectTimer = new Timer();
		TimerTask task = new TimerTask()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//注销设备
				if (mbDeviceOnline)
				{
					doLogout();
				}
				
				//连接设备
				login(mUserName, mPassword, mAddress, mPort);
			}
		};
		
		//设置立即执行，执行周期为20秒
		//执行一次HBNetCtrl.login()最长需要15秒，所以，重连周期应大于执行一次HBNetCtrl.login()的时间。
		mDisconnectTimer.schedule(task, 0, 20 * 1000);
	}
	
	private void cancelTimer()
	{
		if (null != mDisconnectTimer)
		{
			mDisconnectTimer.cancel();
			mDisconnectTimer = null;
		}
	}
}
