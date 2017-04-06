package com.honyum.elevatorMan.base;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;



public class SysActivityManager {
	private List<Activity> mList = new LinkedList<Activity>(); 
    private static SysActivityManager instance; 
 
    private SysActivityManager() {   
    } 
    public synchronized static SysActivityManager getInstance() { 
        if (null == instance) { 
            instance = new SysActivityManager(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
//            System.exit(0); 
        } 
    }
    
    public int getAliveCount() {
    	int count = 0;
    	for (Activity activity : mList) {
    		if (activity != null) {
    			count++;
    		}
    	}
    	return count;
    }
}