package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.net.base.RequestBean
import java.io.Serializable

/**
 * Created by Star on 2017/10/19.
 */
data class CompanyApplyRequest(var body: CompanyApplyRequestBody = CompanyApplyRequestBody()) : RequestBean() {


}

data class CompanyApplyRequestBody(var appliyId :String = "",var name: String = "", var address: String = "", var licenceCode: String = "",
                                   var manager: String = "", var tel: String = "", var email: String = "",
                                   var type: String = "", var remarks: String = "", var licenceImg: String = "",
                                   var licenceImgb: String = "") : Serializable {

    constructor(name :String) :this()
    {}

}