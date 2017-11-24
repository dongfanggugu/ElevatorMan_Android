package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.data.Atom
import com.honyum.elevatorMan.net.base.RequestBean
import com.honyum.elevatorMan.net.base.Response
import java.io.Serializable

/**
 * Created by Star on 2017/11/1.
 */
data class ProjectInfoResponse(val body: List<ProjectInfoResponseBody> = ArrayList<ProjectInfoResponseBody>()):Response()
{
    companion object {
        fun getResult(json:String?):ProjectInfoResponse
        {
            return Atom.parseFromJson(ProjectInfoResponse::class.java,json) as ProjectInfoResponse
        }
    }
}


data class ProjectInfoResponseBody(val id :String = "",val name:String = "") : Serializable