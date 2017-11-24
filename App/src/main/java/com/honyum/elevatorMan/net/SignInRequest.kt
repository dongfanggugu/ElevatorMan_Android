package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.net.base.RequestBean

/**
 * Created by Star on 2017/10/31.
 */
data class SignInRequestBody(val id :String = "",val branchId: String = "", val signTime: String="", val state: String="", val startTime: String?="", val endTime: String?="", val reason: String?="") {


}

data class SignInRequest(var body:SignInRequestBody = SignInRequestBody())  : RequestBean() {


}