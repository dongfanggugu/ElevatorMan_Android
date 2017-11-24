package com.honyum.elevatorMan.activity.common

import android.text.TextUtils
import com.honyum.elevatorMan.R
import com.honyum.elevatorMan.base.BaseActivityWraper
import kotlinx.android.synthetic.main.activity_input_evecode.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

/**
 * Created by Star on 2017/11/1.
 */
class InputEveNumActivity : BaseActivityWraper() {
    var num = ""
    override fun getTitleString(): String {
        return "请输入电梯编号"
    }

    override fun initView() {
        tv_submit.onClick {
            //            if (Utils.isNumsEmpty(et_num1.getText().toString(), et_num2.getText().toString(), et_num3.getText().toString(), et_num4.getText().toString()) || et_num1.getText().toString().trim().length != 5 || et_num2.getText().toString().trim().length != 5 || et_num3.getText().toString().trim().length != 5 || et_num4.getText().toString().trim().length != 5) {
//                showToast("请输入完整的编号")
//            } else {
//                num = et_num1.getText().toString().trim() + et_num2.getText().toString().trim() + et_num3.getText().toString().trim() + et_num4.getText().toString().trim()
//                startActivity<ShowElevatInfoActivity>("info" to num)
//            }
            if (TextUtils.isEmpty(et_num.text.toString()) || et_num.text.length != 20) {
                showToast("请输入完整的编号")
            } else {
                // num = et_num1.getText().toString().trim() + et_num2.getText().toString().trim() + et_num3.getText().toString().trim() + et_num4.getText().toString().trim()
                num = et_num.text.toString()
                startActivity<ShowElevatInfoActivity>("info" to num)
            }
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_input_evecode
    }
}