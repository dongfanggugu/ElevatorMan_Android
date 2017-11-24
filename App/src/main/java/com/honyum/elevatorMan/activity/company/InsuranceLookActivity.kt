package com.honyum.elevatorMan.activity.company

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.adapter.InsuranceListAdapter
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.data.InsuranceOrder
import com.honyum.elevatorMan.net.InsuranceOrderRequest
import com.honyum.elevatorMan.net.InsuranceOrderRequestBody
import com.honyum.elevatorMan.net.InsuranceOrderResponse
import com.honyum.elevatorMan.net.base.NetConstant
import com.honyum.elevatorMan.net.base.NetTask
import com.honyum.elevatorMan.net.base.RequestHead
import kotlinx.android.synthetic.main.activity_insurance_list.*
import org.jetbrains.anko.act

/**
 * Created by Star on 2017/10/20.
 */
class InsuranceLookActivity : BaseActivityWraper() {


    private var data: List<InsuranceOrder>? = null

    override fun getTitleString(): String {
        return "保险查看"
    }


    override fun initView() {

        requestData()


    }

    //请求的保险单
    fun inItData(): InsuranceOrderRequest {
        var head = RequestHead()
        val name = config.name
        val tel = config.tel
        var body = InsuranceOrderRequestBody(name, tel)
        var request = InsuranceOrderRequest(body)
        head.accessToken = config.token
        head.userId = config.userId
        request.head = head
        return request


    }

   private fun requestData() {
        var server = config.server + NetConstant.GET_POLICY
        var netTask = object : NetTask(server, inItData()) {
            override fun onResponse(task: NetTask?, result: String?) {
                result?.let {
                    if (InsuranceOrderResponse.getResponse(it).body?.isEmpty()) {
                        tv_tip.visibility = View.VISIBLE
                        return
                    }
                    else
                        data = InsuranceOrderResponse.getResponse(it).body
                    var adapter = InsuranceListAdapter(data,act)
                    lv_insurance_list.adapter = adapter
                } ?: return
            }

        }
        addTask(netTask)


    }

    override fun getLayoutID(): Int {
        return R.layout.activity_insurance_list
    }

}