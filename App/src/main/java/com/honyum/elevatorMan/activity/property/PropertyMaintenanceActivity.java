package com.honyum.elevatorMan.activity.property;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragment;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.Building;
import com.honyum.elevatorMan.data.Elevator;
import com.honyum.elevatorMan.data.Project;
import com.honyum.elevatorMan.data.UnitInfo;
import com.honyum.elevatorMan.fragment.ProMainCompleteFragment;
import com.honyum.elevatorMan.fragment.ProMainHistoryFragment;
import com.honyum.elevatorMan.fragment.ProMainPlanFragment;
import com.honyum.elevatorMan.net.ProjectRequest;
import com.honyum.elevatorMan.net.ProjectResponse;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyMaintenanceActivity extends BaseFragmentActivity {

    private ImageView mFilter;

    private int mCurrentProject = 0;

    private int mCurrentBuilding = 0;

    private int mCurrentUnit = 0;

    private int mCurrentLift = 0;

    private IFilterLiftInfo mFilterCallback;

    private static int mCurrentPage = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_maintenance);
        mCurrentPage = getIntent().getIntExtra("page", 0);
        initTitleBar();
        requestProject();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        initTitleBar(getString(R.string.property_maintenance), R.id.title_property_main,
                R.drawable.back_normal, backClickListener);
        View titleView = findViewById(R.id.title_property_main);
        mFilter = (ImageView) titleView.findViewById(R.id.btn_query);
        mFilter.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化视图
     */
    private void initView(final List<Project> projectList) {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_main);
        final List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

        fragmentList.add(new ProMainPlanFragment());
        fragmentList.add(new ProMainCompleteFragment());
        fragmentList.add(new ProMainHistoryFragment());

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList));

        //初始化页面
        //final int currentPage = getIntent().getIntExtra("page", 0);
        viewPager.setCurrentItem(0, true);
        mFilterCallback = fragmentList.get(0);
        setSelectedBar(0);


        //维保计划
        findViewById(R.id.tv_plan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromValue(fragmentList, 0, mCurrentPage);
                mCurrentPage = 0;

                viewPager.setCurrentItem(0, true);
                setSelectedBar(0);
                mFilterCallback = fragmentList.get(0);
                initFilterState();
            }
        });

        //维保完成确认
        findViewById(R.id.tv_complete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setFromValue(fragmentList, 1, mCurrentPage);
                mCurrentPage = 1;

                viewPager.setCurrentItem(1, true);
                setSelectedBar(1);
                mFilterCallback = fragmentList.get(1);
                initFilterState();

            }
        });

        //维保历史
        findViewById(R.id.tv_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromValue(fragmentList, 2, mCurrentPage);
                mCurrentPage = 2;

                viewPager.setCurrentItem(2, true);
                setSelectedBar(2);
                mFilterCallback = fragmentList.get(2);
                initFilterState();
            }
        });

        /**
         * ViewPager添加事件监听
         */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.i("zhenhaotag", "on page scrolled:" + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("zhenhaotag", "onPageSelected:" + position);
                setSelectedBar(position);
                mFilterCallback = fragmentList.get(position);
                initFilterState();
                setFromValue(fragmentList, position, mCurrentPage);
                mCurrentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.i("zhenhaotag", "onPageScrollStateChanged:" + state);

            }
        });


        /**
         * 处理筛选
         */
        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(PropertyMaintenanceActivity.this, R.layout.layout_query, null);
                AlertDialog dialog = new AlertDialog.Builder(PropertyMaintenanceActivity.this)
                        .setView(view).create();
                initDialogView(view, dialog, projectList);
                dialog.show();
            }
        });


    }


    /**
     * 筛选视图
     *
     * @param projectList
     * @return
     */
    private void initDialogView(View dialogView, final AlertDialog dialog, final List<Project> projectList) {

        final TextView tvProject = (TextView) dialogView.findViewById(R.id.tv_project);
        final TextView tvBuilding = (TextView) dialogView.findViewById(R.id.tv_building);
        final TextView tvUnit = (TextView) dialogView.findViewById(R.id.tv_unit);
        final TextView tvLift = (TextView) dialogView.findViewById(R.id.tv_lift);


        //项目选择
        if (mCurrentProject != 0) {
            tvProject.setText(projectList.get(mCurrentProject - 1).getName());
        }
        tvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PropertyMaintenanceActivity.this).setTitle("项目选择")
                        .setItems(getProjectNameList(projectList), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (0 == which) {
                                    tvProject.setText("--ALL--");
                                } else {
                                    Project project = projectList.get(which - 1);
                                    tvProject.setText(project.getName());
                                }

                                tvBuilding.setText("--ALL--");
                                tvUnit.setText("--ALL--");
                                tvLift.setText("--ALL--");

                                mCurrentProject = which;
                                mCurrentBuilding = 0;
                                mCurrentUnit = 0;
                                mCurrentLift = 0;

                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });


        //楼栋选择，事件触发发生后，原有数据结构中楼栋的顺序将按照数字和字母表顺序排列好
        if (mCurrentBuilding != 0) {
            tvBuilding.setText(projectList.get(mCurrentProject - 1)
                    .getBuildingList().get(mCurrentBuilding - 1).getBuildingCode());
        }
        tvBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == mCurrentProject) {
                    showToast("请选择具体项目");
                    return;
                }
                new AlertDialog.Builder(PropertyMaintenanceActivity.this).setTitle("楼栋选择")
                        .setItems(getBuildingNameList(projectList.get(mCurrentProject - 1)), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (0 == which) {
                                    tvBuilding.setText("--ALL--");
                                } else {
                                    Project project = projectList.get(mCurrentProject - 1);
                                    if (project.getBuildingList() != null
                                            && project.getBuildingList().size() > 0) {
                                        tvBuilding.setText(project.getBuildingList().get(which - 1)
                                                .getBuildingCode());
                                    }
                                }

                                tvUnit.setText("--ALL--");
                                tvLift.setText("--ALL--");

                                mCurrentBuilding = which;
                                mCurrentUnit = 0;
                                mCurrentLift = 0;

                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        //单元选择，事件触发发生后，原有数据结构中单元的顺序将按照数字和字母表顺序排列好
        if (mCurrentUnit != 0) {
            tvUnit.setText(projectList.get(mCurrentProject - 1)
                    .getBuildingList().get(mCurrentBuilding - 1).
                            getUnitInfoList().get(mCurrentUnit - 1).getUnit() + "单元");
        }
        tvUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == mCurrentBuilding) {
                    showToast("请选择具体楼栋");
                    return;

                }
                new AlertDialog.Builder(PropertyMaintenanceActivity.this).setTitle("单元选择")
                        .setItems(getUnitNameList(projectList.get(mCurrentProject - 1)
                                        .getBuildingList().get(mCurrentBuilding - 1)),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (0 == which) {
                                            tvUnit.setText("--ALL--");
                                        } else {
                                            Building building = projectList.get(mCurrentProject - 1)
                                                    .getBuildingList().get(mCurrentBuilding - 1);

                                            if (building.getUnitInfoList() != null
                                                    && building.getUnitInfoList().size() > 0) {
                                                tvUnit.setText(building.getUnitInfoList().get(which - 1)
                                                        .getUnit());
                                            }
                                        }

                                        tvLift.setText("--ALL--");

                                        mCurrentUnit = which;
                                        mCurrentLift = 0;

                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });

        //电梯选择
        if (mCurrentLift != 0) {
            tvLift.setText(projectList.get(mCurrentProject - 1)
                    .getBuildingList().get(mCurrentBuilding - 1).
                            getUnitInfoList().get(mCurrentUnit - 1)
                    .getElevatorList().get(mCurrentLift - 1).getLiftNum());
        }
        tvLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (0 == mCurrentUnit) {
                    showToast("请选择具体单元");
                    return;
                }

                new AlertDialog.Builder(PropertyMaintenanceActivity.this).setTitle("电梯选择")
                        .setItems(getLiftNameList(projectList.get(mCurrentProject - 1)
                                        .getBuildingList().get(mCurrentBuilding - 1)
                                        .getUnitInfoList().get(mCurrentUnit - 1)),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (0 == which) {
                                            tvLift.setText("--ALL--");
                                        } else {
                                            UnitInfo unitInfo = projectList.get(mCurrentProject - 1)
                                                    .getBuildingList().get(mCurrentBuilding - 1)
                                                    .getUnitInfoList().get(mCurrentUnit - 1);

                                            if (unitInfo.getElevatorList() != null
                                                    && unitInfo.getElevatorList().size() > 0) {
                                                tvLift.setText(unitInfo.getElevatorList().get(which - 1)
                                                        .getLiftNum());
                                            }
                                        }

                                        mCurrentLift = which;
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });


        dialogView.findViewById(R.id.tv_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String project = "";
                if (mCurrentProject != 0) {
                    project = projectList.get(mCurrentProject - 1).getName();
                }

                String building = "";
                if (mCurrentProject != 0 && mCurrentBuilding != 0) {
                    building = projectList.get(mCurrentProject - 1)
                            .getBuildingList().get(mCurrentBuilding - 1).getBuildingCode();
                }

                String unit = "";
                if (mCurrentProject != 0 && mCurrentBuilding != 0 && mCurrentUnit != 0) {
                    unit = projectList.get(mCurrentProject - 1)
                            .getBuildingList().get(mCurrentBuilding - 1)
                            .getUnitInfoList().get(mCurrentUnit - 1).getUnit();
                }

                String lift = "";
                if (mCurrentProject != 0 && mCurrentBuilding != 0 && mCurrentUnit != 0
                        && mCurrentLift != 0) {
                    lift = projectList.get(mCurrentProject - 1)
                            .getBuildingList().get(mCurrentBuilding - 1)
                            .getUnitInfoList().get(mCurrentUnit - 1)
                            .getElevatorList().get(mCurrentLift - 1).getId();
                }
                mFilterCallback.onFilter(project, building, unit, lift);
                dialog.dismiss();
            }
        });
    }

    /**
     * ListView适配器
     */
    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        List<BaseFragment> mFragmentList;

        public MyViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
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
     * 获取项目名称数组
     *
     * @param projectList
     * @return
     */
    private String[] getProjectNameList(List<Project> projectList) {

        String[] strArrays = new String[projectList.size() + 1];
        for (int i = 1; i < projectList.size() + 1; i++) {
            strArrays[i] = projectList.get(i - 1).getName();
        }
        strArrays[0] = "--ALL--";
        return strArrays;
    }

    /**
     * 根据项目获取楼号数组
     *
     * @param project
     * @return
     */
    private String[] getBuildingNameList(Project project) {
        Collections.sort(project.getBuildingList());
        String[] strArray = new String[project.getBuildingList().size() + 1];
        for (int i = 1; i < project.getBuildingList().size() + 1; i++) {
            strArray[i] = project.getBuildingList().get(i - 1).getBuildingCode() + "号楼";
        }

        strArray[0] = "--ALL--";

        return strArray;
    }


    /**
     * 获取楼栋下单元号数组
     *
     * @param building
     * @return
     */
    private String[] getUnitNameList(Building building) {
        //将项目按照单元号分组
        classifyUnit(building);

        String[] strArray = new String[building.getUnitInfoList().size() + 1];
        for (int i = 1; i < building.getUnitInfoList().size() + 1; i++) {
            strArray[i] = building.getUnitInfoList().get(i - 1).getUnit() + "单元";
        }
        strArray[0] = "--ALL--";

        return strArray;
    }

    /**
     * 根据单元号获取电梯数组
     *
     * @param unitInfo
     * @return
     */
    private String[] getLiftNameList(UnitInfo unitInfo) {
        String[] strArray = new String[unitInfo.getElevatorList().size() + 1];
        for (int i = 1; i < unitInfo.getElevatorList().size() + 1; i++) {
            strArray[i] = unitInfo.getElevatorList().get(i - 1).getLiftNum();
        }
        strArray[0] = "--ALL--";
        return strArray;
    }


    /**
     * 获取分组的关键字，并进行排序
     *
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
     * 将楼栋电梯按照单元号进行分类
     *
     * @param building
     */
    private void classifyUnit(Building building) {

        List<String> keyList = getKeys(building.getElevatorList());
        List<UnitInfo> unitInfoList = new ArrayList<UnitInfo>();

        for (String key : keyList) {
            List<Elevator> unitList = new ArrayList<Elevator>();
            for (Elevator elevator : building.getElevatorList()) {
                if (key.equals(elevator.getUnitCode())) {
                    unitList.add(elevator);
                }
            }
            UnitInfo unitInfo = new UnitInfo();
            unitInfo.setUnit(key);
            unitInfo.setElevatorList(unitList);
            unitInfoList.add(unitInfo);
        }
        Collections.sort(unitInfoList);
        building.setUnitInfoList(unitInfoList);
    }

    /**
     * 电梯信息筛选接口
     */
    public interface IFilterLiftInfo {
        public void onFilter(String project, String building, String unit, String liftId);
    }

    /**
     * 设置选择的状态栏
     *
     * @param pageIndex
     */
    private void setSelectedBar(int pageIndex) {
        if (0 == pageIndex) {
            ((TextView) findViewById(R.id.tv_plan)).setTextColor(getResources().getColor(R.color.color_blue_bg));
            findViewById(R.id.tv_bottom1).setBackgroundColor(getResources().getColor(R.color.color_blue_bg));

            ((TextView) findViewById(R.id.tv_complete)).setTextColor(getResources().getColor(R.color.black_overlay));
            findViewById(R.id.tv_bottom2).setBackgroundColor(getResources().getColor(R.color.white));

            ((TextView) findViewById(R.id.tv_history)).setTextColor(getResources().getColor(R.color.black_overlay));
            findViewById(R.id.tv_bottom3).setBackgroundColor(getResources().getColor(R.color.white));

        } else if (1 == pageIndex) {
            ((TextView) findViewById(R.id.tv_complete)).setTextColor(getResources().getColor(R.color.color_blue_bg));
            findViewById(R.id.tv_bottom2).setBackgroundColor(getResources().getColor(R.color.color_blue_bg));

            ((TextView) findViewById(R.id.tv_plan)).setTextColor(getResources().getColor(R.color.black_overlay));
            findViewById(R.id.tv_bottom1).setBackgroundColor(getResources().getColor(R.color.white));

            ((TextView) findViewById(R.id.tv_history)).setTextColor(getResources().getColor(R.color.black_overlay));
            findViewById(R.id.tv_bottom3).setBackgroundColor(getResources().getColor(R.color.white));

        } else if (2 == pageIndex) {
            ((TextView) findViewById(R.id.tv_history)).setTextColor(getResources().getColor(R.color.color_blue_bg));
            findViewById(R.id.tv_bottom3).setBackgroundColor(getResources().getColor(R.color.color_blue_bg));

            ((TextView) findViewById(R.id.tv_complete)).setTextColor(getResources().getColor(R.color.black_overlay));
            findViewById(R.id.tv_bottom2).setBackgroundColor(getResources().getColor(R.color.white));

            ((TextView) findViewById(R.id.tv_plan)).setTextColor(getResources().getColor(R.color.black_overlay));
            findViewById(R.id.tv_bottom1).setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * 初始化筛选状态
     */
    private void initFilterState() {
        mCurrentProject = 0;

        mCurrentBuilding = 0;

        mCurrentUnit = 0;

        mCurrentLift = 0;
    }

    /**
     * 设置当前显示的fragment的上一个显示的fragment的index，用来解决viewpager + fragment时，fragment预加载
     * 导致数据加载和预期有差异的问题。
     *
     * @param fragmentList
     * @param index
     * @param pre
     */
    private void setFromValue(List<BaseFragment> fragmentList, int index, int pre) {

        if (null == fragmentList || 0 == fragmentList.size()) {
            return;
        }
        if (index > fragmentList.size() - 1) {
            return;
        }
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == index) {
                fragmentList.get(i).mFrom = pre;
            } else {
                fragmentList.get(i).mFrom = -1;
            }
        }
    }

}
