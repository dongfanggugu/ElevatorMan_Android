package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.data.Atom
import com.honyum.elevatorMan.data.InsuranceOrder
import com.honyum.elevatorMan.net.base.Response
import java.io.Serializable

/**
 * Created by Star on 2017/10/20.
 */
class InsuranceOrderResponse(var body:List<InsuranceOrder> = ArrayList<InsuranceOrder>()) :Response()
{
    companion object {
        @JvmStatic  fun getResponse(json: String): InsuranceOrderResponse {
            return Atom.parseFromJson(InsuranceOrderResponse::class.java, json) as InsuranceOrderResponse
        }
    }

}

class InsuranceOrderResponseBody(var data:List<InsuranceOrder> = ArrayList<InsuranceOrder>()) :Serializable

{

}