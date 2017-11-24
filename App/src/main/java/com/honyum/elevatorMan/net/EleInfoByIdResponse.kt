package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.data.Atom
import com.honyum.elevatorMan.net.base.Response

/**
 * Created by Star on 2017/11/1.
 */
data class EleInfoByIdResponse(var body: List<EleInfoByIdResponseBody> = ArrayList<EleInfoByIdResponseBody>()) : Response() {
    companion object {

        fun getResult(json :String?) :EleInfoByIdResponse
        {

            return Atom.parseFromJson(EleInfoByIdResponse::class.java,json) as EleInfoByIdResponse
        }
    }
}


data class EleInfoByIdResponseBody(var id: String = "", var liftNum: String = "", var communityName: String = "", var unitCode: String = "", var buildingCode: String = "")