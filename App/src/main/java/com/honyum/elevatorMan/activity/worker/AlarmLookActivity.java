package com.honyum.elevatorMan.activity.worker;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hanbang.netsdk.BaseNetControl;
import com.hanbang.playsdk.PlaySDK;
import com.hanbang.playsdk.PlaySurfaceView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseActivityWraper;
import com.honyum.elevatorMan.utils.DeviceInfoUtils;

/**
 * Created by Star on 2017/6/28.
 */

public class AlarmLookActivity extends BaseActivityWraper {
    PlaySurfaceView videoView;
    private boolean isPreviewing;
    //解码对象
    PlaySDK mPlayer = new PlaySDK();

    @Override
    public String getTitleString() {
        return "监控观看";
    }

    @Override
    protected void initView() {


        videoView = (PlaySurfaceView) findViewById(R.id.video_surface);
        findView(R.id.tv_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_alarmlook;
    }

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
                DeviceInfoUtils.mSelectDevice.hbNetCtrl.stopPreview( 1);

                //停止解码
                mPlayer.closeStream();
                isPreviewing = false;
            }
        } ).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRealPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(DeviceInfoUtils.isVideoLoaded)
        {
            startRealplay();
        }
        //startRealplay();
    }

    /**
     * 开启预览
     */
    private void startRealplay()
    {
//        if ( null == DeviceInfoUtils.mSelectDevice)
//        {
//            Toast.makeText( getActivity(), "未选择设备", Toast.LENGTH_LONG ).show();
//            return;
//        }
//
//        if ( !DeviceInfoUtils.mSelectDevice.isOnline )
//        {
//            Toast.makeText( getActivity(), "请先登录设备", Toast.LENGTH_LONG ).show();
//            return;
//        }
//
//        if ( bPlayback )
//        {
//            Toast.makeText( getActivity(), "请先关闭回放", Toast.LENGTH_LONG ).show();
//            return;
//        }

        //由于开启预览的操作比较耗时，需起线程处理
//        progressDialog = ProgressDialog.show( getActivity(), "", "正在开启预览...", false );
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                //开启预览
                final int result = DeviceInfoUtils.mSelectDevice.hbNetCtrl.startPreview( 1, 1, previewCallback );
              
                            //开启成功
                isPreviewing = true;
                    
            }
        } ).start();
    }
}
