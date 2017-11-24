package com.honyum.elevatorMan.activity.common

import android.text.Editable
import android.text.TextWatcher
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.adapter.EleInfoListAdapter
import com.honyum.elevatorMan.adapter.ProjectListAdapter
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.constant.ConstantIntent
import com.honyum.elevatorMan.net.*
import com.honyum.elevatorMan.net.base.NetConstant
import com.honyum.elevatorMan.net.base.NetTask
import kotlinx.android.synthetic.main.activity_eleinfo_detail.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by Star on 2017/11/1.
 */
class EveListInfoActivity : BaseActivityWraper() {
    private lateinit var info_list :List<EleInfoByIdResponseBody>
    private lateinit var change_list :List<EleInfoByIdResponseBody>


    private lateinit var mAdapter : EleInfoListAdapter
    override fun getTitleString(): String {
        return "电梯列表"
    }

    override fun initView() {
        var num = intent.getStringExtra(ConstantIntent.INTENT_DATA) ?: return
        et_eve_num.addTextChangedListener(object : TextWatcher {
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
                change_list = info_list.filter { it.liftNum.contains(s.toString(),true)}
                mAdapter.fillDatas(change_list)
                mAdapter.notifyDataSetChanged()

            }

        })
        tv_cancel.onClick { et_eve_num.setText("") }
        requestEleInfo(num)
    }

    private fun requestEleInfo(num: String) {
        var eleInfo = EleInfoRequest()
        eleInfo.body = EleInfoRequestBody(num)
        eleInfo.head = NewRequestHead().setaccessToken(config.token).setuserId(config.userId)

        var server = config.server + NetConstant.GET_ELEVATOR_BY_COMMUNITYID

        var netTask = object : NetTask(server, eleInfo) {
            override fun onResponse(task: NetTask?, result: String?) {
                var response = EleInfoByIdResponse.getResult(result)
                info_list = response?.body
                mAdapter = EleInfoListAdapter(info_list,ctx)
                ele_list.adapter = mAdapter
                et_eve_num.isFocusable =  true
                et_eve_num.isClickable = true
                et_eve_num.hint= "请输入电梯名称"
            }

        }
        addTask(netTask)
    }


    override fun getLayoutID(): Int {
        return R.layout.activity_eleinfo_detail
    }
}