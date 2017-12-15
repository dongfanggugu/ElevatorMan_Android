package com.honyum.elevatorMan.activity.common

import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.constant.ConstantIntent
import com.honyum.elevatorMan.net.SignInRequest
import com.honyum.elevatorMan.net.SignInRequestBody
import com.honyum.elevatorMan.net.base.*
import com.honyum.elevatorMan.utils.FormatDate
import kotlinx.android.synthetic.main.activity_attence.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

/**
 * Created by Star on 2017/10/31.
 */
class SignActivity : BaseActivityWraper(), View.OnClickListener {


    var dialog = null;

    companion object {
        val SIGN_IN = "0"  //签到
        val SIGN_OUT = "1"  //签退
        val SIGN_LEAVE = "2" //请假
        val SIGN_CANCEL_LEAVE = "3" //
        var map = mapOf<Int, String>(Pair(R.id.tv_sign_in, SIGN_IN), Pair(R.id.tv_sign_out, SIGN_OUT), Pair(R.id.tv_leave, SIGN_LEAVE), Pair(R.id.tv_cancel_leave, SIGN_CANCEL_LEAVE))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sign_in, R.id.tv_sign_out
            -> map.get(v?.id)?.let { requestSign(it) }
            R.id.tv_cancel_leave, R.id.tv_leave
            -> signCancel(v?.id)
        }
    }

    private fun getRequest(state: String, signTime: String = "", startTime: String = "", endTime: String = "", reason: String = ""): RequestBean {

        val signRequestBody = SignInRequestBody("", config.branchId ?: "", signTime, state, startTime, endTime, reason)
        val head = NewRequestHead().setaccessToken(config.token ?: "").setuserId(config.userId ?: "")
        var signInRequest = SignInRequest()
        signInRequest.head = head
        signInRequest.body = signRequestBody
        return signInRequest

    }

    private fun requestSign(state: String, signTime: String = "", startTime: String = "", endTime: String = "", reason: String = "") {
        val server = config?.server + NetConstant.SIGN

        var netTask = object : NetTask(server, getRequest(state, signTime, startTime, endTime, reason)) {

            override fun onResponse(task: NetTask?, result: String?) {
                val response = Response.getResponse(result)
                showToast("${response?.head?.rspMsg}")
            }
        }
        addTask(netTask)
    }


    /**
     * 请假\销假 跳转到新页面
     */
    private fun signCancel(id: Int) {
        if (TextUtils.equals(map.get(id), SIGN_LEAVE)) startActivity<SignLeaveActivity>(ConstantIntent.INTENT_TITLE to "请假", ConstantIntent.INTENT_ACTION to SIGN_LEAVE)
        else startActivity<SignLeaveActivity>(ConstantIntent.INTENT_TITLE to "销假", ConstantIntent.INTENT_ACTION to SIGN_CANCEL_LEAVE)

    }

    override fun getTitleString(): String {
        return "考勤"
    }


    override fun initView() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val bannerHeight = dm.widthPixels / 2
        val bannerLayout = fl_banner.layoutParams
        bannerLayout.height = bannerHeight
        fl_banner.layoutParams = bannerLayout


        val dataString = FormatDate.get(5)
        tv_week.text = dataString.split(" ")[1]
        tv_date.text = dataString.split(" ")[0]
        tv_sign_in.setOnClickListener(this)
        tv_sign_out.setOnClickListener(this)
        tv_cancel_leave.setOnClickListener(this)
        tv_leave.setOnClickListener(this)
    }


    override fun getLayoutID(): Int {
        return R.layout.activity_attence
    }

}