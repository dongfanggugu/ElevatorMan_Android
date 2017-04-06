package com.honyum.elevatorMan.fragment.maintenance_1;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.view.SlidingDrawer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MtTodayFragment extends Fragment {


    private View mView;

    private BaiduMap baiduMap;

    private BaseFragmentActivity mAcitvity;

    public MtTodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAcitvity = (BaseFragmentActivity) getActivity();

        SDKInitializer.initialize(mAcitvity.getApplicationContext());

        mView = inflater.inflate(R.layout.fragment_mt_today, container, false);

        initTitleBar();

        initView();

        return mView;
    }

    private void initTitleBar() {
        View titleView = mView.findViewById(R.id.title);
        ((TextView) titleView.findViewById(R.id.tv_title)).setText("当日维保概况");
    }

    private void initView() {

        MapView mapView = (MapView) mView.findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        final SlidingDrawer sd = (SlidingDrawer) mView.findViewById(R.id.slidingDrawer);
        sd.open();
        final TextView tab = (TextView) mView.findViewById(R.id.tv_tab);
        final EditText etSearch = (EditText) mView.findViewById(R.id.et_search);


        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (sd.isOpened()) {
                    sd.animateClose();
                }
                hideKeyboard(etSearch);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });


        sd.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onPreScrollStarted() {
                hideKeyboard(etSearch);
            }

            @Override
            public void onScrollStarted() {
            }

            @Override
            public void onScroll(boolean willBackward) {
            }

            @Override
            public void onScrollEnded() {
                if (sd.isOpened()) {
                    tab.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_top));
                } else {
                    tab.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_down));
                }
            }
        });

        mView.findViewById(R.id.handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd.toggle();
            }
        });

    }

    /**
     * 隐藏软键盘
     *
     * @param et EditText控件
     */
    private void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) mAcitvity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

}
