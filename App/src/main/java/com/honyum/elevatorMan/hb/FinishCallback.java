package com.honyum.elevatorMan.hb;

/**
 * 异步接口完成回调
 */
public interface FinishCallback
{
    /**
     * 响应异步接口完成。
     * @param error
     *      错误代码，参考{@link ManagerError#getErrorMessage}。
     * @param tag
     *      自定义标签。
     */
    void onFinish(int error, Object tag);
}
