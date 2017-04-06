package com.honyum.elevatorMan.activity.worker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chorstar.jni.ChorstarJNI;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.knowledge.TitleListActivity;
import com.honyum.elevatorMan.base.Config;
import com.honyum.elevatorMan.utils.SQLiteUtils;
import com.honyum.elevatorMan.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class LiftKnowledgeActivity extends WorkerBaseActivity {


    private ImageView mQuery;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift_knowledge);
        copyKnowledge();
        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        initTitleBar(getString(R.string.lift_knowledge), R.id.title_knowlage,
                R.drawable.back_normal, backClickListener);
        View titleView = findViewById(R.id.title_knowlage);
        mQuery = (ImageView) titleView.findViewById(R.id.btn_query);
        mQuery.setImageResource(R.drawable.icon_query);
        mQuery.setVisibility(View.VISIBLE);
        mQuery.setOnClickListener(queryListener);
    }

    private void initView() {
        findViewById(R.id.ll_qa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiftKnowledgeActivity.this, TitleListActivity.class);
                intent.putExtra("type", "常见问题");
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_op).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiftKnowledgeActivity.this, TitleListActivity.class);
                intent.putExtra("type", "操作手册");
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_fault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiftKnowledgeActivity.this, TitleListActivity.class);
                intent.putExtra("type", "故障对照");
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_law).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiftKnowledgeActivity.this, TitleListActivity.class);
                intent.putExtra("type", "安全法规");
                startActivity(intent);
            }
        });

        //更新知识库
        ProgressDialog progress = ProgressDialog.show(this, "wait...", "update knowledge...");
        Thread uThread = new Thread() {
            @Override
            public void run() {
                Config config = getConfig();
                ChorstarJNI.requestKnowledgeUpdate(config.getServer() + "/", config.getToken(), config.getUserId());
                config = null;
            }
        };

        try {
            uThread.start();
            uThread.join();
            progress.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 点击搜索
     */
    private View.OnClickListener queryListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            View view = View.inflate(LiftKnowledgeActivity.this, R.layout.layout_dialog_search, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(LiftKnowledgeActivity.this);

            builder.setView(view);
            AlertDialog dialog = builder.create();

            List<String> brandList = ChorstarJNI.getBrandList();
            initDialogView(view, dialog, brandList);

            Window dialogWindow = dialog.getWindow();
//
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//
//            lp.width = 30;
//            lp.height = 30;
//
//            dialogWindow.setGravity(Gravity.LEFT|Gravity.TOP);
//
//            dialogWindow.setAttributes(lp);

            dialog.show();
        }
    };


    /**
     * 初始化弹出框
     * @param view
     * @param dialog
     * @param brandList
     */
    private void initDialogView(View view, final Dialog dialog, final List<String> brandList) {
        final TextView tvBrand = (TextView) view.findViewById(R.id.tv_brand);
        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LiftKnowledgeActivity.this);
                builder.setTitle("品牌选择");
                String[] brandArray = brandList.toArray(new String[brandList.size()]);
                builder.setItems(brandArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tvBrand.setText(brandList.get(which));
                    }
                });
                builder.create().show();
            }
        });

        final EditText etKeywords = (EditText) view.findViewById(R.id.et_keywords);

        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String brand = tvBrand.getText().toString();
                String keywords = etKeywords.getText().toString();

                Intent intent = new Intent(LiftKnowledgeActivity.this, TitleListActivity.class);
                intent.putExtra("type", "搜索结果");
                intent.putExtra("brand", brand);
                intent.putExtra("keywords", keywords);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 拷贝知识库数据库到应用安装路径下
     */
    private void copyKnowledge() {
        String knowledge =  "knowledge.db";
        String dbPath = "/data/data/com.honyum.elevatorMan/databases";
        File knFile = new File(dbPath + File.separator + knowledge);
        //if (!knFile.exists()) {
        Utils.copyAssetFile(this, knowledge, dbPath, knowledge);
        //}
    }
}
