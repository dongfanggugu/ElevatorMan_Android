package com.honyum.elevatorMan.net.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;

import com.honyum.elevatorMan.activity.WelcomeActivity;
import com.honyum.elevatorMan.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 网络任务处理
 */
public class NetWorkManager {

    // 断网等待时间
    public static final int TYPE_DELETE_THE_TASK_0 = 0;
    public static final int TYPE_DELETE_SYNC_TASK_1 = 1;


    byte[] bLock = new byte[0];

    private static NetWorkManager manager = null;
    private BaseFragmentActivity bact = null;
    private Vector<NetTask> taskList = null;
    private ProgressDialog mProgressDialog;

    private NetWorkManager() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public synchronized static NetWorkManager getInstance() {
        if (manager == null) {
            manager = new NetWorkManager();
            manager.taskList = new Vector<NetTask>();
        }
        return manager;
    }

    /**
     * @param netTask 删除当前task
     */
    public void removeTask(NetTask netTask, Message msg) {
        if (taskList.isEmpty()) {
            return;
        }
//		int startIndex = -1;
//		int endIndex = -1;
        synchronized (bLock) {

//			switch (type) {
//			case TYPE_DELETE_THE_TASK_0:
//				if (netTask == null) {
//					return;
//				}
//				if (tasks.contains(netTask)) {
//					startIndex = tasks.indexOf(netTask);
//					endIndex = startIndex;
//				}
//				break;
//			case TYPE_DELETE_SYNC_TASK_1:
//				for (int i = 0; i < tasks.size(); i++) {
//					NetTask nt = tasks.get(i);
//					if (nt.isSyncRequest()) {
//						startIndex = i;
//						endIndex = startIndex;
//					}
//				}
//				break;
//			}
            if (null == netTask) {
                return;
            }

            int index = taskList.indexOf(netTask);

            if (-1 == index) {
                return;
            }

            Log.i("my_network", "hasOtherSyncTask:" + hasOtherSyncTask(index));
            if (!hasOtherSyncTask(index)) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }

            netTask.cancelConn();
            taskList.remove(netTask);
            Log.i("my_network", "remove size:" + taskList.size());
            netTask = null;


            // 这种移除的方式存在缺陷，应该用倒置的方式进行移除，才不会出现数组越界异常。
            //但在我们这个系统中，进行移除的时候不会出现问题，因为tasks任务数都为1.
//			for (int i = startIndex; i <= endIndex; i++) {
//				NetTask delNetTask = tasks.get(i);
//				if (delNetTask.isSyncRequest()) {
//					if (mProgressDialog != null && mProgressDialog.isShowing()) {
//						mProgressDialog.dismiss();
//						mProgressDialog = null;
//					}
//				}
//				Log.d("info", "removeTask----" + delNetTask.mContexts.get());
//                delNetTask.cancelConn();
//				tasks.remove(delNetTask);
//                delNetTask = null;
//				Log.d("info", "removeTask----tasks.size()----->" + tasks.size());
//			}
        }
        handleResult(msg);
    }

    /**
     * 获取当前任务列表中除去index，是否还有同步请求任务
     *
     * @param index
     * @return
     */
    private boolean hasOtherSyncTask(int index) {
        for (int i = 0; i < taskList.size(); i++) {
            if (i != index) {
                if (taskList.get(i).isSyncRequest()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 添加任务
     *
     * @param netTask
     * @param title
     * @param info
     * @return
     */
    public boolean addTast(NetTask netTask, String title, String info) {

        this.bact = netTask.getBaseActivity();
        if (checkNetConn(bact)) {

            if (netTask.isSyncRequest()) {

                if (null == mProgressDialog) {
                    mProgressDialog = new ProgressDialog(bact);
                    if (title != null) {
                        mProgressDialog.setTitle(title);
                    }


                    mProgressDialog.setMessage(info == null ? "正在进行网络连接，请稍后.." : info);
                    mProgressDialog.setCancelable(false);
                    //mProgressDialog.setOnCancelListener(onCancelListener);
                    mProgressDialog.setButton("取消", onClickListener);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.show();
                }
            }

            synchronized (bLock) {
                netTask.start();
                taskList.add(netTask);
                Log.i("my_network", "add size:" + taskList.size());
            }

            Log.d("info", "addTask----tasks.size()----->" + taskList.size());

            return true;
        } else {
            return false;
        }
    }

//	DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
//		@Override
//		public void onCancel(DialogInterface dialog) {
//			removeTask(null, null);
//			dialog.dismiss();
//			dialog = null;
//			mProgressDialog = null;
//		}
//	};

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //dialog.dismiss();

            //将同时请求的任务一起取消
            List<NetTask> tempList = new ArrayList<NetTask>();

            for (NetTask netTask : taskList) {
                if (netTask.isSyncRequest()) {
                    tempList.add(netTask);
                }
            }

            for (NetTask netTask : tempList) {
                removeTask(netTask, null);
            }
            tempList = null;

            //当是第一个页面时，退出页面
            if (bact instanceof WelcomeActivity) {
                bact.finish();
            }
        }
    };

    /**
     * 检测当前的网络状态
     *
     * @param context
     * @return
     */
    public static boolean checkNetConn(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()
                    && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理返回信息
     *
     * @param msg
     */
    public void handleResult(Message msg) {
        if (bact != null && msg != null) {
            bact.getHandler().sendMessage(msg);
        }
    }

}
