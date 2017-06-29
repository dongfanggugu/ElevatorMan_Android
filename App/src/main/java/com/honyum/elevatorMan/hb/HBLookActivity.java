package com.honyum.elevatorMan.hb;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.hb.AlarmFragment;

public class HBLookActivity extends Activity
{


    RadioGroup mRadioGroup;

    RadioButton mDeivceBtn,mVedioBtn,mAlarmBtn;

    /**
     * 设备列表Fragment
     */
//    DeivceListFragment mDLf;

    /**
     * 报警Fragment
     */
    AlarmFragment mAF;

    /**
     * 视频Fragment
     */
    VedioFragment mVF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main_hb);

        initView();
    }

    private void initView()
    {
        mRadioGroup = (RadioGroup) findViewById( R.id.radio_group );
        mDeivceBtn = (RadioButton) findViewById( R.id. radio_btn_device);
        mVedioBtn = (RadioButton) findViewById( R.id.radio_btn_device );
        mAlarmBtn = (RadioButton) findViewById( R.id.radio_btn_alarm );

        FragmentManager fm = getFragmentManager();
//        mDLf = (DeivceListFragment)fm.findFragmentById( R.id.device_fragment );
        mVF = (VedioFragment)fm.findFragmentById( R.id.vedio_fragment );
        mAF = (AlarmFragment)fm.findFragmentById( R.id.alarm_fragment );

        FragmentTransaction ft = fm.beginTransaction();
//        ft.show( mDLf );
        ft.show( mVF );
        ft.hide( mAF );
        ft.commitAllowingStateLoss();

        mRadioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId )
            {
                onTabCheckedChanged( checkedId );
            }
        } );

    }

    /**
     * tab切换时间
     * @param checkedId
     */
    private void onTabCheckedChanged( int checkedId )
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch( checkedId )
        {
        case R.id.radio_btn_device:

            // 设置中部内容
//            ft.show( mDLf );
            ft.show( mVF );
            ft.hide( mAF );
            ft.commitAllowingStateLoss();

            // 设置底部
//            mDeivceBtn.setSelected( true );
            mVedioBtn.setSelected( true );
            mAlarmBtn.setSelected( false );
            break;

        case R.id.radio_btn_video:

            // 设置中部内容
//            ft.hide( mDLf );
            ft.show( mVF );
            ft.hide( mAF );
            ft.commitAllowingStateLoss();

            // 设置底部
            mDeivceBtn.setSelected( false );
            mVedioBtn.setSelected( true );
            mAlarmBtn.setSelected( false );
            break;

        case R.id.radio_btn_alarm:

            // 设置中部内容
//            ft.hide( mDLf );
            ft.hide( mVF );
            ft.show( mAF );
            ft.commitAllowingStateLoss();

            // 设置底部
            mDeivceBtn.setSelected( false );
            mVedioBtn.setSelected( false );
            mAlarmBtn.setSelected( true );
            break;

        default:
            break;
        }
    }



}
