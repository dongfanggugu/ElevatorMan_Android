package com.honyum.elevatorMan.utils;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.honyum.elevatorMan.data.IconInfo;
import com.honyum.elevatorMan.data.IconInfosTag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Star on 2017/12/5.
 */

public class IconsParser {

    private static final String TAG = "IconsParser";
    private XmlPullParser xmlParser;
    private WeakReference<Context> mContext;
    private ArrayList<IconInfo> infos;
    private IconInfo mInfo;
    //private IconInfos mIconInfos;  //
    private IconInfosTag tags;
    private List<IconInfosTag> tagList;


    public IconsParser(Context context) {
        mContext = new WeakReference<>(context);
    }

    public List<IconInfosTag> getIconsInfo(InputStream is) {
        try {
            xmlParser = Xml.newPullParser();
            ///初始化xmlPull解析器
            xmlParser.setInput(is, "utf-8");
            int event = xmlParser.getEventType();   //先获取当前解析器光标在哪
            while (event != XmlPullParser.END_DOCUMENT) {    //如果还没到文档的结束标志，那么就继续往下处理
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml解析开始");
                        break;
                    case XmlPullParser.START_TAG:
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        Log.d(TAG, "当前标签是：" + xmlParser.getName());
                        if (xmlParser.getName().equals("array")) {
                           // mIconInfos = new ArrayList<>();
                            tagList = new ArrayList<>();
                        } else if (xmlParser.getName().equals("icons")) {
                            tags = new IconInfosTag();
                            tags.setTag(xmlParser.getAttributeValue(0));
                            infos = new ArrayList<>();
                        } else if (xmlParser.getName().equals("icon")) {
                            mInfo = new IconInfo();
                        } else if (xmlParser.getName().equals("id")) {
                            mInfo.setId(xmlParser.nextText());
                        } else if (xmlParser.getName().equals("name")) {
                            mInfo.setName(xmlParser.nextText());
                        } else if (xmlParser.getName().equals("image")) {
                            if (mContext != null)
                                mInfo.setImage(Utils.getResource(xmlParser.nextText(), mContext.get()));
                        } else if (xmlParser.getName().equals("action")) {
                            mInfo.setAction(xmlParser.nextText());
                        }

                        break;
                    case XmlPullParser.TEXT:
                        Log.d(TAG, "Text:" + xmlParser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        if (xmlParser.getName().equals("array")) {
                        } else if (xmlParser.getName().equals("icons")) {
                            tags.setIconsInfo(infos);
                            tagList.add(tags);
                        } else if (xmlParser.getName().equals("icon")) {
                            infos.add(mInfo);
                        }
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();   //将当前解析器光标往下一步移
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tagList;


    }
}
