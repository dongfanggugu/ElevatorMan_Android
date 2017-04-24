package com.example.viewtest;

import util.DensityUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import Views.MySurfaceView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.provider.Settings;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends Activity {

	private Context context;
	private Button reset_btn;
	private MySurfaceView mview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;

		mview = (MySurfaceView) findViewById(R.id.MySurfaceView);


		reset_btn = (Button) findViewById(R.id.reset_btn);
		reset_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 清除
                //mview.reset();
            }
        });

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Bitmap bitmap = loadBitmapFromView(reset_btn);

                ImageView imageView = (ImageView) findViewById(R.id.iv_sign);
                imageView.setImageBitmap(mview.mBitmap);



                //saveImage(bitmap);
            }
        });

		// 第二种方式
		//  startActivity(new Intent(context, SecondActivity.class));

	}

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);



       // c.setBitmap(bmp);

        /** 如果不设置canvas画布为白色，则生成透明 */
        c.drawColor(Color.WHITE);

        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutStream);  //把位图输出到指定的文件中
            fileOutStream.flush();
            fileOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
