package com.honyum.elevatorMan.activity.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Network;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.net.SignUploadRequest;
import com.honyum.elevatorMan.net.SignUploadResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;

public class SecondActivity extends BaseFragmentActivity {

	private ImageView img;
	private Bitmap mBitmap;
	private Canvas canvas;
	private Paint paint;
	// 重置按钮
	private Button reset_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		img = (ImageView) findViewById(R.id.img);

		reset_btn = (Button) findViewById(R.id.reset_btn);
		reset_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				img.setImageBitmap(null);
				showImage();
			}
		});

        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(mBitmap);
            }
        });
		// 绘图
		showImage();

	}

	private void showImage() {

        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();

        int height = wm.getDefaultDisplay().getHeight();

        // 创建一张空白图片
		mBitmap = Bitmap.createBitmap(width, height - 200, Bitmap.Config.ARGB_8888);
		// 创建一张画布
		canvas = new Canvas(mBitmap);
		// 画布背景为白色
		canvas.drawColor(Color.WHITE);
		// 创建画笔
		paint = new Paint();
		// 画笔颜色为蓝色
		paint.setColor(Color.BLACK);
		// 宽度5个像素
		paint.setStrokeWidth(8);
		// 先将白色背景画上
		canvas.drawBitmap(mBitmap, new Matrix(), paint);
		img.setImageBitmap(mBitmap);

		img.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 获取手按下时的坐标
					startX = (int) event.getX();
					startY = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 获取手移动后的坐标
					int endX = (int) event.getX();
					int endY = (int) event.getY();
					// 在开始和结束坐标间画一条线
					canvas.drawLine(startX, startY, endX, endY, paint);
					// 刷新开始坐标
					startX = (int) event.getX();
					startY = (int) event.getY();
					img.setImageBitmap(mBitmap);
					break;
				}
				return true;
			}
		});

	}

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        /** 如果不设置canvas画布为白色，则生成透明 */
        c.drawColor(Color.WHITE);

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }


    private void uploadImage(Bitmap bitmap) {
        String base64Code = Utils.imgToStrByBase64(bitmap);

        SignUploadRequest request = new SignUploadRequest();
        SignUploadRequest.SignUploadReqBody body = new SignUploadRequest.SignUploadReqBody();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setAutograph(base64Code);

        request.setHead(head);
        request.setBody(body);

        String server = getConfig().getServer() + NetConstant.URL_UPLOAD_SIGN;
        NetTask task = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                SignUploadResponse response = SignUploadResponse.getResponse(result);
                getConfig().setSign(response.getBody().getAutograph());
                showToast("签名上传成功");
                finish();
            }
        };

        addTask(task);
    }

    private void saveImage(Bitmap bitmap) {

        String fileName = "IMG_"
                + System.currentTimeMillis() + ".jpg";
        File sdRoot = Environment.getExternalStorageDirectory();
        String dir = "/picture/";
        File mkDir = new File(sdRoot, dir);
        if (!mkDir.exists()) {
            mkDir.mkdirs();
        }

        File file = new File(mkDir, fileName);
        FileOutputStream fileOutStream = null;

        try {
            fileOutStream = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fileOutStream);  //把位图输出到指定的文件中
            fileOutStream.flush();
            fileOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
