package com.honyum.elevatorMan.data

/**
 * Created by Star on 2017/12/14.
 */
data class ToDoInfo(var id: String? = null,
                    var branchId: String? = null,
                    var processId: String? = null,
                    var processName: String? = null,
                    var processVesion: String? = null,
                    var processType: String? = null,
                    var bizType: String? = null,
                    var bizId: String? = null,
                    var bizCode: String? = null,
                    var bizName: String? = null,
                    var bizDesc: String? = null,
                    var bizURL: String? = null,
                    var currUserId: String? = null,
                    var currUserName: String? = null,
                    var currUserTel: String? = null,
                    var preUserId: String? = null,
                    var preUserName: String? = null,
                    var preUserTel: String? = null,
                    var nodeType: String? = null,
                    var nodeId: String? = null,
                    var nodeName: String? = null,
                    var parentId: String? = null,
                    var statusCode: String? = null,
                    var processOpinion: String? = null,
                    var processResult: String? = null,
                    var processSource: String? = null,//处理终端PC，ANDROID，IOS，WEIXIN
                    var isLastNode: String? = null,
                    var isFinish: String? = null,
                    var processDate: String? = null,
                    var createDate: String? = null,
                    var lastUpdateDate: String? = null)
