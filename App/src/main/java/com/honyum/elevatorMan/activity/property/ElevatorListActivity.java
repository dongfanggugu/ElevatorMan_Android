package com.honyum.elevatorMan.activity.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.Object;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.constant.Constant;
import com.honyum.elevatorMan.data.Building;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.data.UnitInfo;
import com.honyum.elevatorMan.net.ReportAlarmRequest;
import com.honyum.elevatorMan.net.ReportAlarmRequest.ReportAlarmReqBody;
import com.honyum.elevatorMan.net.ReportAlarmResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.utils.Utils;

public class ElevatorListActivity extends BaseFragmentActivity {
	
	//当前项目名称
	private String projectName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elevator_list);
		Intent intent = getIntent();
		initTitleBar();
		initView(intent);
	}

	/**
	 * 初始化view
	 */
	private void initView(Intent intent) {
		if (null == intent) {
			return;
		}
		projectName = intent.getStringExtra("name");
		List<Elevator> elevatorList = ((Building) intent
				.getSerializableExtra("building")).getElevatorList();

		classifyElevator(elevatorList);
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitleBar() {
		initTitleBar(getString(R.string.elevator_chosen), R.id.title_elevator,
                R.drawable.back_normal, backClickListener);
	}

	/**
	 * 点击标题栏后退按钮事件
	 */
	private OnClickListener backClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}

	};

	private void setListener(View view) {
		view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ElevatorListActivity.this, EasyAlarmActivity.class);
                Elevator elevator = (Elevator) v.getTag();
                intent.putExtra("project", projectName);
                intent.putExtra("id", elevator.getId());
                intent.putExtra("code", elevator.getLiftNum());
                startActivity(intent);
            }

        });
	}

	/**
	 * 将电梯按照单元号进行分类
	 * @param elevatorList
	 */
	private void classifyElevator(List<Elevator> elevatorList) {
	
		LinearLayout llElevator = (LinearLayout) findViewById(R.id.ll_elevator);

		List<String> keyList = getKeys(elevatorList);
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String pre, String next) {

                int thisNum = Utils.StringToInt(pre);
                int anotherNum = Utils.StringToInt(next);

                if (thisNum != 0 && anotherNum != 0) {
                    return thisNum - anotherNum;
                }
                return pre.compareTo(next);
            }
        });
		
		for (String key : keyList) {
			List<Elevator> unitList = new ArrayList<Elevator>();
			for (Elevator elevator : elevatorList) {
				if (key.equals(elevator.getUnitCode())) {
					unitList.add(elevator);
				}
			}
			View view = initUnitView(key + "单元", unitList);
			llElevator.addView(view);
		}
	}

	/**
	 * 根据分类初始化view
	 * @param unit
	 * @param elevatorList
	 * @return
	 */
	private View initUnitView(String unit, List<Elevator> elevatorList) {
		
		//计算view的宽度
		WindowManager manager = getWindowManager();
		int width = manager.getDefaultDisplay().getWidth();
		int viewWidth = width / 3;
		
		View view = View.inflate(this, R.layout.layout_elevator_item_new, null);
		((TextView) view.findViewById(R.id.tv_unit)).setText(unit);
		
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_item);
		
		View parent = null;
		LinearLayout ll = null;
		
		for (int i = 0; i < elevatorList.size(); i++) {
			if (0 == i % 3) {
				parent = View.inflate(this, R.layout.layout_elevator_linear, null);
				ll = (LinearLayout) parent.findViewById(R.id.ll_linear);
				layout.addView(parent);
			}
				
				View item = View.inflate(this, R.layout.layout_elevator_item, null);
				setListener(item);
				item.setTag(elevatorList.get(i));
				((TextView) item.findViewById(R.id.tv_self_num)).setText(elevatorList.get(i)
						.getNumber());
				
				((TextView) item.findViewById(R.id.tv_code)).setText(elevatorList.get(i)
						.getLiftNum());
				
				setElevatorIcon(i % 6, (ImageView) item.findViewById(R.id.img_lift));
				ll.addView(item);
				Utils.setLayoutWidth(item, viewWidth);
			
		}
		return view;
	}
	/**
	 * 获取分组的关键字，并进行排序
	 * @param elevatorList
	 * @return
	 */
	private List<String> getKeys(List<Elevator> elevatorList) {
		Map<String, String> keyMap = new HashMap<String, String>();
		for (Elevator elevator : elevatorList) {
			keyMap.put(elevator.getUnitCode(), "");
		}
		Set<String> keySet = keyMap.keySet();
		List<String> keyList = new ArrayList<String>();
		for (String key : keySet) {
			keyList.add(key);
		}
		Collections.sort(keyList);
		return keyList;
	}

    /**
     * 设置电梯图标
     * @param tag
     * @param imageView
     */
	private void setElevatorIcon(int tag, ImageView imageView) {
		switch (tag) {
		case 0:
			imageView.setImageResource(R.drawable.lift_1);
			break;
		case 1:
			imageView.setImageResource(R.drawable.lift_2);
			break;
		case 2:
			imageView.setImageResource(R.drawable.lift_3);
			break;
		case 3:
			imageView.setImageResource(R.drawable.lift_4);
			break;
		case 4:
			imageView.setImageResource(R.drawable.lift_5);
			break;
		case 5:
			imageView.setImageResource(R.drawable.lift_6);
			break;
		}
	}
}