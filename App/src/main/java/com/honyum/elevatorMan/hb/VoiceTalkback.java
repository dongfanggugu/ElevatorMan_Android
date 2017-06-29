package com.honyum.elevatorMan.hb;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.hanbang.netsdk.BaseNetControl.NetDataCallback;
import com.hanbang.netsdk.HBNetCtrl;
import com.hanbang.netsdk.NetParamDef.DeviceType;
import com.hanbang.playsdk.PlaySDK;


/**
 * 语音对讲
 */
public class VoiceTalkback
{
	final static int SAPMLERATE_IN_HZ 	= 8000;			//采样率
	final static int PACKET_SIZE	   	= 640;			//一包PAM数据的大小
    final static int AUDIO_TYPE_PCM = 2;            //PCM格式
	
//	final WeakReference<Device> mParentDevice;			//该对讲所属设备
    DeviceInfo mDeviceInfo;
	final PlaySDK mPlaySdk = new PlaySDK();				//解码库对象
	Object mCollectionLock = new Object();				//音频采集同步锁
	boolean mbTalking;									//是否在对讲
	int mAudioType;										          //接收的音频类型
    int mSendAudioType;                                         //发送的音频类型
    
    HBNetCtrl mHBNetCtrl;
    
    public final static byte AUDIO_ENCODE_TYPE_PCM = 2;

	public VoiceTalkback( DeviceInfo device )
	{
        mDeviceInfo = device;
        mHBNetCtrl = mDeviceInfo.hbNetCtrl;
	}
	
	//开启语音对讲
	public void startTalkback( final FinishCallback callback, final Object tag )
	{
	    try
        {
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    performStartTalkback( callback, tag );
                }
            } ).start();
        }
        catch ( Exception e )
        {
            // 线程池可能抛出RejectedExecutionException、NullPointerException
            e.printStackTrace();
        }
	}

	void performStartTalkback( FinishCallback callback, Object tag )
	{
/*	    if ( Device.CONNECTION_STATE_CONNECTED != device.getConnectionState() )
        {
            // 设备离线
            if ( null != callback )
            {
                callback.onFinish( ManagerError.ERR_DEVICE_DISCONNECTED, tag );
            }
            return;
        }*/
	    
        if ( mbTalking )
        {
            // 当前设备已经在对讲
            if ( null != callback )
            {
                callback.onFinish( ManagerError.ERR_SUCCESS, tag );
            }
            return;
        }

	    int error = ManagerError.ERR_UNKNOWN;

        // 设置对讲编码格式
        mAudioType = PlaySDK.PLAY_AUDIO_FORMAT_G722_HANBANG;
        mSendAudioType = PlaySDK.PLAY_AUDIO_FORMAT_G722_HANBANG;

        //增加对鸿雁IPC的支持，设置PCM格式
        if ( mHBNetCtrl.getDeviceType() == DeviceType.DEV_TYPE_IPC )
        {
            if ( mHBNetCtrl.setVoiceEncodeType( AUDIO_ENCODE_TYPE_PCM ) )
            {
                mAudioType = PlaySDK.PLAY_AUDIO_FORMAT_G711_ALAW;
                mSendAudioType = AUDIO_TYPE_PCM;
            }
        }

        if ( mSendAudioType != AUDIO_TYPE_PCM )
        {
            //尝试设置G711编码
            if ( mHBNetCtrl.setVoiceEncodeType( HBNetCtrl.AUDIO_ENCODE_TYPE_G711 ) )
            {
                mAudioType = PlaySDK.PLAY_AUDIO_FORMAT_G711_ALAW;
            }
        }

        // 开启对讲
        if ( mHBNetCtrl.startVioceTalkback( new VoiceCallback() ) )
        {
            // 打开解码库
            byte[] fileHead = new byte[64];
            String strHeadFlag = "HBGKSTREAMV30";
            DeviceType devType = mHBNetCtrl.getDeviceType();
            if ( DeviceType.DEV_TYPE_DVR == devType )
            {
                strHeadFlag = "HBGK7000T";
            }

            System.arraycopy( strHeadFlag.getBytes(), 0, fileHead, 0, strHeadFlag.length() );
            mPlaySdk.openStream( fileHead );

            // 设置音频优先
            int nFlag = mPlaySdk.getEnableFlag();
            nFlag = nFlag | PlaySDK.PLAY_ENABLE_SOUND_PRIORITY;
            mPlaySdk.setEnableFlag( nFlag );

            // 播放
            mPlaySdk.play();

            // 开启编码器
            if ( mPlaySdk.openAudioEncoder( mAudioType, 1, 16, 8000 ) )
            {
                // 开启音频采集线程
//                Account.getThreadPool().execute( new CollectVoice() );
                
                new Thread( new CollectVoice() ).start();

                //等待音频采集器开启成功
                try
                {
                    synchronized ( mCollectionLock )
                    {
                        mCollectionLock.wait();
                    }
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }

                if ( mbTalking )
                {
                    //对讲成功
                    error = ManagerError.ERR_SUCCESS;
                }
                else
                {
                    //因为开启音频采集器失败，导致对讲失败
                    error = ManagerError.ERR_AUDIO_RECORD_FAILED;
                    mHBNetCtrl.stopVoiceTalkback();
                    mPlaySdk.closeStream();
                }
            }
            else
            {
                // 语音对讲开启失败
                mHBNetCtrl.stopVoiceTalkback();
                mPlaySdk.closeStream();
                error = ManagerError.ERR_NOT_SUPPORTED;
            }
        }
        else
        {
            error = ManagerError.ERR_NOT_SUPPORTED;
        }

        if ( null != callback )
        {
            callback.onFinish( error, tag );
        }
	}
	
	//网络库数据回调
    class VoiceCallback implements NetDataCallback
    {
        @Override
        public void onNetData(DataType type, byte[] data, int nOffset,
                              int nValidLength, long nTimestamp)
        {
            // 解码库的开启由编码器负责，这里仅在开启后向解码库送数据
            // 否则多线程开启解码库，可能会导致解码库崩溃
            if (mPlaySdk.isOpened())
            {
                //向解码库送入音频数据
                mPlaySdk.inputData(data, nOffset, nValidLength, PlaySDK.PLAY_FRAME_AUDIO, 0);
            }
        }

        @Override
        public void onDisconnected()
        {
        }
    };
	
	//关闭语音对讲
	public void stopTalkback()
	{
	    try
        {
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    performStopTalkback();
                }
            } ).start();
/*            Account.getThreadPool().execute( new Runnable()
            {
                @Override
                public void run()
                {
                    performStopTalkback();
                }
            }  );*/
        }
        catch ( Exception e )
        {
            // 线程池可能抛出RejectedExecutionException、NullPointerException
            e.printStackTrace();
        }
	}
	    
	void performStopTalkback()
	{
		if ( !mbTalking )
		{
			return;
		}
		
		//停止音频采集
		mbTalking = false;
		try
		{
			synchronized (mCollectionLock)
			{
				mCollectionLock.wait();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		//关闭网络库
//		Device device = mParentDevice.get();
//		device.mHbNet.stopVoiceTalkback();
        
        mHBNetCtrl.stopVoiceTalkback();
		
		//关闭解码库
		mPlaySdk.closeAudioEncoder();
		mPlaySdk.closeStream();
	}
	
	boolean isTalking()
	{
		return mbTalking;
	}
	
	//音频采集
	private class CollectVoice implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				//创建一个录音器
				int nMiniSize = AudioRecord.getMinBufferSize(SAPMLERATE_IN_HZ, 
					AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
				
				AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 
					SAPMLERATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO, 
					AudioFormat.ENCODING_PCM_16BIT, nMiniSize);
				
				//开始采集
				record.startRecording();
				
				//通知音频采集器开启成功
				mbTalking = true;
				synchronized (mCollectionLock)
				{
					mCollectionLock.notify();
				}
				
				//循环读取数据
				int nReadLength = 0;
				byte[] encodeAudioBuf;
				byte[] audioBuffer = new byte[PACKET_SIZE];
//				Device device = mParentDevice.get();
				while (mbTalking)
				{
					nReadLength = record.read(audioBuffer, 0, PACKET_SIZE);
                    if ( mSendAudioType == AUDIO_TYPE_PCM && nReadLength > 0 )
                    {
                        //发送PCM数据
                        byte[] voiceData = new byte[nReadLength];
                        System.arraycopy( audioBuffer, 0, voiceData, 0, nReadLength );
                        mHBNetCtrl.sendVoiceData( voiceData );
                    }
					else if (nReadLength == PACKET_SIZE)
					{
						//送入编码
						mPlaySdk.inputAudioRawData(audioBuffer);
						
						//获取编码后的音频送入网络库
						do
						{
							encodeAudioBuf = mPlaySdk.getAudioEncodedData();
							if (null != encodeAudioBuf)
							{
                                mHBNetCtrl.sendVoiceData(encodeAudioBuf);
							}
						}while (null != encodeAudioBuf);
					}
				}
				
				//停止录音
				record.stop();
				record.release();
				record = null;
				
				//通知音频采集器关闭成功
				synchronized (mCollectionLock)
				{
					mCollectionLock.notify();
				}
			}
			catch (IllegalArgumentException e) 
			{
				//通知音频采集器开启失败
				mbTalking = false;
				synchronized (mCollectionLock)
				{
					mCollectionLock.notify();
				}
			}
			catch (IllegalStateException e) 
			{
				//通知音频采集器开启失败
				mbTalking = false;
				synchronized (mCollectionLock)
				{
					mCollectionLock.notify();
				}
			}
		}
	}
}
