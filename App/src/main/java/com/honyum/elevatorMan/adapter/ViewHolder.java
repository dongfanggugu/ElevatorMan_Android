package com.honyum.elevatorMan.adapter;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
    private SparseArray<View> array;
    private View view;

    public ViewHolder(View view){
        this.view=view;
        this.array=new SparseArray<>();
    }
    public void addView (int id){
        array.append(id,view.findViewById(id));
    }
    public View getView(int id){
        return (View)array.get(id);
    }

}