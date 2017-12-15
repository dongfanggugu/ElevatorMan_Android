package com.honyum.elevatorMan.activity.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.adapter.BaseListViewAdapter
import com.honyum.elevatorMan.adapter.BaseViewHolder
import com.honyum.elevatorMan.base.BaseFragmentActivity
import com.honyum.elevatorMan.data.ToDoInfo
import com.honyum.elevatorMan.net.UploadPageRequest
import com.honyum.elevatorMan.net.base.NetTask
import com.honyum.elevatorMan.net.base.NewRequestHead
import com.honyum.elevatorMan.net.base.RequestBean
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.act

/**
 * Created by Star on 2017/12/14.
 */
class ToDoFragment : Fragment() {

    private lateinit var mContext: BaseFragmentActivity
    private var mView:View? = null
    private fun getImageRequestBean(userId: String, token: String): RequestBean {
        val request = UploadPageRequest()
        request.head = NewRequestHead().setuserId(userId).setaccessToken(token)
        request.body = request.UploadPageRequestBody().setPage(0).setRows(5).setBranchId(mContext.config.branchId)
        return request
    }

    private fun requestSubmitInfo() {

        var server: String =
                mContext.config.pcServer + "/mobileProcess/undealDataGrid"
        var netTask = object : NetTask(server, getImageRequestBean(mContext.config.userId, mContext.config.token)) {
            override fun onResponse(task: NetTask?, result: String?) {

              //  mView?.find<ListView>(R.id.lv_list)?.adapter = ToDoAdapter(,mContext)
            }
        }
        mContext.addTask(netTask)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity as BaseFragmentActivity
        mView = inflater?.inflate(R.layout.todo_fragment, container, false)
        return mView
    }

    override fun onHiddenChanged(hidden: Boolean) {

    }

    class ToDoAdapter(datas: MutableList<ToDoInfo>?, context: Context?, layoutId: Int = R.layout.item_todo) : BaseListViewAdapter<ToDoInfo>(datas, context, layoutId) {
        override fun bindData(holder: BaseViewHolder?, t: ToDoInfo, index: Int) {


        }
    }
}