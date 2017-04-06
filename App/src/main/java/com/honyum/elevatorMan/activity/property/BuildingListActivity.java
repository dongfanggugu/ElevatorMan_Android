package com.honyum.elevatorMan.activity.property;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.Building;
import com.honyum.elevatorMan.data.Project;

import java.util.Collections;
import java.util.List;

public class BuildingListActivity extends BaseFragmentActivity {
	
	//当前项目的纬度
	//private String latitude;
	
	//当前项目的经度
	//private String longtitude;
	
	//当前项目名称
	private String projectName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_list);
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
		Project project = (Project) intent.getSerializableExtra("project");
		List<Building> buildingList = project.getBuildingList();

        //按照楼号进行排序
        Collections.sort(buildingList);

		projectName = project.getName();

		GridView listView = (GridView) findViewById(R.id.list_building);
		listView.setAdapter(new MyAdapter(this, buildingList));
		setListener(listView);
	}

	/**
	 * ListView 适配器
	 * 
	 * @author chang
	 * 
	 */
	public class MyAdapter extends BaseAdapter {

		private List<Building> mBuildingList;
		private Context mContext;

		public MyAdapter(Context context, List<Building> buildingList) {
			mContext = context;
			mBuildingList = buildingList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mBuildingList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mBuildingList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == convertView) {
				convertView = View.inflate(mContext,
						R.layout.layout_building_item, null);
			}

			TextView tvBuilding = (TextView) convertView
					.findViewById(R.id.tv_building);
			tvBuilding.setText(mBuildingList.get(position).getBuildingCode() + "号楼");
			
			ImageView imgBuilding = (ImageView) convertView.findViewById(R.id.img_building);
			
			setBuildingIcon(position % 6, imgBuilding);
			
			convertView.setTag(mBuildingList.get(position));
			return convertView;
		}

	}

	/**
	 * 初始化标题栏
	 */
	private void initTitleBar() {
		initTitleBar(getString(R.string.building_chosen), R.id.title_building,
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
	
	/**
	 * 设置ListView的监听事件
	 * @param listView
	 */
    private void setListener(GridView listView) {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Building building = (Building) view.getTag();
                Intent intent = new Intent(BuildingListActivity.this, ElevatorListActivity.class);
                intent.putExtra("building", building);
                intent.putExtra("name", projectName);
                startActivity(intent);
            }

        });
    }

    /**
	 * 设置楼栋的icon
	 * @param tag
	 * @param imageView
	 */
	private void setBuildingIcon(int tag, ImageView imageView) {
		switch (tag) {
		case 0:
			imageView.setImageResource(R.drawable.building_1);
			break;
		case 1:
			imageView.setImageResource(R.drawable.building_2);
			break;
		case 2:
			imageView.setImageResource(R.drawable.building_3);
			break;
		case 3:
			imageView.setImageResource(R.drawable.building_4);
			break;
		case 4:
			imageView.setImageResource(R.drawable.building_5);
			break;
		case 5:
			imageView.setImageResource(R.drawable.building_6);
			break;
			
		}
	}

}