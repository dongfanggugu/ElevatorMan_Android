package com.honyum.elevatorMan.hb;

import android.content.Context;

import com.hanbang.netsdk.NetSdkError;
import com.hanbang.ydtsdk.YdtSdkError;
import com.honyum.elevatorMan.R;


/**
 * 错误码<p>
 * 错误码取值范围[-500, -301]
 */
public final class ManagerError
{
    /**
     * 错误码最小值
     */
    public final static int ERR_MIN = -500;

    /**
     * 错误码最大值
     */
    public final static int ERR_MAX = -301;

    /**
     * 成功
     */
    public final static int ERR_SUCCESS = 0;

    /**
     * 未知错误
     */
    public final static int ERR_UNKNOWN = -301;
    
    /**
     * 不支持
     */
    public final static int ERR_NOT_SUPPORTED = -302;
    
    /**
     * 尚未实现
     */
    public final static int ERR_NOT_IMPLEMENTED = -303;
    
    /**
     * 尚未处理完成
     */
    public final static int ERR_IO_PENDING = -304;
    
    /**
     * 空指针
     */
    public final static int ERR_NULL_POINTER = -305;
    
    /**
     * 没有初始化
     */
    public final static int ERR_NOT_INITIALIZED = -306;

    /**
     * 参数错误
     */
    public final static int ERR_INVALID_PARAMETER = -307;
    
    /**
     * 任务被取消
     */
    public final static int ERR_CANCELLED = -308;

    /**
     * 开启录音功能失败
     */
    public final static int ERR_AUDIO_RECORD_FAILED = -309;
    
    /**
     * 没有更多数据
     */
    public final static int ERR_NO_MORE_DATA = -310;
    
    /**
     * 设备离线
     */
    public final static int ERR_DEVICE_DISCONNECTED = -320;

    /**
     * 设备销毁
     */
    public final static int ERR_DEVICE_DESTROYED = -321;

    /**
     * 设备禁用
     */
    public final static int ERR_DEVICE_DISABLED = -322;
    
    /**
     * 已经添加
     */
    public final static int ERR_DEVICE_ALREADY_ADDED = -323;
    
    /**
     * 设备序列号不匹配
     */
    public final static int ERR_DEVICE_SN_NOT_MACTHED = -324;

    /**
     * 网络断开
     */
    public final static int ERR_NETWORK_DISCONNECTED = -325;

    /**
     * 账户名不存在
     */
    public final static int ERR_ACCOUNT_NAME_NOT_EXIST = -330;
    
    /**
     * 密码错误
     */
    public final static int ERR_WRONG_PASSWORD = -331;
    
    /**
     * 账户已经登录
     */
    public final static int ERR_ACCOUNT_ALREAD_LOGINED = -332;

    /**
     * 通道已经打开
     */
    public final static int ERR_ALREADY_OPENED = -333;

    /**
     * 网络命令成功
     */
    public final static int ERR_VIDEO_NETSDK_OK = -335;
    
    /**
     * 开启解码库成功
     */
    public final static int ERR_VIDEO_PLAYSDK_OK = -336;
    
    /**
     * 视频数据缓冲中
     */
    public final static int ERR_VIDEO_BUFFERING = -337;
    
    /**
     * 视频数据缓冲完成
     */
    public final static int ERR_VIDEO_BUFFERING_COMPLETED = -338;
    

    /**
     * 获取错误码对应的消息
     * @param context
     *      [in]进程上下文。
     * @param error
     *      [in]错误码。
     * @return
     *      错误消息。
     */
    public static String getErrorMessage( Context context, int error )
    {
        if ( null == context )
        {
            return "";
        }

        String message;
        if ( error <= NetSdkError.ERR_MAX
                && error >= NetSdkError.ERR_MIN )
        {
            message = NetSdkError.getErrorMessage( context, error );
        }
        else if ( error <= YdtSdkError.ERR_MAX
                && error >= YdtSdkError.ERR_MIN )
        {
            message = YdtSdkError.getErrorMessage( context, error );
        }
        else
        {
            int msgId = R.string.ERR_UNKNOWN;
            switch ( error )
            {
            case ERR_SUCCESS:
                msgId = R.string.ERR_SUCCESS;
                break;

            case ERR_NOT_SUPPORTED:
                break;

            case ERR_NOT_IMPLEMENTED:
                break;

            case ERR_IO_PENDING:
                break;

            case ERR_NULL_POINTER:
                break;

            case ERR_NOT_INITIALIZED:
                break;

            case ERR_INVALID_PARAMETER:
                msgId = R.string.ERR_INVALID_PARAMETER;
                break;

            case ERR_CANCELLED:
                break;

            case ERR_AUDIO_RECORD_FAILED:
                break;
            
            case ERR_NO_MORE_DATA:
                break;

            case ERR_DEVICE_DISCONNECTED:
                break;

            case ERR_DEVICE_DESTROYED:
                break;

            case ERR_DEVICE_DISABLED:
                break;

            case ERR_DEVICE_ALREADY_ADDED:
                break;

            case ERR_DEVICE_SN_NOT_MACTHED:
                break;

            case ERR_NETWORK_DISCONNECTED:
                break;

            case ERR_ACCOUNT_NAME_NOT_EXIST:
                break;

            case ERR_WRONG_PASSWORD:
                break;

            case ERR_ACCOUNT_ALREAD_LOGINED:
                break;

            case ERR_ALREADY_OPENED:
                break;

            case ERR_UNKNOWN:
            default:
                msgId = R.string.ERR_UNKNOWN;
                break;
            }

            message = context.getResources().getString( msgId );
        }

        if ( ERR_SUCCESS != error )
        {
            message += "(" + error + ")";
        }

        return message;
    }

    
}
