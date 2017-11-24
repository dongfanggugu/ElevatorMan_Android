package com.honyum.elevatorMan.activity.company

import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.data.InsuranceOrder
import kotlinx.android.synthetic.main.activity_insurance_detail.*

/**
 * Created by Star on 2017/10/23.
 */
class InsuranceActivity : BaseActivityWraper() {
    override fun getTitleString(): String {
        return "保单详情"
    }

    override fun initView() {


        var data = intent.extras.getSerializable("data") as InsuranceOrder

        if (data==null)
        {
            showToast("获取保险消息失败！")
            return
        }
        tv_insurance_name.text = data.policyInfo.insuranceSchemeInfo?.name
        tv_insurance_no.text = "NO.${data.policyInfo.code}"
        tv_insurance_company.text = "保险公司：${data.policyInfo.insuranceSchemeInfo?.companyName}"
        tv_name.text = "${data.createUser}"
        tv_benamed.text = "${data.userName}"

        tv_date.text = "${data.policyInfo.cycle}天"


        tv_start.text = "${data.policyInfo.startTime}"

        tv_end.text = "${data.policyInfo.endTime}"


    }

    override fun getLayoutID(): Int {
        return R.layout.activity_insurance_detail
    }
}