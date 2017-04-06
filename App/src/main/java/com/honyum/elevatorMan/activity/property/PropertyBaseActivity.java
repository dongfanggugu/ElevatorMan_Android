package com.honyum.elevatorMan.activity.property;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.SysActivityManager;
import com.honyum.elevatorMan.fragment.PropertySettingFragment;

public class PropertyBaseActivity extends BaseFragmentActivity {

	private long exitTime = 0;
	
	private boolean exitFlag = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initSlidingMenu(R.layout.layout_slide);
		loadSetting();
	}
	
	/**
	 * 加载侧边栏
	 */
	private void loadSetting() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.settings, new PropertySettingFragment());
		transaction.commit();
	}
	
	/**
	 * 设置退出按钮的标记
	 * @param exit
	 */
	protected void setExitFlag(boolean exit) {
		exitFlag = exit;
	}

	/**
	 * 控制侧边栏的显示
	 */
	
	protected OnClickListener menuClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getSlidingMenu().toggle();
		}
		
	};

	/**
	 * 处理后退事件，直接回到登录页面
	 */
	/**
	 * 处理后退事件，直接回到登录页面
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub		
		if (exitFlag) {
			popMsgAlertWithCancel(getString(R.string.exit_confirm), new IConfirmCallback() {
				@Override
				public void onConfirm() {
					PropertyBaseActivity.super.onBackPressed();
					SysActivityManager.getInstance().exit();
				}
			}, "否", "是", getString(R.string.exit_confirm));
//			new AlertDialog.Builder(this).setTitle(R.string.exit_confirm)
//			.setPositiveButton("是", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//					PropertyBaseActivity.super.onBackPressed();
//					SysActivityManager.getInstance().exit();
//				}
//
//			}).setNegativeButton("否", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//				}
//			}).show();
//
			
		} else {
			super.onBackPressed();
		}
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//     
//		if (keyCode == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			if (System.currentTimeMillis() - exitTime > 2000) {				
//				Toast.makeText(this, "再按一次退出应用", 0).show();
//				exitTime = System.currentTimeMillis();
//			} else {
//				SysActivityManager.getInstance().exit();				
//			}
//		}
//		return true;
//	}
	
}