package com.honyum.elevatorMan.adapter

import android.content.Context
import android.content.Intent
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.activity.common.EveListInfoActivity
import com.honyum.elevatorMan.activity.common.ShowElevatInfoActivity
import com.honyum.elevatorMan.net.EleInfoByIdResponseBody

/**
 * Created by Star on 2017/10/20.
 */
class EleInfoListAdapter(val datas: List<EleInfoByIdResponseBody>?, context: Context?, layoutId: Int = R.layout.item_ele_info) : BaseListViewAdapter<EleInfoByIdResponseBody>(datas, context, layoutId) {

    override fun bindData(holder: BaseViewHolder?, t: EleInfoByIdResponseBody?, index: Int) {

        holder?.setText(R.id.tv_name,t?.communityName+t?.buildingCode+"楼"+t?.unitCode+"单元")?.setText(R.id.tv_ele_id,t?.liftNum)
                ?.setOnClickListener(R.id.rl_ele_info,{
                    var intent = Intent(mContext, ShowElevatInfoActivity::class.java)
                    intent.putExtra("info", t?.liftNum)
                    mContext.startActivity(intent)})



    }

}