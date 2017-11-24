package com.honyum.elevatorMan.net

import com.honyum.elevatorMan.data.Atom
import com.honyum.elevatorMan.net.base.Response

/**
 * Created by Star on 2017/10/31.
 */
data class AttendanceReponse(val body:AttendanceReponseBody = AttendanceReponseBody()) :Response() {

    companion object {
        fun getResponse(json :String):AttendanceReponse
        {
            return Atom.parseFromJson(AttendanceReponse::class.java, json) as AttendanceReponse
        }
    }


}
data class AttendanceReponseBody(val id :String = "",val startTime:String = "",val endTime:String = "",val reason:String="") {
    constructor(id: String) : this() {}
}