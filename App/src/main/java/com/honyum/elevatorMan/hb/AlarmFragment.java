package com.honyum.elevatorMan.hb;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hanbang.ydtsdk.AlarmInformation;
import com.hanbang.ydtsdk.AlarmParam;
import com.hanbang.ydtsdk.YdtNetSDK;
import com.honyum.elevatorMan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AlarmFragment extends Fragment implements View.OnClickListener
{

    /**
     * 线程
     */
    private ExecutorService threadPoll = Executors.newSingleThreadExecutor();

    /**
     * 一点通网络库对象
     */
    YdtNetSDK mYdtNetSDK;

    /**
     * 设备序列号
     */
    String mDeviceSn;

    /**
     * 开始时间
     */
    long beginTime;

    /**
     * 结束时间
     */
    long endTime;

    /**
     * 一天换算为毫秒
     */
    final static long TIME_MILLIS_OF_DAY = 1L * 24 * 60 * 60 * 1000;

    /**
     * 起始位置
     */
    int mStartNo = 0;

    /**
     * 循环一次获取的条数
     */
    int mCount = 20;

    /**
     *设备序列号
     */
//    EditText mEditText;

    /**
     * 报警信息列表
     */
    List<AlarmParam> mAlarmParams;

    /**
     * 报警信息列表
     */
    ListView alarmList;

    ProgressDialog dialog;

    /**
     * 设备密码
     */
    String DEVICE_PASSWORD = "888888";

    /**
     * 设备的分享类型， 0 - 自有设备， 1 - 分享给该用户的设备， 2 - 公共设备
     */
    int SHARE_TYPE = 0;

    /**
     * 绑定标识， 1 - 绑定， 0 - 解绑， -1 - 禁止设备报警上传
     */
    int BIND_FLAG = 1;

    Spinner deviceSpinner;//设备选择器

    List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

    //选中的设备
    DeviceInfo mSelectDevice;


    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        // Inflate the layout for this fragment

        mYdtNetSDK = AccountInfo.getInstance().getYdtNetSDK();
        View view = inflater.inflate( R.layout.fragment_alarm_hb, container, false );

        beginTime = System.currentTimeMillis() - TIME_MILLIS_OF_DAY;
        endTime = System.currentTimeMillis();
        mAlarmParams = new ArrayList<AlarmParam>(  );

        initView( view );
        return view;

    }

    @Override
    public void onHiddenChanged( boolean hidden )
    {
        super.onHiddenChanged( hidden );

        if ( hidden )
        {
        }
        else
        {
            deviceList.clear();
            deviceList.addAll( AccountInfo.getInstance().getYdtDeviceInfos() );

            ArrayAdapter<DeviceInfo> adapter = new ArrayAdapter<DeviceInfo>( getActivity(), android.R.layout.simple_spinner_item, deviceList );
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            deviceSpinner.setAdapter( adapter );
        }
    }

    private void initView( View view )
    {

//        mEditText = (EditText) view.findViewById( R.id.deicesn_text );
        //绑定设备报警功能
        view.findViewById( R.id.bind_alarm ).setOnClickListener( this );
        //获取多条报警信息
        view.findViewById( R.id.get_sum_alarm ).setOnClickListener( this );
        //解绑设备
        view.findViewById( R.id.unbind ).setOnClickListener( this );

        deviceSpinner = (Spinner) view.findViewById( R.id.device_spinner );

        deviceSpinner.setSelection( 0 );

        alarmList = (ListView) view.findViewById( R.id.list_alarm );

        dialog = new ProgressDialog( getActivity() );
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        dialog.setMessage( "请稍等。。。" );
        dialog.setCancelable( false );

        /**
         * 选择设备
         */
        deviceSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
            {
                deviceSpinner.setSelection( position );
                mSelectDevice = deviceList.get( position );
            }

            @Override
            public void onNothingSelected( AdapterView<?> parent )
            {

            }
        } );

        //列表点击事件，根据alarmId回去单条报警信息
        alarmList.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> parent, View view, final int position, long id )
            {
                threadPoll.execute( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AlarmInformation information = mYdtNetSDK.getSingleAlarmInfo( mAlarmParams.get( position ).alarmId );
                        if ( information.nErrorCode == 0 )
                        {
                            //获取成功
                        }
                    }
                } );

            }
        } );

    }

    @Override
    public void onClick( View v )
    {

        switch ( v.getId() )
        {

        //绑定设备报警功能
        case R.id.bind_alarm:
            dialog.show();
            threadPoll.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    BIND_FLAG = 1;//绑定
                    mDeviceSn = mSelectDevice.deviceSn;
                    int error = mYdtNetSDK.bindDeviceAlarm( mDeviceSn, DEVICE_PASSWORD, SHARE_TYPE, BIND_FLAG );
                    if ( error == 0 )
                    {
                        deviceSpinner.post( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText( getActivity(),"绑定成功",Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                    dialog.dismiss();
                }
            } );
            break;

            //获取多条报警信息
        case R.id.get_sum_alarm:
            dialog.show();
            threadPoll.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    /**
                     * mDeviceSn: 设备序列号
                     * beginTime：报警起始时间（按照自己的需求传递，此处为一天前）
                     * endTime：报警结束时间(此处为当前时间)
                     * mStartNo:起始位置
                     * mCount:循环一次获取的条数
                     */
                    while ( true )
                    {
                        AlarmInformation alarmInf = mYdtNetSDK.getAlarmList( mDeviceSn, beginTime, endTime, mStartNo, mCount );
                        if ( null == alarmInf.alarmList || alarmInf.alarmList.size() == 0 )
                        {
                            break;
                        }
                        mAlarmParams.addAll( alarmInf.alarmList );

                        if ( mCount > alarmInf.alarmList.size() )
                        {
                            break;
                        }
                        mStartNo = mStartNo + mCount;
                    }

                    alarmList.post( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            dialog.dismiss();
                            AlarmAdapter adapter = new AlarmAdapter( getActivity(), mAlarmParams );
                            alarmList.setAdapter( adapter );
                        }
                    } );

                }
            } );

            break;

        case R.id.unbind:
            dialog.show();
            threadPoll.execute( new Runnable()
            {
                @Override
                public void run()
                {

                    BIND_FLAG = 0;//解绑
                    mDeviceSn = mSelectDevice.deviceSn;
                    int error = mYdtNetSDK.bindDeviceAlarm( mDeviceSn, DEVICE_PASSWORD, SHARE_TYPE, BIND_FLAG );
                    if ( error == 0 )
                    {
                        deviceSpinner.post( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText( getActivity(),"解绑成功",Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                    dialog.dismiss();
                }
            } );
            break;

        }
    }

    class AlarmAdapter extends BaseAdapter
    {

        private LayoutInflater mInflater;
        private List<AlarmParam> alarms;

        public AlarmAdapter( Context context,List<AlarmParam> alarmParams )
        {
            mInflater = LayoutInflater.from( context );
            this.alarms = alarmParams;
        }

        @Override
        public int getCount()
        {
            return alarms.size();
        }

        @Override
        public Object getItem( int position )
        {
            return alarms.get( position );
        }

        @Override
        public long getItemId( int position )
        {
            return 0;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            ViewHolder viewHolder = null;
            if ( convertView == null )
            {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate( R.layout.item_alarm, null );
                viewHolder.alarmJson = (TextView) convertView.findViewById( R.id.alarm_json );
                convertView.setTag( viewHolder );
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.alarmJson.setText( alarms.get( position ).alarmJson );
            return convertView;
        }
    }

    class ViewHolder
    {
        TextView alarmJson;
    }
}
