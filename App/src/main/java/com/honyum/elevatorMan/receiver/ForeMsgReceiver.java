package com.honyum.elevatorMan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ForeMsgReceiver extends BroadcastReceiver {

    private IReceiveForeMsg mReceiveForeMsg = null;

    public ForeMsgReceiver(IReceiveForeMsg callback) {
        mReceiveForeMsg = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        String action = delForeAction(intent.getAction());
        intent.setAction(action);
        if (mReceiveForeMsg != null) {
            mReceiveForeMsg.onReceiveForeMsg(intent);
        }
    }







    /**
     * 取消内部消息的action
     * @param action
     * @return
     */
    private String delForeAction(String action) {
        if (!action.contains("_FORE")) {
            return action;
        }

        return action.replace("_FORE", "");
    }

    /**
     * 当页面在前台时收到消息时处理的接口
     */
    public interface IReceiveForeMsg {
        public void onReceiveForeMsg(Intent intent);
    }
}
