package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.net.base.RequestBean

/**
 * Created by Star on 2017/11/1.
 */
data class ProjectInfoRequest(var body:ProjectInfoRequestBody=ProjectInfoRequestBody()) :RequestBean()

data class ProjectInfoRequestBody(var lng:Double = 0.0,var lat:Double = 0.0)