package com.honyum.elevatorMan.activity.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.base.BaseActivityWraper
import com.honyum.elevatorMan.net.UploadPageRequest
import com.honyum.elevatorMan.net.base.NetTask
import com.honyum.elevatorMan.net.base.NewRequestHead
import com.honyum.elevatorMan.net.base.RequestBean
import kotlinx.android.synthetic.main.activity_to_do_list.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by Star on 2017/12/14.
 */
class ToDoListActivity : BaseActivityWraper() {

    private lateinit var todoFragment: Fragment
    private lateinit var doneFragment: Fragment

    private var currFragment: Fragment? = null

    override fun getTitleString(): String {
        return "代办事项"
    }


    override fun initView() {

        tv_todo.onClick {
            switchFragment(todoFragment).commit()
        }
        tv_done.onClick {
            switchFragment(doneFragment).commit()
        }

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_to_do_list
    }

    private fun switchFragment(targetFragment: Fragment): FragmentTransaction {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            currFragment?.let {
                fragmentTransaction.hide(currFragment)
            }
            fragmentTransaction.add(R.id.fragment, targetFragment, targetFragment::class.java.simpleName)
        } else {
            fragmentTransaction.hide(currFragment).show(targetFragment)
        }
        currFragment = targetFragment
        return fragmentTransaction

    }
}