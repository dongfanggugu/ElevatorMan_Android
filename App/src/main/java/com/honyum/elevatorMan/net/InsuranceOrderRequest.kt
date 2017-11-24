package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.net.base.RequestBean
import java.io.Serializable

/**
 * Created by Star on 2017/10/20.
 */
data class InsuranceOrderRequest(var body: InsuranceOrderRequestBody = InsuranceOrderRequestBody()) :RequestBean() {

}
data class InsuranceOrderRequestBody( var name :String = "",var tel:String = ""):Serializable