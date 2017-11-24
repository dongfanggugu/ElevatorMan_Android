package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.net.base.RequestBean
import java.io.Serializable

/**
 * Created by Star on 2017/11/1.
 */
data class EleInfoRequest(var body :EleInfoRequestBody = EleInfoRequestBody()):RequestBean()

data class EleInfoRequestBody(var communityId:String = "") :Serializable