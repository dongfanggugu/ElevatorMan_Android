package com.honyum.elevatorMan.adapter

import android.content.Context
import android.content.Intent
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.activity.company.InsuranceActivity
import com.honyum.elevatorMan.data.InsuranceOrder

/**
 * Created by Star on 2017/10/20.
 */
class InsuranceListAdapter(val datas: List<InsuranceOrder>?, context: Context?, layoutId: Int = R.layout.item_insurance_detail) : BaseListViewAdapter<InsuranceOrder>(datas, context, layoutId) {

    override fun bindData(holder: BaseViewHolder?, t: InsuranceOrder?, index: Int) {

        holder?.setText(R.id.tv_insurance_name, t?.policyInfo?.insuranceSchemeInfo?.name)?.setText(R.id.tv_insurance_company, "保险公司：${t?.policyInfo?.insuranceSchemeInfo?.companyName}")
                ?.setText(R.id.tv_insurance_date, "保险期限：${t?.policyInfo?.startTime.toString()} - ${t?.policyInfo?.endTime.toString()}")
                ?.setOnClickListener(R.id.rl_data, {
                    var intent = Intent(mContext, InsuranceActivity::class.java)
                    intent.putExtra("data", t)
                    mContext.startActivity(intent)
                })
    }

}