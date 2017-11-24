package com.honyum.elevatorMan.data

import java.io.Serializable

/**
 * Created by Star on 2017/10/23.
 */
data class InsuranceSchemeInfo(var code:String =  "",
                               var companyName :String=  "",
                               var content :String=  "",
                               var cycle :Int = 0,
                               var id :String=  "",
                               var name :String=  "",
                               var price :Double=  0.0,
                               var state :String=  ""
                               ) :Serializable
