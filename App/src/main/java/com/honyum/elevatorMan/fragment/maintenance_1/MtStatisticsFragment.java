package com.honyum.elevatorMan.fragment.maintenance_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honyum.elevatorMan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MtStatisticsFragment extends Fragment {


    public MtStatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mt_statistics, container, false);
    }

}
