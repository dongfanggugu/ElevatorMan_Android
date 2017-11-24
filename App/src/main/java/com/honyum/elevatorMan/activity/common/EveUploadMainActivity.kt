package com.honyum.elevatorMan.activity.common

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ListView
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.adapter.ProjectListAdapter
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.net.*
import com.honyum.elevatorMan.net.base.NetConstant
import com.honyum.elevatorMan.net.base.NetTask
import com.honyum.elevatorMan.net.base.NewRequestHead
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.activity_eleinfo_upload.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult


/**
 * Created by Star on 2017/11/1.
 */
class EveUploadMainActivity :BaseActivityWraper(),View.OnClickListener{

    var y :Double = 0.0
    var x :Double = 0.0
    private lateinit var mLocationClient: LocationClient
    private lateinit var mBDLocationListener: BDLocationListener

    private lateinit var info_list :List<ProjectInfoResponseBody>
    private lateinit var change_list :List<ProjectInfoResponseBody>


    private lateinit var mAdapter :ProjectListAdapter

    companion object {
        private val REQUEST_CODE = 10
    }
    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.ll_scan->toScan()
            R.id.ll_input_info->toInput()
            R.id.ll_upload_data->toUpload()
        }
    }
    private fun toScan()
    {
        startActivityForResult<CaptureActivity>(REQUEST_CODE);
    }
    private fun toInput()
    {
        startActivity<InputEveNumActivity>()
    }
    private fun toUpload()
    {
        startActivity<RecordUpdateListActivity>()
    }

    private fun RequestProjectInfo(x :Double,y:Double)
    {
        var server = config.server+NetConstant.GET_COMMUNITY_BY_DISTANCE
        var body = ProjectInfoRequestBody(x,y)
        var request = ProjectInfoRequest()
        request.body = body
        var head = NewRequestHead().setuserId(config.userId).setaccessToken(config.token)
        request.head =head
        var netTask = object : NetTask(server,request) {
            override fun onResponse(task: NetTask?, result: String?) {
                var response = ProjectInfoResponse.getResult(result)
                info_list = response?.body
                mAdapter = ProjectListAdapter(info_list,ctx)
                project_list.adapter = mAdapter
                et_project_name.isFocusable =  true
                et_project_name.isClickable = true
                et_project_name.hint= "请输入项目名称"
            }

        }
        addTask(netTask)

    }

    override fun getTitleString(): String {
        return "信息采集"
    }

    private inner class MyBDLocationListener : BDLocationListener {

        override fun onReceiveLocation(bdLocation: BDLocation?) {
            // 非空判断
            if (bdLocation != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                y = bdLocation.latitude
                x=bdLocation.longitude
                val address = bdLocation.addrStr
                if (mLocationClient.isStarted) {
                    // 获得位置之后停止定位
                    mLocationClient.stop()
                }
                RequestProjectInfo(x,y)

            }
        }
    }

    /**
     * 获得所在位置经纬度及详细地址
     */
    fun getLocation() {
        // 声明定位参数
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy// 设置定位模式 高精度
        option.coorType = "bd09ll"// 设置返回定位结果是百度经纬度 默认gcj02
        option.scanSpan = 5000// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true)// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true)// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.locOption = option
        // 启动定位
        mLocationClient.start()

    }
    override fun initView() {
        mLocationClient = LocationClient(applicationContext)
        mBDLocationListener = MyBDLocationListener()
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener)
        getLocation()

        ll_scan.setOnClickListener(this)
        ll_input_info.setOnClickListener(this)
        ll_upload_data.setOnClickListener(this)

        et_project_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s.isNullOrBlank())
                {
                    mAdapter.fillDatas(info_list)
                    mAdapter.notifyDataSetChanged()
                }
                change_list = info_list.filter { it.name.contains(s.toString(),true)}
                mAdapter.fillDatas(change_list)
                mAdapter.notifyDataSetChanged()

            }

        })
        tv_cancel.onClick { et_project_name.setText("") }
    }

    override fun getLayoutID(): Int {
    return R.layout.activity_eleinfo_upload
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                val bundle = data!!.getExtras() ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) === CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    val s = result.split("liftNum=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    if (s.size > 1) {
                        val it = Intent(this, ShowElevatInfoActivity::class.java)
                        it.putExtra("info", s[s.size - 1])
                        startActivity(it)

                    } else {
                        showToast("扫描失败！")
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) === CodeUtils.RESULT_FAILED) {

                    showToast("扫描失败！")
                }
            }
        }
    }
}