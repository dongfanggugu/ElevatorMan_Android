package com.honyum.elevatorMan.hb;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hanbang.netsdk.BaseNetControl;
import com.hanbang.netsdk.HBNetCtrl;
import com.hanbang.netsdk.NetSdkError;
import com.hanbang.netsdk.RecordFileParam;
import com.hanbang.playsdk.PlaySDK;
import com.hanbang.playsdk.PlaySurfaceView;
import com.hanbang.ydtsdk.YdtDeviceInfo;
import com.hanbang.ydtsdk.YdtDeviceParam;
import com.hanbang.ydtsdk.YdtNetSDK;
import com.hanbang.ydtsdk.YdtSdkError;
import com.hanbang.ydtsdk.YdtSmsServerInfo;
import com.honyum.elevatorMan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class VedioFragment extends Fragment implements View.OnClickListener
{
    //未知错误
    final static int ERR_UNKNOWN = -201;
    
    //序列号不匹配
    final static int ERR_DISMATCH_SN = -202;
    
    PlaySurfaceView videoView;
    Button btnLoginDevice;//设备登录
    Button btnRealplay;//视频预览
//    Spinner deviceSpinner;//设备选择器
    Button playBack;//回放按钮
    Button getFileBtn;//获取文件的按钮
    TextView mPlayFileText;//显示录像文件
    
    List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();
    DeviceInfo mSelectDevice;
    
    //解码对象
    PlaySDK mPlayer = new PlaySDK();
    
    //是否在预览
    boolean isPreviewing = false;
    
    ProgressDialog progressDialog;
    
    /**
     * 通道
     */
    int  mChannelIndex = 0;
    
    /**
     * 录像类型，此处为全部录像
     */
    int mPlayType = 0XFF;
    
    /**
     * 获取录像时的起始时间
     */
    long beginTime;
    
    final static int REQUEST_LARGE_DATA = 20;        //请求大量数据
    final static int REQUEST_SMALL_DATA = 10;        //请求少量数据
    final static int REQUEST_NO_DATA = 0;            //不请求数据
    
    int mRequestData = REQUEST_NO_DATA;                //请求数据的数据量
    boolean bPlayback = false;                        //是否在回放
    Timer mTimerRequestData;                        //请求数据定时器
    
    /**
     * Spinner选中的位置
     */
    int mPosition = 0;
    
    EditText mEditText;
    
    /**
     * 语音对讲对象
     */
    VoiceTalkback mTalkback;
    
    //预览数据回调
    BaseNetControl.NetDataCallback previewCallback = new BaseNetControl.NetDataCallback()
    {
        /**
         * 网络数据回调
         * @param type 数据类型。
         * @param data 数据源。
         * @param nOffset 数据源的偏移量，即从数据源的nOffset位置开始是有效数据。
         * @param nValidLength 有效数据长度。
         */
        @Override
        public void onNetData(DataType type, byte[] data, int nOffset, int nValidLength, long nTimeStamp )
        {
            if ( !mPlayer.isOpened() )
            {
                //打开解码对象
                if ( mPlayer.openStream( data, nOffset, nValidLength ) )
                {
                    //设置播放视口
                    mPlayer.setPlaySurfaceView( videoView );
                    //设置缓冲模式:均衡
                    mPlayer.setBufferMode( PlaySDK.PLAY_BUFFER_MODE_BALANCED );
                    //设置播放状态
                    mPlayer.play();
                }
            }
            // 转换帧类型（只用于流媒体数据）
            int nFrameType = PlaySDK.PLAY_FRAME_UNKNOWN;
            switch ( type )
            {
            case DATA_TYPE_AUDIO:
                nFrameType = PlaySDK.PLAY_FRAME_AUDIO;
                break;
    
            case DATA_TYPE_IFRAME:
                nFrameType = PlaySDK.PLAY_FRAME_VIDEO_I;
                break;
    
            case DATA_TYPE_PFRAME:
                nFrameType = PlaySDK.PLAY_FRAME_VIDEO_P;
                break;
    
            default:
                nFrameType = PlaySDK.PLAY_FRAME_UNKNOWN;
                break;
            }
            mPlayer.inputData( data, nOffset, nValidLength, nFrameType, nTimeStamp );
        }
        
        @Override
        public void onDisconnected()
        {
            
        }
    };
    
    @Override
    public void onHiddenChanged( boolean hidden )
    {
        super.onHiddenChanged( hidden );
        
        if ( hidden )
        {
        }
        else
        {
            // 创建语音对讲对象
//         deviceList.clear();
//            deviceList.addAll( AccountInfo.getInstance().getYdtDeviceInfos() );
     /*
            ArrayAdapter<DeviceInfo> adapter = new ArrayAdapter<DeviceInfo>( getActivity(), android.R.layout.simple_spinner_item, deviceList );
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            deviceSpinner.setAdapter( adapter );*/
        }
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_vedio_hb, container, false );
        videoView = (PlaySurfaceView) view.findViewById( R.id.video_surface );
        btnLoginDevice = (Button) view.findViewById( R.id.btn_login_device );
        btnRealplay = (Button) view.findViewById( R.id.btn_realplay );
//        deviceSpinner = (Spinner) view.findViewById( R.id.device_spinner );
        getFileBtn = (Button) view.findViewById( R.id.btn_get_file );
        playBack = (Button) view.findViewById( R.id.btn_play_back );
        mPlayFileText = (TextView) view.findViewById( R.id.play_file );
        mEditText = (EditText) view.findViewById( R.id.device_edittext );
        mPlayFileText.setMovementMethod( ScrollingMovementMethod.getInstance() );
        
        btnLoginDevice.setOnClickListener( this );
        btnRealplay.setOnClickListener( this );
        getFileBtn.setOnClickListener( this );
        playBack.setOnClickListener( this );
//        deviceSpinner.setSelection( mPosition );
        
        /**
         * 选择设备
         */
/*        deviceSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
            {
                //切换设备之前先注销之前的设备
                if ( mSelectDevice != null && mSelectDevice.isOnline )
                {
                    Toast.makeText( getActivity(), "请先注销！", Toast.LENGTH_SHORT ).show();
                    deviceSpinner.setSelection( mPosition );
                }
                else
                {
                    mSelectDevice = deviceList.get( position );
                    mPosition = position;
                }
            }
            
            @Override
            public void onNothingSelected( AdapterView<?> parent )
            {
                
            }
        } );*/
        return view;
    }
    
    /**
     * 设备登录
     */
    private void loginDevice()
    {
        progressDialog = ProgressDialog.show( getActivity(), "", "正在登录...", false );
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                YdtNetSDK ydtNetSDK = AccountInfo.getInstance().getYdtNetSDK();
                YdtDeviceInfo ydtDeviceInfo = ydtNetSDK.getSpecifiedDeviceWithoutLogin( mEditText.getText().toString() );
    
                if ( ydtDeviceInfo.nErrorCode == 0 )
                {
                    YdtDeviceParam deviceParam = ydtDeviceInfo.deviceList.get( 0 );
                    mSelectDevice = new DeviceInfo();
                    mSelectDevice.deviceUser = deviceParam.devUser;
                    mSelectDevice.devicePsw = deviceParam.devPassword;
                    mSelectDevice.deviceSn = deviceParam.devSN;
                    mSelectDevice.deviceId = deviceParam.devId;
                    mSelectDevice.deviceName = deviceParam.devName;
                    mSelectDevice.deviceDomain = deviceParam.devDomain;
                    mSelectDevice.domainPort = deviceParam.devDomainPort;
                    mSelectDevice.vveyeId = deviceParam.devVNIp;
                    mSelectDevice.vveyeRemortPort = deviceParam.devVNPort;
                    mSelectDevice.channelCount = deviceParam.devChannelCount;
    
                    //登录设备
                    if ( null == mSelectDevice.hbNetCtrl )
                    {
                        mSelectDevice.hbNetCtrl = new HBNetCtrl();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
    
//                    //首先尝试局域网登录
                    int result = ERR_UNKNOWN;
//                    if ( !mSelectDevice.deviceLanIp.isEmpty() && 0 != mSelectDevice.deviceLanPort )
//                    {
//                        result = mSelectDevice.hbNetCtrl
//                                .login( mSelectDevice.deviceUser, mSelectDevice.devicePsw, mSelectDevice.deviceLanIp, mSelectDevice.deviceLanPort,
//                                        mSelectDevice.callback );
//                    }

                    //如果局域网登录失败，尝试VV穿透
                    if ( 0 != result && !mSelectDevice.vveyeId.isEmpty() && 0 != mSelectDevice.vveyeRemortPort )
                    {
                        result = mSelectDevice.hbNetCtrl
                                .loginVveye( mSelectDevice.deviceUser, mSelectDevice.devicePsw, mSelectDevice.vveyeId, mSelectDevice.vveyeRemortPort,
                                        mSelectDevice.callback );
                    }

//                    //如果失败，尝试域名登录
//                    if ( 0 != result && !mSelectDevice.deviceDomain.isEmpty() && 0 != mSelectDevice.domainPort )
//                    {
//                        result = mSelectDevice.hbNetCtrl
//                                .login( mSelectDevice.deviceUser, mSelectDevice.devicePsw, mSelectDevice.deviceDomain, mSelectDevice.domainPort,
//                                        mSelectDevice.callback );
//                    }
//
//                    //流媒体登录
//                    if ( 0 != result && !mSelectDevice.deviceSn.isEmpty() && !mSelectDevice.deviceId.isEmpty() )
//                    {
//                        // 从一点通获取流媒体参数
////                        YdtNetSDK ydtNetSDK = AccountInfo.getInstance().getYdtNetSDK();
//                        YdtSmsServerInfo info = ydtNetSDK.getSmsServer( mSelectDevice.deviceId );
//                        result = info.nErrorCode;
//                        if ( YdtSdkError.ERR_SUCCESS == result )
//                        {
//                            result = NetSdkError.ERR_CONNECT_FAILED;
//
//                            // 记录流媒体信息
//                            mSelectDevice.smsIp = info.smsServerIp;
//                            mSelectDevice.smsPort = info.smsPort;
//
//                            // 当设备在线时，登录流媒体
//                            if ( 0 == info.deviceStatus )
//                            {
//                                result = mSelectDevice.hbNetCtrl
//                                        .loginSms( mSelectDevice.smsIp, mSelectDevice.smsPort, mSelectDevice.deviceSn, mSelectDevice.channelCount );
//                            }
//                        }
//                    }

                    if ( 0 == result )
                    {
                        //登录成功，对设备序列号进行校验
                        String devSn = mSelectDevice.hbNetCtrl.getSerialNo();
                        if ( !devSn.equals( mSelectDevice.deviceSn ) )
                        {
                            //序列号不匹配，登录失败
                            result = ERR_DISMATCH_SN;
            
                            //注销对错误设备的登录
                            mSelectDevice.hbNetCtrl.logout();
                        }
                        else
                        {
                            mSelectDevice.isOnline = true;
                        }
    
                        //开始预览
                        startRealplay();
    
                        btnLoginDevice.post( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                String tipText = "登录成功";
                                Toast.makeText( getActivity(), tipText, Toast.LENGTH_SHORT ).show();
                            }
                        } );
                        
                    }
                    
                }
                else
                {
                    btnLoginDevice.post( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressDialog.dismiss();
            
                            String tipText = "登录失败";
                            Toast.makeText( getActivity(), tipText, Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } ).start();
    }
    
    /**
     * 设备退出
     */
    private void logoutDevice()
    {
        if ( null == mSelectDevice || null == mSelectDevice.hbNetCtrl )
        {
            return;
        }
        
        progressDialog = ProgressDialog.show( getActivity(), "", "正在注销...", false );
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                //关闭预览
                if ( isPreviewing )
                {
                    //关闭预览
                    mSelectDevice.hbNetCtrl.stopPreview( 0 );
                    
                    //停止解码
                    mPlayer.closeStream();
                    isPreviewing = false;
                }
                
                //关闭回放
                if ( bPlayback )
                {
                    //关闭预览
                    mSelectDevice.hbNetCtrl.stopPlayback( 0 );
                    
                    //停止解码
                    mPlayer.closeStream();
                    bPlayback = false;
                    
                }
                
                mSelectDevice.hbNetCtrl.logout();
                mSelectDevice.isOnline = false;
                
                //修改界面
                btnRealplay.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressDialog.dismiss();
                    }
                } );
            }
        } ).start();
    }
    
    /**
     * 开启预览
     */
    private void startRealplay()
    {
        if ( null == mSelectDevice )
        {
            Toast.makeText( getActivity(), "未选择设备", Toast.LENGTH_LONG ).show();
            return;
        }
        
        if ( !mSelectDevice.isOnline )
        {
            Toast.makeText( getActivity(), "请先登录设备", Toast.LENGTH_LONG ).show();
            return;
        }
        
        if ( bPlayback )
        {
            Toast.makeText( getActivity(), "请先关闭回放", Toast.LENGTH_LONG ).show();
            return;
        }
        
        //由于开启预览的操作比较耗时，需起线程处理
//        progressDialog = ProgressDialog.show( getActivity(), "", "正在开启预览...", false );
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                //开启预览
                final int result = mSelectDevice.hbNetCtrl.startPreview( 1, 1, previewCallback );
                btnRealplay.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( 0 == result )
                        {
                            //开启成功
                            isPreviewing = true;
                        }
                        else
                        {
                            Toast.makeText( getActivity(), "开启预览失败", Toast.LENGTH_SHORT ).show();
                        }
                        progressDialog.dismiss();
                    }
                } );
            }
        } ).start();
    }
    
    /**
     * 暂停
     */
    private void stopRealPlay()
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                //关闭预览
                mSelectDevice.hbNetCtrl.stopPreview( 0 );
                
                //停止解码
                mPlayer.closeStream();
                isPreviewing = false;
            }
        } ).start();
        
    }
    
    @Override
    public void onClick( View v )
    {
        switch ( v.getId() )
        {
        case R.id.btn_login_device:
    
            loginDevice();
/*            if ( btnLoginDevice.getText().toString().equals( "登录设备" ) )
            {
                //设备登录
                loginDevice();
            }
            else
            {
                //退出设备
                logoutDevice();
            }*/
            break;
        
        case R.id.btn_realplay:
    
            mTalkback = new VoiceTalkback( mSelectDevice );
            //对讲
            mTalkback.startTalkback( new FinishCallback()
            {
    
                @Override
                public void onFinish( int error, Object tag )
                {
                    mEditText.post( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText( getActivity(),"开启成功",Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }, null );
            
            break;
        
        case R.id.btn_get_file:
            //获取录像文件
            getRecordFile();
            break;
        
        case R.id.btn_play_back:
            if ( bPlayback )
            {
                //停止回放
                stopPlayBack();
            }
            else
            {
                //开始回放
                playBack();
            }
            break;
        
        default:
            break;
        }
    }
    
    /**
     * 停止视频回放
     */
    private void stopPlayBack()
    {
        if ( bPlayback )
        {
            progressDialog = ProgressDialog.show( getActivity(), "", "请稍等...", false );
            
            new Thread( new Runnable()
            {
                
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    bPlayback = false;
                    
                    //取消请求数据定时器
                    mTimerRequestData.cancel();
                    
                    //关闭网络
                    mSelectDevice.hbNetCtrl.stopPlayback( 0 );
                    
                    //关闭解码
                    mPlayer.closeStream();
                    
                    //关闭完成
                    progressDialog.dismiss();
                    playBack.post( new Runnable()
                    {
                        
                        @Override
                        public void run()
                        {
                            // TODO Auto-generated method stub
                            playBack.setText( "回放" );
                        }
                    } );
                }
            } ).start();
        }
    }
    
    /**
     * 视频回放
     */
    private void playBack()
    {
        
        if ( null == mSelectDevice )
        {
            Toast.makeText( getActivity(), "未选择设备", Toast.LENGTH_LONG ).show();
            return;
        }
        
        if ( !mSelectDevice.isOnline )
        {
            Toast.makeText( getActivity(), "请先登录设备", Toast.LENGTH_LONG ).show();
            return;
        }
        
        Calendar calendar = Calendar.getInstance();
        //日期选择dialog
        Dialog dialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            boolean isFired = false;
            
            @Override
            public void onDateSet( DatePicker view, final int year, final int monthOfYear, final int dayOfMonth )
            {
                //点击确定按钮时的处理
                if ( isFired )
                {
                    return;
                }
                isFired = true;
                
                Calendar c = Calendar.getInstance();
                //时间选择dialog
                Dialog mDialog = new TimePickerDialog( getActivity(), new TimePickerDialog.OnTimeSetListener()
                {
                    
                    boolean isFirst1 = false;
                    
                    @Override
                    public void onTimeSet( TimePicker view, int hourOfDay, int minute )
                    {
                        if ( isFirst1 )
                        {
                            return;
                        }
                        isFirst1 = true;
                        //开始时间
                        final Calendar startTime = Calendar.getInstance();
                        startTime.set( year, monthOfYear, dayOfMonth, hourOfDay, minute );
                        final long beginTime = startTime.getTimeInMillis();
                        //构造结束时间，不能垮天
                        final Calendar stopTime = Calendar.getInstance();
                        stopTime.set( startTime.get( Calendar.YEAR ), startTime.get( Calendar.MONTH ), startTime.get( Calendar.DAY_OF_MONTH ), 23, 59, 59 );
                        final long endTime = stopTime.getTimeInMillis();
                        progressDialog = ProgressDialog.show( getActivity(), "", "请稍等...", false );
                        new Thread( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                /**
                                 * 回放请求
                                 * mChannelIndex : 通道
                                 * startTime : 起始时间
                                 * stopTime :结束时间
                                 */
                                int result = mSelectDevice.hbNetCtrl.startPlayback( mChannelIndex, beginTime, endTime, callback );
                                if ( 0 == result )
                                {
                                    bPlayback = true;
                                    
                                    //请求回放数据
                                    mSelectDevice.hbNetCtrl.getPlaybackData( mChannelIndex, REQUEST_LARGE_DATA );
                                    
                                    //开启请求数据定时器
                                    mTimerRequestData = new Timer();
                                    TimerTask task = new TimerTask()
                                    {
                                        
                                        @Override
                                        public void run()
                                        {
                                            // TODO Auto-generated method stub
                                            if ( bPlayback && mRequestData > REQUEST_NO_DATA )
                                            {
                                                mSelectDevice.hbNetCtrl.getPlaybackData( mChannelIndex, mRequestData );
                                                mRequestData = REQUEST_NO_DATA;
                                            }
                                        }
                                    };
                                    mTimerRequestData.schedule( task, 1000, 1000 );
                                    
                                    playBack.post( new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            playBack.setText( "停止" );
                                        }
                                    } );
                                }
                                
                                progressDialog.dismiss();
                            }
                        } ).start();
                        
                    }
                }, c.get( Calendar.HOUR_OF_DAY ), // 传入年份
                        c.get( Calendar.MINUTE ),// 传入月份
                        false );
                
                mDialog.show();
                
            }
        }, calendar.get( Calendar.YEAR ), // 传入年份
                calendar.get( Calendar.MONTH ), // 传入月份
                calendar.get( Calendar.DAY_OF_MONTH ) // 传入天数
        );
        dialog.show();
    }
    
    /**
     * 获取视频文件
     */
    private void getRecordFile()
    {
        if ( null == mSelectDevice )
        {
            Toast.makeText( getActivity(), "未选择设备", Toast.LENGTH_LONG ).show();
            return;
        }
        
        if ( !mSelectDevice.isOnline )
        {
            Toast.makeText( getActivity(), "请先登录设备", Toast.LENGTH_LONG ).show();
            return;
        }
        
        
        final Calendar calendar = Calendar.getInstance();
        Dialog dialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            boolean isFired = false;
            
            @Override
            public void onDateSet( DatePicker view, final int year, final int monthOfYear, final int dayOfMonth )
            {
                if ( isFired )
                {
                    return;
                }
                progressDialog = ProgressDialog.show( getActivity(), "", "请稍等...", false );
                isFired = true;
                new Thread( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set( year, monthOfYear, dayOfMonth );
                        beginTime = calendar1.getTimeInMillis();
                        //mChannelIndex:
                        final List<RecordFileParam> recordFileParams = mSelectDevice.hbNetCtrl.findRecordFile( mChannelIndex, mPlayType, beginTime );
                        
                        mPlayFileText.post( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressDialog.dismiss();
                                if ( null == recordFileParams )
                                {
                                    Toast.makeText( getActivity(), "未查到录像文件", Toast.LENGTH_SHORT ).show();
                                }
                                else
                                {
                                    //打印录像文件起始时间结束时间
                                    
                                    int fileCount = recordFileParams.size();
                                    StringBuilder recordContent = new StringBuilder();
                                    recordContent.append( "共查询到录像文件数：" ).append( recordFileParams.size() ).append( "\n" );
                                    for ( int i = 0; i < fileCount; i++ )
                                    {
                                        RecordFileParam param = recordFileParams.get( i );
                                        long beginTimeInMillis = param.getStartTime();
                                        long endTimeInMillis = param.getStopTime();
                                        Calendar startTime = Calendar.getInstance();
                                        startTime.setTimeInMillis( beginTimeInMillis );
                                        Calendar stopTime = Calendar.getInstance();
                                        stopTime.setTimeInMillis( endTimeInMillis );
                                        
                                        //序号
                                        recordContent.append( i ).append( " " );
                                        
                                        //开始时间
                                        recordContent.append( startTime.get( Calendar.YEAR ) ).append( "/" );
                                        recordContent.append( startTime.get( Calendar.MONTH ) + 1 ).append( "/" );
                                        recordContent.append( startTime.get( Calendar.DAY_OF_MONTH ) ).append( " " );
                                        recordContent.append( startTime.get( Calendar.HOUR ) ).append( ":" );
                                        recordContent.append( startTime.get( Calendar.MINUTE ) ).append( ":" );
                                        recordContent.append( startTime.get( Calendar.SECOND ) );
                                        
                                        recordContent.append( " - " );
                                        
                                        //结束时间
                                        recordContent.append( stopTime.get( Calendar.YEAR ) ).append( "/" );
                                        recordContent.append( stopTime.get( Calendar.MONTH ) + 1 ).append( "/" );
                                        recordContent.append( stopTime.get( Calendar.DAY_OF_MONTH ) ).append( " " );
                                        recordContent.append( stopTime.get( Calendar.HOUR ) ).append( ":" );
                                        recordContent.append( stopTime.get( Calendar.MINUTE ) ).append( ":" );
                                        recordContent.append( stopTime.get( Calendar.SECOND ) ).append( "\n" );
                                    }
                                    
                                    mPlayFileText.setText( recordContent );
                                }
                            }
                        } );
                        
                    }
                } ).start();
            }
            
        }, calendar.get( Calendar.YEAR ), // 传入年份
                calendar.get( Calendar.MONTH ), // 传入月份
                calendar.get( Calendar.DAY_OF_MONTH ) // 传入天数
        );
        dialog.show();
    }
    
    //回放数据回调
    BaseNetControl.NetDataCallback callback = new BaseNetControl.NetDataCallback()
    {
        
        /**
         * 网络数据回调
         * @param type 数据类型。
         * @param data 数据源。
         * @param nOffset 数据源的偏移量，即从数据源的nOffset位置开始是有效数据。
         * @param nValidLength 有效数据长度。
         */
        @Override
        public void onNetData(DataType type, byte[] data, int nOffset, int nValidLength, long nTimeStamp )
        {
            // TODO Auto-generated method stub
            if ( !mPlayer.isOpened() )
            {
                //打开解码
                if ( mPlayer.openStream( data, nOffset, nValidLength ) )
                {
                    //设置视口
                    mPlayer.setPlaySurfaceView( videoView );
                    
                    //注册缓冲区状态回调
                    mPlayer.setOnBufferStateListener( new PlaySDK.OnBufferStateListener()
                    {
                        
                        @Override
                        public void onBufferStateChanged(PlaySDK player, int oldState, int newState )
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
                        public void onBufferStateAlmostChange(PlaySDK player, boolean almostEmpty )
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
                    } );
                    
                    //设置缓冲模式:流畅性优先
                    mPlayer.setBufferMode( PlaySDK.PLAY_BUFFER_MODE_FLUENCY );
                    
                    //设置播放状态
                    mPlayer.play();
                }
            }
            
            //送入数据
            mPlayer.inputData( data, nOffset, nValidLength );
        }
        
        @Override
        public void onDisconnected()
        {
            // TODO Auto-generated method stub
            
        }
    };
    
}
