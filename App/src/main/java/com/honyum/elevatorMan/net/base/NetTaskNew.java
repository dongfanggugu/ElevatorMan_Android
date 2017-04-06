package com.honyum.elevatorMan.net.base;

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

import android.content.Context;
import android.util.Log;
import android.widget.TableRow;

/**
 * 处理网络接收数据的类
 * 
 */
public class NetTaskNew extends Thread {

	/**
	 * tags
	 */
	
	public String SERVER_URL = "";

	protected WeakReference<Context> mContext = new WeakReference<Context>(null);

	private HttpURLConnection mCon;

	// 取消标志
	private volatile boolean mCanceled = false;
	private volatile boolean mNetDisable = false;

	public RequestBean mRequest = null;
	
	public String mResult = null;

	
	
	/**
	 * 传入请求Request
	 * @param request
	 */
	public NetTaskNew(Context context, String serverAdd, RequestBean request) {
		SERVER_URL = serverAdd;
		mRequest = request;
		mContext = new WeakReference<Context>(context);
	}
	
	//abstract protected void onResponse(NetTask task, String result);
	protected void onReturn(NetTaskNew task, String result) {
		Log.i("newnettask", "result:" + result);
	}
	

	
	/**
	 * 获取context
	 * @return
	 */
	public Context getContext() {
		return mContext.get();
	}

	/**
	 * 设置context
	 * @param context
	 */
	public void setContext(Context context) {
		mContext = new WeakReference<Context>(context);
	}

	@Override
	public void run() {
		
		String uploadJson = getJson(mRequest);
		
		System.out.println(uploadJson);
		if (uploadJson == null || uploadJson.equals("")) {
			cancelConn();
		}
		Log.i("newnettask", "uploadJson:" + uploadJson);

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

                tryCount++;
                if (5 == tryCount) {
                    mCanceled = true;
                }

			} catch (Exception e) {
				if (!mCanceled) {
					// 网络异常，休息一秒后继续尝试
					tryCount++;
					if (!NetWorkManager.checkNetConn(mContext.get())) {
						mCanceled = true;
						mNetDisable = true;
					}
					try {
						Thread.sleep(3000);
						if (5 == tryCount) {
							mCanceled = true;
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

			}
		}

		if (mNetDisable) {
			stopTask();
			return;
		}

		if (mCanceled && tryCount == 5) {
			stopTask();
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
			onReturn(NetTaskNew.this, mResult);
			Log.i("newnettask", "result:" + mResult);
		} else {
			Log.i("newnettask", "result is null");
		}
		stopTask();
	}

	// 根据错误代码进行错误处理

	public void cancelConn() {
		mCanceled = true;
		if (mCon != null) {
			mCon.disconnect();
			mCon = null;
		}
	}

	/**
	 * 断开连接
	 */
	public void stopTask() {
		cancelConn();
	}

	private boolean stopTaskIfCanceled() {
		if (mCanceled) {
			stopTask();
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
					Log.e("newnettask", "network compress part error");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("newnettask", "network compress part error");
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
			Log.d("info", "NET:code==" + code);
			// 判断HTTPCODE是否正常
			if (code == HttpURLConnection.HTTP_OK) {
				inputStream = conn.getInputStream();
				byteArrayOutputStream = new ByteArrayOutputStream();
				// 一个个字符读取，2g手机网络一个TCP包较小
				byte[] buf = new byte[1024];
				int len = 0;
				Log.i("data", "len:" + len);
				while ((len = inputStream.read(buf)) != -1) {					
					byteArrayOutputStream.write(buf, 0, len);
				}
				data = byteArrayOutputStream.toByteArray();


			} else {
				Log.i("newnettask", "code:" + code);
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
}
