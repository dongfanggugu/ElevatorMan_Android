package com.example.netsdktest;

import com.example.manager.DeviceManager;
import com.hanbang.netsdk.BaseNetControl.NetDataCallback;
import com.hanbang.netsdk.HBNetCtrl;
import com.hanbang.netsdk.NetParamDef.PTZAction;
import com.hanbang.playsdk.PlaySDK;
import com.hanbang.playsdk.PlaySurfaceView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentVideo extends Fragment
{
	DeviceManager device;
	PlaySDK mPlaySdk = new PlaySDK();		//预览使用的解码库
	PlaySDK mPlayAudioSdk = new PlaySDK();	//语音对讲使用的解码库
	
	ProgressDialog mProgressDlg;
	
	PlaySurfaceView surfaceView;
	Button btnPreview;	
	boolean bPreview = false;
	
	//对讲相关
	Button btnTalkback;
	boolean bTalkback = false;
	Thread mThreadCollect;
	final int PACKET_SIZE = 640;
	
	//视频数据回调
	NetDataCallback callback = new NetDataCallback()
	{
		
		@Override
		public void onNetData(DataType type, byte[] data, int nOffset, int nValidLength,
				long nTimeStamp)
		{
			// TODO Auto-generated method stub
			if (!mPlaySdk.isOpened())
			{
				if (mPlaySdk.openStream(data, nOffset, nValidLength))
				{
					//设置播放状态
					mPlaySdk.play();
				}
			}
			
			mPlaySdk.inputData(data, nOffset, nValidLength);
		}
		
		@Override
		public void onDisconnected()
		{
			// TODO Auto-generated method stub
			
		}
	};
	
	//对讲数据回调
	NetDataCallback voiceDataCallback = new NetDataCallback()
	{
		
		@Override
		public void onNetData(DataType type, byte[] data, int nOffset, int nValidLength,
				long nTimeStamp)
		{
			// TODO Auto-generated method stub
			if (mPlayAudioSdk.isOpened())
			{
				mPlayAudioSdk.inputData(data, nOffset, nValidLength);
			}
		}
		
		@Override
		public void onDisconnected()
		{
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view =  inflater.inflate(R.layout.fragment_video_hb, container, false);
		
		//device = ((NetSdkTestApplication)getActivity().getApplication()).device;
        device = DeviceManager.getInstance();
		
		surfaceView = (PlaySurfaceView)view.findViewById(R.id.surface_video);
		btnPreview = (Button)view.findViewById(R.id.btn_preview);
		
		//预览
		btnPreview.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				preview();
			}
		});
		
		//语音对讲
		btnTalkback = (Button)view.findViewById(R.id.btn_talkback);
		btnTalkback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				talkback();
			}
		});
		
		//回放
		((Button)view.findViewById(R.id.btn_playback)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), PlaybackActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		//云台控制 - 上
		((Button)view.findViewById(R.id.btn_up)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ptzControl(PTZAction.TURN_UP);
			}
		});
		
		//云台控制 - 下
		((Button)view.findViewById(R.id.btn_down)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ptzControl(PTZAction.TURN_DOWN);
			}
		});
		
		//云台控制 - 左
		((Button)view.findViewById(R.id.btn_left)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ptzControl(PTZAction.TURN_LEFT);
			}
		});
		
		//云台控制 - 右
		((Button)view.findViewById(R.id.btn_right)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ptzControl(PTZAction.TURN_RIGHT);
			}
		});
		
		//云台控制 - 停止
		((Button)view.findViewById(R.id.btn_stop)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ptzControl(PTZAction.ALL_STOP);
			}
		});
		
		return view;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden)
	{
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden)
		{
			//关闭预览
			stopPreview();
			
			//关闭对讲
			stopTalkback();
		}
	}

	//预览
	private void preview()
	{
		if (bPreview)
		{
			stopPreview();
		}
		else
		{
			//开启预览
			mProgressDlg = ProgressDialog.show(getActivity(), null, "正在开启...");					
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					//设置视口
					surfaceView.setPlayer(mPlaySdk);
					
					final int result = device.startPreview(0, 0, callback);
					
					mProgressDlg.dismiss();					
					btnPreview.post(new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							if (0 == result)
							{
								bPreview = true;
								btnPreview.setText("停止");
							}
							else
							{
								Toast.makeText(getActivity(), "开启预览失败", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}).start();
		}
	}
	
	void stopPreview()
	{
		if (!bPreview)
		{
			return;
		}
		
		bPreview = false;			
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//清空视口
				surfaceView.setPlayer(null);
				
				//关闭预览
				device.stopPreview(0);
				
				//停止解码
				mPlaySdk.closeStream();
			}
		}).start();
		
		//更新界面
		btnPreview.setText("预览");
	}
	
	void talkback()
	{
		if (bTalkback)
		{
			stopTalkback();
		}
		else
		{
			startTalkback();
		}
	}
		
	void startTalkback()
	{
		//锁住界面
		final ProgressDialog dlg = ProgressDialog.show(getActivity(), null, "正在处理...");
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//默认是自定义的G722编码，可以先设置音频编码格式为G711
				int nEncodeType = PlaySDK.PLAY_AUDIO_FORMAT_G722_HANBANG;
				HBNetCtrl hbNetCtrl = device.getHBNetCtrl();
				if (hbNetCtrl.setVoiceEncodeType(HBNetCtrl.AUDIO_ENCODE_TYPE_G711))
				{
					nEncodeType = PlaySDK.PLAY_AUDIO_FORMAT_G711_ALAW;
				}
				
				//开启对讲
				if (hbNetCtrl.startVioceTalkback(voiceDataCallback))
				{
					//打开解码库
					byte[] fileHead = new byte[64];
		            String strHeadFlag = "HBGKSTREAMV30";
		            System.arraycopy( strHeadFlag.getBytes(), 0, fileHead, 0, strHeadFlag.length() );
		            mPlayAudioSdk.openStream(fileHead);
		            
		            //设置音频优先
		            int nFlag = mPlaySdk.getEnableFlag();
		            nFlag = nFlag | PlaySDK.PLAY_ENABLE_SOUND_PRIORITY;
		            mPlayAudioSdk.setEnableFlag( nFlag );
		            
		            //播放
		            mPlayAudioSdk.play();
		            
		            //开启编码器
		            if (mPlayAudioSdk.openAudioEncoder(nEncodeType, 1, 16, 8000))
		            {
		            	bTalkback = true;
		            	
		            	//开启采集线程
		            	mThreadCollect = new Thread(new CollectVoice());
		            	mThreadCollect.start();
		            	
		            }
		            else
		            {
		            	//对讲失败
		            	hbNetCtrl.stopVoiceTalkback();
		            	mPlayAudioSdk.closeStream();
		            }
				}
				
				dlg.dismiss();				
				btnTalkback.post(new Runnable()
				{
					public void run()
					{
						String text = bTalkback ? "停止对讲" : "语音对讲";
						btnTalkback.setText(text);
					}
				});
			}
		}).start();

	}
	
	void stopTalkback()
	{
		bTalkback = false;
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				//停止采集线程
				if (null != mThreadCollect && mThreadCollect.isAlive())
				{
					try
					{
						mThreadCollect.join();
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mThreadCollect = null;
				}
				
				//关闭网络库
				device.stopTalkback();
				
				//关闭解码库
				mPlayAudioSdk.closeAudioEncoder();
				mPlayAudioSdk.closeStream();
			}
		}).start();
		
		btnTalkback.setText("语音对讲");
	}
	
	void ptzControl(final PTZAction action)
	{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				device.controlPtz(0, action);
			}
		}).start();
	}
	
	private class CollectVoice implements Runnable
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			//创建一个录音器
			int nSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, 
					AudioFormat.ENCODING_PCM_16BIT);
			
			AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 
					8000, AudioFormat.CHANNEL_IN_MONO, 
					AudioFormat.ENCODING_PCM_16BIT, nSize);
			
			//开始采集
			record.startRecording();
			
			//读取数据
			int nReadLen = 0;
			byte[] encodeAudio;
			byte[] audioBuffer = new byte[PACKET_SIZE];
			while (bTalkback)
			{
				nReadLen = record.read(audioBuffer, 0, PACKET_SIZE);
				if (nReadLen == PACKET_SIZE)
				{
					//送入编码
					mPlayAudioSdk.inputAudioRawData(audioBuffer);
					
					//获取编码后的音频，发送给对方
					do
					{
						encodeAudio = mPlayAudioSdk.getAudioEncodedData();
						if (null != encodeAudio)
						{
							device.sendVoiceData(encodeAudio);
						}
					}
					while(null != encodeAudio);
				}
			}
			
			//停止采集
			record.stop();
			record.release();
			record = null;
		}		
	}
}
