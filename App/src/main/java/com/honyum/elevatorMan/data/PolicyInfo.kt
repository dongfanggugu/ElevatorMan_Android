package com.honyum.elevatorMan.data

import java.io.Serializable

/**
 * Created by Star on 2017/10/20.
 */
data class PolicyInfo (

     val branchName: String = "",
     val code: String = "",
     val createTime: String = "" ,
     val cycle: Int = 0,
     val endTime: String = "" ,
     val id: String = "" ,
     val name: String = "" ,
     val schemeName: String = "",
     val startTime: String = "" ,
     val state: String = "" ,
     val submitTime: String = "" ,
     val successTime: String = "",
     var insuranceSchemeInfo:InsuranceSchemeInfo = InsuranceSchemeInfo()
):Serializable

data class InsuranceOrder(var birthDay:String  =  "",
                          var card:String=  "",
                          var createTime:String=  "",
                          var createUser:String=  "",
                          var edu:String=  "",
                          var id:String=  "",
                          var policyInfo :PolicyInfo = PolicyInfo(),
                          var political:String =   "",
                          var pro:String=  "",
                          var sex:String=  "",
                          var tel:String=  "",
                          var userName:String=  ""):Serializable