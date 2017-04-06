package com.example.netsdktest;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.manager.DeviceManager;
import com.hanbang.netsdk.BaseNetControl.NetDataCallback;
import com.hanbang.netsdk.RecordFileParam;
import com.hanbang.playsdk.PlaySDK;
import com.hanbang.playsdk.PlaySDK.OnBufferStateListener;
import com.hanbang.playsdk.PlaySurfaceView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//回放，以第一通道为例
public class PlaybackActivity extends Activity
{
    final static int REQUEST_LARGE_DATA = 20;		//请求大量数据
	final static int REQUEST_SMALL_DATA = 10;		//请求少量数据
	final static int REQUEST_NO_DATA = 0;			//不请求数据
	
	PlaySurfaceView viewVideo;						//播放窗口
	EditText editYear;								//年
	EditText editMonth;								//月
	EditText editDay;								//日
	EditText editHour;								//时
	EditText editMinute;							//分
	EditText editSecond;							//秒
	TextView textRecord;							//录像文件打印区
	
	DeviceManager device;							//设备实例
	PlaySDK mPlaySdk = new PlaySDK();				//解码实例
	
	boolean bPlayback = false;						//是否在回放
	int mRequestData = REQUEST_NO_DATA;				//请求数据的数据量
	
	Timer mFindFileTimer;
	Timer mTimerRequestData;						//请求数据定时器
	
	Calendar recordDate = Calendar.getInstance();	//查询录像的时间

	//回放数据回调
	NetDataCallback callback = new NetDataCallback()
	{
		
		@Override
		public void onNetData(DataType type, byte[] data, int nOffset, int nValidLength,
				long nTimeStamp)
		{
			// TODO Auto-generated method stub
			if (!mPlaySdk.isOpened())
			{
				//打开解码
				if (mPlaySdk.openStream(data, nOffset, nValidLength))
				{					
					//注册缓冲区状态回调
					mPlaySdk.setOnBufferStateListener(new OnBufferStateListener()
					{
						
						@Override
						public void onBufferStateChanged(PlaySDK player, int oldState, int newState)
						{
							// TODO Auto-generated method stub
							if ( PlaySDK.PLAY_STATE_PLAYING == player.getPlayState() )
			                {
			                    switch ( newState )
			                    {
			                    case PlaySDK.PLAY_BUFFER_STATE_BUFFERING:   // 正在缓冲中
			                        // 缓冲区已经空了，需要请求大量数据
			                        mRequestData = REQUEST_LARGE_DATA;
			                        break;

			                    case PlaySDK.PLAY_BUFFER_STATE_SUITABLE:    // 已缓冲了合适的数据
			                        break;

			                    case PlaySDK.PLAY_BUFFER_STATE_MORE:        // 已缓冲了太多的数据
			                        mRequestData = REQUEST_NO_DATA;
			                        break;

			                    default:
			                        break;
			                    }
			                }
						}
						
						@Override
						public void onBufferStateAlmostChange(PlaySDK player, boolean almostEmpty)
						{
							// TODO Auto-generated method stub
							if ( PlaySDK.PLAY_STATE_PLAYING == player.getPlayState() )
			                {
			                    if ( almostEmpty )
			                    {
			                        // 缓冲区接近于空，需要请求少量数据
			                        if ( mRequestData < REQUEST_SMALL_DATA )
			                        {
			                            mRequestData = REQUEST_SMALL_DATA;
			                        }
			                    }
			                    else
			                    {
			                        // 缓冲区接近于满，不请求数据
			                        mRequestData = REQUEST_NO_DATA;
			                    }
			                }
						}
					});
					
					//设置缓冲模式:流畅性优先
					mPlaySdk.setBufferMode(PlaySDK.PLAY_BUFFER_MODE_FLUENCY);
					
					//设置播放状态
					mPlaySdk.play();	
				}
			}
			
			//送入数据
			mPlaySdk.inputData(data, nOffset, nValidLength);
		}
		
		@Override
		public void onDisconnected()
		{
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playback_hb);
		
		//device = ((NetSdkTestApplication)getApplication()).device;
        device = DeviceManager.getInstance();
		
		viewVideo = (PlaySurfaceView)findViewById(R.id.surface_video);
		editYear = (EditText)findViewById(R.id.edit_year);
		editMonth = (EditText)findViewById(R.id.edit_month);
		editDay = (EditText)findViewById(R.id.edit_day);
		editHour = (EditText)findViewById(R.id.edit_hour);
		editMinute = (EditText)findViewById(R.id.edit_minute);
		editSecond = (EditText)findViewById(R.id.edit_second);
		textRecord = (TextView)findViewById(R.id.text_record);
		textRecord.setMovementMethod(ScrollingMovementMethod.getInstance());

		//查询
		((Button)findViewById(R.id.btn_find)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//获取查询时间
				String year = editYear.getText().toString();
				String month = editMonth.getText().toString();
				String day = editDay.getText().toString();
				if (0 == year.length()
					|| 0 == month.length()
					|| 0 == day.length())
				{
					Toast.makeText(PlaybackActivity.this, "请输入年月日", Toast.LENGTH_SHORT).show();
					return;
				}
				
				int nYear = Integer.parseInt(year);
				int nMonth = Integer.parseInt(month);
				int nDay = Integer.parseInt(day);
				if (nYear < 1970 || nYear > 2099
					|| nMonth < 1 || nMonth > 12
					|| nDay  < 1 || nDay > 31)
				{
					Toast.makeText(PlaybackActivity.this, "请输入正确的年月日", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//构造查询时间
				recordDate.set(nYear, nMonth - 1, nDay, 0, 0, 0);
				
				findRecordFile();
			}
		});
		
		//播放
		((Button)findViewById(R.id.btn_play)).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (bPlayback)
				{
					stopPlayback();
				}
				else
				{
					startPlayback();
				}
			}
		});
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		//停止回放
		stopPlayback();
		
		super.onDestroy();		
	}
	
	void findRecordFile()
	{
		final ProgressDialog dlg = ProgressDialog.show(this, null, "正在查询...", true, false);
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				final List<RecordFileParam> fileList = device.findRecordFile(0, recordDate);
				dlg.dismiss();
				
				textRecord.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub						
						if (null == fileList)
						{
							Toast.makeText(PlaybackActivity.this, "未查到录像文件", Toast.LENGTH_SHORT).show();
						}
						else
						{
							int fileCount = fileList.size();
							StringBuilder recordContent = new StringBuilder();
							recordContent.append("共查询到录像文件数：").append(fileList.size()).append("\n");
							for (int i = 0; i < fileCount; i++)
							{
								RecordFileParam param = fileList.get(i);
								Calendar startTime = param.getStartTime();
								Calendar stopTime = param.getStopTime();
								
								//序号
								recordContent.append(i).append(" ");
								
								//开始时间
								recordContent.append(startTime.get(Calendar.YEAR)).append("/");
								recordContent.append(startTime.get(Calendar.MONTH) + 1).append("/");
								recordContent.append(startTime.get(Calendar.DAY_OF_MONTH)).append(" ");
								recordContent.append(startTime.get(Calendar.HOUR)).append(":");
								recordContent.append(startTime.get(Calendar.MINUTE)).append(":");
								recordContent.append(startTime.get(Calendar.SECOND));
								
								recordContent.append(" - ");
								
								//结束时间
								recordContent.append(stopTime.get(Calendar.YEAR)).append("/");
								recordContent.append(stopTime.get(Calendar.MONTH) + 1).append("/");
								recordContent.append(stopTime.get(Calendar.DAY_OF_MONTH)).append(" ");
								recordContent.append(stopTime.get(Calendar.HOUR)).append(":");
								recordContent.append(stopTime.get(Calendar.MINUTE)).append(":");
								recordContent.append(stopTime.get(Calendar.SECOND)).append("\n");
							}
							
							textRecord.setText(recordContent);
						}
					}
				});
			}
		}).start();
	}
	
	//开启回放
	void startPlayback()
	{
		//获取要播放的时间
		String hour = editHour.getText().toString();
		String minute = editMinute.getText().toString();
		String second = editSecond.getText().toString();
		if (hour.length() == 0 
			|| minute.length() == 0 
			|| second.length() == 0)
		{
			Toast.makeText(PlaybackActivity.this, "请输入时分秒", Toast.LENGTH_SHORT).show();
			return;
		}
		
		int nHour = Integer.parseInt(hour);
		int nMinite = Integer.parseInt(minute);
		int nSecond = Integer.parseInt(second);
		if (nHour < 0 || nHour > 23
			|| nMinite < 0 || nMinite > 59
			|| nSecond  < 0 || nSecond > 59)
		{
			Toast.makeText(PlaybackActivity.this, "请输入正确的时分秒", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//构造播放开始时间
		final Calendar startTime = Calendar.getInstance();
		startTime.set(recordDate.get(Calendar.YEAR), 
			recordDate.get(Calendar.MONTH), 
			recordDate.get(Calendar.DAY_OF_MONTH), 
			nHour, nMinite, nSecond);
		
		final ProgressDialog dlg = ProgressDialog.show(this, null, "正在开启...", true, false);
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub	
				//设置视口
				viewVideo.setPlayer(mPlaySdk);
				
				//回放请求
				final int result = device.startPlayback(0, startTime, callback);
				if (0 == result)
				{
					bPlayback = true;
					
					//请求回放数据
					device.getPlaybackData(0, REQUEST_LARGE_DATA);
					
					//开启请求数据定时器
					mTimerRequestData = new Timer();
					TimerTask task = new TimerTask()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							if (bPlayback && mRequestData > REQUEST_NO_DATA)
							{
								device.getPlaybackData(0, mRequestData);
								mRequestData = REQUEST_NO_DATA;
							}
						}
					};
					mTimerRequestData.schedule(task, 1000, 1000);
				}
				
				//关闭等待对话框
				dlg.dismiss();
				textRecord.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						if (0 != result)
						{
							Toast.makeText(PlaybackActivity.this, "开启失败", Toast.LENGTH_SHORT).show();
						}
						else
						{
							((Button)findViewById(R.id.btn_play)).setText("停止");
						}
					}
				});
			}
		}).start();
	}
	
	//关闭回放
	//该接口需要和开启回放接口同步，本例中通过ProgressDialog锁住界面保证同步。
	void stopPlayback()
	{
		if (bPlayback)
		{
			final ProgressDialog dlg = ProgressDialog.show(this, null, "正在开启...", true, false);
			
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					bPlayback = false;
					
					viewVideo.setPlayer(null);
					
					//取消请求数据定时器
					mTimerRequestData.cancel();
					
					//关闭网络
					device.stopPlayback(0);
					
					//关闭解码
					mPlaySdk.closeStream();
					
					//关闭完成
					dlg.dismiss();
					editDay.post(new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							((Button)findViewById(R.id.btn_play)).setText("回放");
						}
					});
				}
			}).start();
		}
	}

}
