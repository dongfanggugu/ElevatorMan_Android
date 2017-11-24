package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.data.Atom
import com.honyum.elevatorMan.net.base.Response

import java.io.Serializable

/**
 * Created by Star on 2017/10/19.
 */
data class GetApplyResponse(var body:GetApplyResponseBody = GetApplyResponseBody()) : Response() {

    companion object {
        @JvmStatic fun getResponse(json: String): GetApplyResponse {
            return Atom.parseFromJson(GetApplyResponse::class.java, json) as GetApplyResponse
        }
    }
}

data class GetApplyResponseBody(var address:String="",
                                var code:String="",
                                var reason:String = "",
                                var createTime:String="",
                                var email:String="",
                                var id:String="",
                                var manager :String = "",
                                var licenceCode:String="",
                                var licenceImg:String="",
                                var licenceImgb:String="",
                                var name:String="",
                                var remarks:String="",
                                var state:String="",
                                var tel:String="",
                                var type:String="",
                                var userId:String="",
                                var userName:String="",
                                var userTel:String="") :Serializable
{
    constructor(name:String) : this()
    {}
}