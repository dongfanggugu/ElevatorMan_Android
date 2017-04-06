package com.honyum.elevatorMan.net.base;

import android.os.Message;
import android.util.Log;

import com.honyum.elevatorMan.base.BaseFragmentActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.DeflaterOutputStream;

/**
 * 处理网络接收数据的类
 */
public abstract class NetTask extends Thread {


    public final String RSP_CODE_OK = "0";
    public String SERVER_URL =
            "http://114.255.192.131:8080/cp_map_lossOfPart/servlet/MAPServlet";

    protected WeakReference<BaseFragmentActivity> mContexts = new WeakReference<BaseFragmentActivity>(
            null);

    private HttpURLConnection mCon;

    // 取消标志
    private volatile boolean mCanceled = false;
    private volatile boolean mNetDisable = false;

    // 是否是同步请求，同步请求时，显示进度条
    private boolean isSyncRequest = false;

    public RequestBean mRequest = null;
    public String mResult = null;

    private static int mTryCount = 3;

    /**
     * 传入请求Request
     *
     * @param request
     */
    public NetTask(String serverAdd, RequestBean request) {
        SERVER_URL = serverAdd;
        mRequest = request;
    }

    public boolean isSyncRequest() {
        return isSyncRequest;
    }

    public void setSyncRequest() {
        this.isSyncRequest = true;
    }

    abstract protected void onResponse(NetTask task, String result);

    protected void onFailed(NetTask task, String errorCode, String errorMsg) {
        //printErrorMsg(errorMsg);
        BaseFragmentActivity context = mContexts.get();
        Message msg = Message.obtain();
        msg.arg1 = Integer.parseInt(errorCode);
        msg.obj = errorMsg;
        //context.getHandler().sendMessage(msg);
        stopTask(msg);
    }

    /**
     * 获取context
     *
     * @return
     */
    public BaseFragmentActivity getBaseActivity() {
        Object obj = mContexts.get();
        if (obj == null) {
            return null;
        } else {
            return (BaseFragmentActivity) obj;
        }
    }

    /**
     * 设置context
     *
     * @param baseAct
     */
    public void setBaseActivity(BaseFragmentActivity baseAct) {
        mContexts = new WeakReference<BaseFragmentActivity>(baseAct);
    }

    @Override
    public void run() {

        String uploadJson = getJson(mRequest);

        System.out.println(uploadJson);
        if (uploadJson == null || uploadJson.equals("")) {
            cancelConn();
        }
        Log.i("NetTask", "uploadJson:" + uploadJson);

        // 获取所需上传对象的字节数组
        byte[] outData = uploadJson.getBytes();

        // 下发数据
        byte[] inData = null;
        // 网络连接重试次数
        int tryCount = 0;
        while (inData == null && !mCanceled) {
            try {
                // 创建连接，并发送数据及获取返回数据
                inData = connect(outData);

                //当出现返回码错误时，连接5次，无法连接，关闭连接
                tryCount++;
                if (mTryCount == tryCount) {
                    mCanceled = true;
                }


            } catch (Exception e) {
                Log.i("nettask", "exception occurred");
                if (!mCanceled) {
                    // 网络异常，休息一秒后继续尝试
                    tryCount++;
                    if (!NetWorkManager.checkNetConn(getBaseActivity())) {
                        mCanceled = true;
                        mNetDisable = true;
                    }
                    try {
                        Thread.sleep(3000);
                        if (mTryCount == tryCount) {
                            mCanceled = true;
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }

        if (mNetDisable) {
            Message msg = Message.obtain();
            msg.arg1 = -5;
            msg.obj = "网络连接失败，请检查网络设置";
            stopTask(msg);
            return;
        }

        if (mCanceled && tryCount == mTryCount) {
            Message msg = Message.obtain();
            msg.arg1 = -6;
            msg.obj = "服务器连接失败，请重新尝试";
            stopTask(msg);
            return;
        }

        if (stopTaskIfCanceled()) {
            return;
        }


        if (inData != null) {
            try {
                mResult = new String(inData, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (mResult != null) {
            Log.i("NetTask", "result:" + mResult);
            mContexts.get().getHandler().post(mHandleResult);
        } else {
            Log.i("NetTask", "result is null");
            stopTask(null);
        }
    }

    // 根据错误代码进行错误处理
    public void cancelConn() {
        mCanceled = true;
        if (mCon != null) {
            mCon.disconnect();
            mCon = null;
        }
    }

    public void stopTask(Message msg) {
        cancelConn();
        NetWorkManager netManager = NetWorkManager.getInstance();
        netManager.removeTask(this, msg);
    }

    private boolean stopTaskIfCanceled() {
        if (mCanceled) {
            stopTask(null);
        }
        return mCanceled;
    }

    // 连接服务器，
    @SuppressWarnings("resource")
    private byte[] connect(byte[] outData) throws Exception {
        if (outData == null) {
            throw new NullPointerException();
        }
        URL rui = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] data = null;
        try {
            rui = new URL(SERVER_URL);
            conn = (HttpURLConnection) rui.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Connection",
                    "application/x-www-form-urlencoded");
            // 设置超时时间
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", outData.length + "");
            mCon = conn;
            boolean compress = false;// 是否对传输的数据进行压缩
            if (compress) {
                BufferedReader in;
                try {
                    in = new BufferedReader(new InputStreamReader(
                            new ByteArrayInputStream(outData), "UTF-8"));
                    // 使用GZIPOutputStream包装OutputStream流，使其具体压缩特性，最后会生成test.txt.gz压缩包
                    // 并且里面有一个名为test.txt的文件
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    // 因IOS很难实现gzip压缩方式，故暂使用deflate压缩
                    BufferedOutputStream out = new BufferedOutputStream(
                            new DeflaterOutputStream(bos));
                    int c;
                    while ((c = in.read()) != -1) {
                        /*
						 * 注，这里是压缩一个字符文件， 前面是以字符流来读的 ，不能直接存入c ，
						 * 因为c已是Unicode
						 * 码，这样会丢掉信息的 (当然本身编码格式就不对 )，所以这里要以GBK来解后再存入 。
						 */
                        out.write(String.valueOf((char) c).getBytes("UTF-8"));
                    }
                    in.close();
                    out.close();
                    // 将数据base64处理
                    outData = bos.toByteArray();
                } catch (UnsupportedEncodingException e) {
                    Log.e("network", "network compress part error");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("network", "network compress part error");
                    e.printStackTrace();
                }
            }

            OutputStream os = conn.getOutputStream();

            if (os != null) {
                os.write(outData);
                os.flush();
                os.close();
            }
            int code = conn.getResponseCode();
            Log.i("info", "NET:code==" + code);

            // 判断HTTPCODE是否正常
            if (code == HttpURLConnection.HTTP_OK) {


                inputStream = conn.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                Log.i("data", "len:" + len);
                while ((len = inputStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                data = byteArrayOutputStream.toByteArray();

            } else {
                Log.w("NetTask", "code:" + code);
            }

        } catch (Exception e) {
            // 此处不处理，在doInBackground循环中处理IO异常
            throw e;
        } finally {
            // close 流和连接
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (inputStream != null)
                    inputStream.close();
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (Exception e) {

            }
        }
        return data;
    }


    /**
     * 将javabean转化为json字符串
     *
     * @param request
     * @return
     */
    private String getJson(RequestBean request) {
        try {
            String beanToJson = JacksonJsonUtil.beanToJson(request);
            return beanToJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    // 处理返回数据，在主线程中处理
    private Runnable mHandleResult = new Runnable() {

        @Override
        public void run() {
            if (mCanceled) {
                stopTask(null);
                return;
            }
            if (mResult != null) {
                try {
                    if (mCanceled) {
                        return;
                    }
                    try {
                        Response response = (Response) JacksonJsonUtil.jsonToBean(mResult,
                                Response.class);
                        if (RSP_CODE_OK.equals(response.getHead().getRspCode())) {
                            onResponse(NetTask.this, mResult);
                        } else {
                            onFailed(NetTask.this, response.getHead().getRspCode(),
                                    response.getHead().getRspMsg());
                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            stopTask(null);
        }

    };
}
