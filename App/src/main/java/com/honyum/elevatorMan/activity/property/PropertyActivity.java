package com.honyum.elevatorMan.activity.property;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.Project;
import com.honyum.elevatorMan.net.ProjectRequest;
import com.honyum.elevatorMan.net.ProjectResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.List;

public class PropertyActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        initTitleBar();
        requestProject();
    }

    /**
     * 初始化view
     */
    private void initView(List<Project> projectList) {
        if (null == projectList || 0 == projectList.size()) {
            showToast("当前物业没有关联项目");
            return;
        }
        ListView listView = (ListView) findViewById(R.id.list_project);
        listView.setAdapter(new MyAdapter(this, projectList));
        setListener(listView);
    }

    /**
     * listview 适配器
     *
     * @author chang
     */
    public class MyAdapter extends BaseAdapter {

        private List<Project> mProjectList;
        private Context mContext;

        public MyAdapter(Context context, List<Project> projectList) {
            mContext = context;
            mProjectList = projectList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mProjectList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mProjectList.get(position);
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
                convertView = View.inflate(mContext, R.layout.layout_project_item, null);
            }

            LinearLayout llProject = (LinearLayout) convertView.findViewById(R.id.ll_project);
            ImageView imgProject = (ImageView) convertView.findViewById(R.id.img_project);
            TextView tvProject = (TextView) convertView.findViewById(R.id.tv_project);

            setProjectTheme(position % 3, llProject, imgProject, tvProject);

            tvProject.setText(mProjectList.get(position).getName());
            convertView.setTag(mProjectList.get(position));
            return convertView;
        }


    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.main_page), R.id.title_property,
                R.drawable.back_normal, backClickListener);
    }

    /**
     * 设置listview的监听事件
     *
     * @param listView
     */
    private void setListener(ListView listView) {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Project project = (Project) view.getTag();
                Intent intent = new Intent(PropertyActivity.this, BuildingListActivity.class);
                intent.putExtra("project", project);
                startActivity(intent);
            }

        });
    }

    /**
     * 请求项目信息
     */
    private void requestProject() {
        NetTask netTask = new NetTask(getConfig().getServer() + NetConstant.URL_PROJECT_LIST,
                getProjectList()) {

            @Override
            protected void onResponse(NetTask task, String result) {
                // TODO Auto-generated method stub
                try {
                    ProjectResponse response = ProjectResponse.getProjectResponse(result);
                    initView(response.getBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        addTask(netTask);
        //initView(GenerateData.createProject());
    }

    /**
     * 请求项目信息
     *
     * @return
     */
    private RequestBean getProjectList() {
        ProjectRequest request = new ProjectRequest();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        request.setHead(head);

        return request;

    }


    /**
     * 设置项目的item风格
     *
     * @param tag
     * @param layout
     * @param imageView
     * @param textView
     */
    private void setProjectTheme(int tag, LinearLayout layout, ImageView imageView,
                                 TextView textView) {
        switch (tag) {
            case 0:
                int colorOri = getResources().getColor(R.color.color_ori_bg);
                layout.setBackgroundColor(colorOri);
                imageView.setImageResource(R.drawable.icon_project_1);
                textView.setTextColor(colorOri);
                break;
            case 1:
                int colorBlue = getResources().getColor(R.color.color_blue_bg);
                layout.setBackgroundColor(colorBlue);
                imageView.setImageResource(R.drawable.icon_project_2);
                textView.setTextColor(colorBlue);
                break;
            case 2:
                int colorGreen = getResources().getColor(R.color.color_green_bg);
                layout.setBackgroundColor(colorGreen);
                imageView.setImageResource(R.drawable.icon_project_3);
                textView.setTextColor(colorGreen);
                break;
        }
    }
}