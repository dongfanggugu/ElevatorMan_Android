package com.chorstar.data;

/**
 * Created by changhaozhang on 16/4/5.
 */
public class JKnowledgeInfo {

    private int mNativeKnowledge;

    public void init(int cknPtr) {
        mNativeKnowledge = native_init(cknPtr);
    }

    public void finalize() {
        try {
            finalizer(mNativeKnowledge);
            mNativeKnowledge = 0;
        } finally {
            try {
                super.finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public String getKnId() {
        return native_getKnId(mNativeKnowledge);
    }

    public String getBrand() {
        return native_getBrand(mNativeKnowledge);
    }


    private native void finalizer(int knowledge);
    public native int native_init(int CKnowledge);
    public native String native_getBrand(int knowledge);
    private native String native_getKnId(int knowledge);
}
