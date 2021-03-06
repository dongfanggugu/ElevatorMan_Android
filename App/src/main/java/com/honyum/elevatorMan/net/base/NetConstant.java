
package com.honyum.elevatorMan.net.base;

public class NetConstant {

    /**
     * 返回成功
     */
    public static final String RSP_CODE_SUC_0 = "0";

    /**
     * 服务器重启，导致登陆超时
     */
    public static final String RSP_CODE_TIME_OUT = "-2";

    /**
     * 其他人登陆
     */
    public static final String RSP_CODE_OTHER_LOGIN = "-3";

    /**
     * @Fields login:
     */
    public static final String URL_LOG_IN = "/login";

    /**
     * 请求报警信息
     */
    public static final String URL_ALARM_INFO = "/getAlarmDetail";


    /**
     * 上传位置信息
     */
    public static final String URL_REPORT_LOCATION = "/updateLocation";

    /**
     * 接受任务
     */
    public static final String URL_ACCEPT_ALARM = "/userAcceptAlarm";

    /**
     * 上报状态
     */
    public static final String URL_REPORT_STATE = "/saveProcessRecord";


    /**
     * 物业请求最近的一条报警信息
     */
    public static final String URL_ALARM_LIST_ONE = "/getOneAlarmListByUserId";

    /**
     * 物业请求报警信息列表
     */
    public static final String URL_ALARM_LIST = "/getAlarmListByUserId";

    /**
     * 维修工请求报警信息列表
     */
    public static final String URL_WORKER_ALARM_LIST = "/getAlarmListByRepairUserId";

    /**
     * 物业请求项目列表
     */
    public static final String URL_PROJECT_LIST = "/getElevatorList";

    /**
     * 物业报警
     */
    public static final String URL_REPORT_ALARM = "/alarm";

    /**
     * 物业请求报警状态
     */
    public static final String URL_ALARM_STATE = "/getRepairListByAlarmId";

    /**
     * 维修工途中遇到意外情况上报
     */
    public static final String URL_WORKER_EXCEPT = "/unexpectedByUser";

    /**
     * 物业确认报警救援完成
     */
    public static final String URL_CONFIRM_ALARM = "/propertyConfirmComplete";

    /**
     * 版本检测接口
     */
    public static final String URL_VERSION_CHECK = "/checkVersion";


    /**
     * 退出登录
     */
    public static final String URL_LOG_OUT = "/logout";

    /**
     * 请求报警地点周围维修工位置
     */
    public static final String URL_WORKERS = "/getNearUserLocation";

    /**
     * 上报时间节点
     */
    public static final String URL_REPORT_TIME = "/online";

    /**
     * 请求维保电梯信息
     */
    public static final String URL_GET_LIFT_INFO = "/getMainElevatorList";

    /**
     * 上报维保结果
     */
    public static final String URL_REPORT_MAIN = "/finishMain";

    /**
     * 上报维保计划
     */
    public static final String URL_REPORT_PLAN = "/newMainPlan";

    /**
     * 修改维保计划
     */
    public static final String URL_MODIFY_PLAN = "/updateMainPlan";

    /**
     * 删除维保计划
     */
    public static final String URL_DELETE_PLAN = "/removeMainPlan";

    /**
     * 维修工注册
     */
    public static final String URL_WORKER_REGISTER = "/registerRepair";

    /**
     * 物业获取维保电梯计划列表
     */
    public static final String URL_PRO_PLAN_LIST = "/getMainPlanByPropertyId";

    /**
     * 物业上报维保计划处置结果
     */
    public static final String URL_PRO_REPORT_PLAN_RESULT = "/verifyMainPlan";

    /**
     * 物业获取维保完成待确认的电梯列表
     */
    public static final String URL_PRO_GET_FINISH_RESULT = "/getFinishedMainList";

    /**
     * 物业获取维保历史
     */
    public static final String URL_PRO_GET_MAIN_HISTORY = "/getHistoryMainList";

    /**
     * 物业获取维保详情
     */
    public static final String URL_PRO_GET_MAIN_DETAIL = "/getMainDetail";

    /**
     * 修改个人信息
     */
    public static final String URL_MODIFY_INFO = "/editUser";

    /**
     * 密码修改
     */
    public static final String URL_PWD_MODIFY = "/editUserPwd";

    /**
     * 上传头像
     */
    public static final String URL_UPLOAD_ICON = "/updateLoadPic";


    /**
     * 请求维保公司列表
     */
    public static final String URL_REQUEST_COMPANY = "/getBranchs";

    /**
     * 重置密码接口
     */
    public static final String URL_RESET_PWD = "/resetPWD";

    /**
     * 获取汉邦一点通视频参数信息
     */
    public static final String URL_REQUEST_YDT = "/getYdtParam";

    /**
     * 接收到通知后的反馈接口
     */
    public static final String URL_NOTIFY_FEEDBACK = "/alarmFeedback";


    public static final String URL_ALARM_UNASSIGNED = "/getAlarmListByReceiveAndUnassign";


    /**
     * 上报工作地点
     */
    public static final String URL_REPORT_WORK_PLACE = "/saveOrUpdateResidentAddress";


    /**
     * 上报家庭地址
     */
    public static final String URL_REPORT_HOME_PLACE = "/saveOrUpdateFamilyAddress";


    /**
     * 上传音频
     */
    public static final String UPLOAD_AUDIO = "/uploadAudio";


    /**
     * 添加聊天记录
     */
    public static final String SEND_CHAT = "/addChat";


    /**
     * 获取聊天记录
     */
    public static final String GET_CHAT_LIST = "/getChatList";


    /**
     * 上传图片
     */
    public static final String UPLOAD_CERT = "/updateLoadCert";


    /**
     * 物业获取驻点地址信息
     */
    public static final String GET_PROPERTY_ADDRESS_LIST = "/getPropertyAddressList";


    /**
     * 物业添加驻点地址
     */
    public static final String ADD_PROPERTY_ADDRESS = "/addPropertyAddress";


    /**
     * 请求所有的维保项目
     */
    public static final String GET_ALL_COMMUNITYS_BY_PROPERTY = "/getAllCommunitysByProperty";


    /**
     * 物业公司注册
     */
    public static final String REGISTER_PROPERTY = "/propertyRegist";


    /**
     * 添加物业联系维保公司记录
     */
    public static final String ADD_CONTACT_MAINT = "/addContactMaint";


    /**
     * 维修工获取正在进行中的报修单
     */
    public static final String GET_UNDERWAY_REPAIR = "/getRepairByWorker";


    /**
     * 维修工获取已完成的报修单
     */
    public static final String GET_HISTORY_REPAIR = "/getRepairByWorkerComplete";


    /**
     * 维修工获取已完成的报修单
     */
    public static final String COMMIT_REPAIR_DESCRIBE = "/editRepairByWorker";

    /**
     * 物业获取聊天频道
     */
    public static final String URL_CHAT_CHANNEL = "/getChatChannels";

    /**
     *上传签名
     */
    public static final String URL_UPLOAD_SIGN = "/updateLoadAutograph";

    /**
     * 物业反馈维保不合格
     */
    public static final String URL_MAINT_FAILED = "/backMaint";
}
