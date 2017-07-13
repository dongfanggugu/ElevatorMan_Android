package com.honyum.elevatorMan.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.util.common.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * 设置GridView的高度
     *
     * @param gridView
     * @param column
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView,
                                                        int column) {

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int size = listAdapter.getCount();
        int itemHeight = 0;

        for (int i = 0; i < size; i++) {
            View gridItem = listAdapter.getView(i, null, gridView);
            gridItem.measure(0, 0);
            totalHeight += gridItem.getMeasuredHeight();
            itemHeight = gridItem.getMeasuredHeight();
        }

        // totalHeight = totalHeight / column;
        if ((size % column) != 0) {
            totalHeight = totalHeight / column + itemHeight / column;
            size = size / column + 1;
        } else {
            totalHeight = totalHeight / column;
            size = size / column;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // params.height = (totalHeight
        // + (gridView.getHeight() * (size - 1)));
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }


    /**
     * 百度坐标系BD-09 转 火星坐标系GCJ-02
     *
     * @param bdLat 百度纬度
     * @param bdLng 百度经度
     * @return 火星坐标系经纬度
     */
    public static LatLng bd2Gcj(double bdLat, double bdLng) {
        double PI = 3.1415926535897932384626;
        double xPi = PI * 3000.0 / 180.0;
        double a = 6378245.0;
        double ee = 0.00669342162296594323;

        double x = bdLng - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * xPi);

        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * xPi);
        double gcjLng = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);

        return new LatLng(gcjLat, gcjLng);
    }


    /**
     * 设置view的宽度
     *
     * @param view
     * @param width
     */
    public static void setLayoutWidth(View view, int width) {

        Log.i("util", "type:" + view.getParent().getClass().getSimpleName());

        if (view.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view
                    .getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            // view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            // view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            // view.setX(x);
            view.requestLayout();
        }
    }

    /**
     * 判断应用是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("elevator:", "back");
                    return true;
                } else {
                    Log.i("elevator:", "fore");
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 应用是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static String getSdPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return null;
        }
    }

    /**
     * 获取版本名称，在manifest中配置的版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 从string转换为double，添加异常处理
     *
     * @param string
     * @return
     */
    public static double getDouble(String string) {
        double result = 0.0;
        try {
            result = Double.parseDouble(string);
        } catch (Exception e) {
            result = 0;
        }

        return result;
    }

    /**
     * 判断是否为合法的ipv4地址
     *
     * @param ip
     * @return
     */
    public static boolean isIPV4(String ip) {
        Pattern pattern = Pattern.compile(
                "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        return pattern.matcher(ip).matches();
    }

    /**
     * 利用反射机制，使dialog点击按钮时不再调用系统的关闭功能
     *
     * @param dialog
     * @param close
     */
    public static void setDialogCloseable(DialogInterface dialog, boolean close) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);

            //mShowing变量为false时，表示对话框已经关闭
            field.set(dialog, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取string每个字符的ASCII码的和
     *
     * @param string
     * @return
     */
    public static int getIntByStr(String string) {
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            result += ch;
        }
        return result;
    }

    /**
     * 将日期转换为字符串，格式为 2000-01-01
     */
    public static String dateToString(Date date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 将2000-01-01格式字符串转换为日期
     *
     * @param dateStr
     * @return
     */
    public static Date stringToDate(String dateStr) {

        Date date = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 获取两个日期的间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getIntervalDays(Date startDate, Date endDate) {

        if (null == startDate || null == endDate) {
            return Integer.MAX_VALUE;
        }

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        RemindUtils.initCalendarTime(start);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        RemindUtils.initCalendarTime(end);

        if (start.after(end)) {
            return -1;
        }

        int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        int year2 = end.get(Calendar.YEAR);

        while (start.get(Calendar.YEAR) != year2) {
            days += start.getActualMaximum(Calendar.DAY_OF_YEAR);
            start.add(Calendar.YEAR, 1);
        }

        return days;
    }

    /**
     * 将字符数据转换为int型
     *
     * @param intStr
     * @return
     */
    public static int StringToInt(String intStr) {
        int result = 0;

        try {
            result = Integer.parseInt(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据图片url获取图片
     */
    public static String getImage(String urlPath) throws Exception {

        String fileName = getFileNameByUrl(urlPath);
        String dir = getTempPath();

        String filePath = dir + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                saveBitmap(bitmap, dir, fileName);
            }
        }
        return filePath;
    }
    /**
     * 根据图片url获取图片
     */
    public static String getVideo(String urlPath) throws Exception {

        String fileName = getFileNameByUrl(urlPath);
        String dir = getTempPath();

        String filePath = dir + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                saveBitmap(bitmap, dir, fileName);
            }
        }
        return filePath;
    }

    /**
     * 保存图片文件
     *
     * @param bm
     * @param path
     * @param fileName
     */
    public static void saveBitmap(Bitmap bm, String path, String fileName) {
        saveBitmapWithQuality(bm, path, fileName, 100);
    }


    /**
     * 按照指定图片质量保存图片
     *
     * @param bm
     * @param path
     * @param fileName
     * @param quality
     */
    public static void saveBitmapWithQuality(Bitmap bm, String path, String fileName, int quality) {

        if (null == bm) {
            return;
        }

        if (quality < 0 || quality > 100) {
            return;
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //如果文件存在，删除文件
        File file = new File(path, fileName);
        if (file.exists()) {
            file.delete();
        }

        //写入文件
        try {
            FileOutputStream out = new FileOutputStream(new File(dir, fileName));
            if (fileName.toUpperCase().contains("PNG")) {
                bm.compress(Bitmap.CompressFormat.PNG, quality, out);
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据下发的url获取图片的文件名
     * 描述
     *
     * @param url
     * @return
     */
    public static String getFileNameByUrl(String url) {
        if (!url.contains("/")) {
            return url;
        }
        return url.substring(url.lastIndexOf("/"));
    }

    /**
     * 获取图片文件的临时存储目录
     * 描述
     *
     * @return
     */
    public static String getTempPath() {
        if (StringUtils.isEmpty(getSdPath())) {
            return "";
        }
        return getSdPath() + "/chorstar/temporary";
    }

    /**
     * 获取bitmap文件
     *
     * @param file
     * @return
     */
    public static Bitmap getImageFromFile(File file) {

        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 获取输入大小的照片
     *
     * @param path
     * @param height
     * @return
     */
    public static Bitmap getBitmapBySize(String path, int width, int height) {

        Bitmap bitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        //设置为true,只是获取图片的大小，不加载图片到内存，decoder返回null
        options.inJustDecodeBounds = true;

        //将图片的out...参数赋值给options
        BitmapFactory.decodeFile(path, options);

        //计算缩放比例
        options.inSampleSize = calculateScale(options, width, height);

        //设置为false之后，decoder返回bitmap
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(path, options);


        return bitmap;
    }


    /**
     * 获取图片缩放的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateScale(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int width = options.outWidth;
        int height = options.outHeight;

        int scale = 1;

        if (width > reqWidth || height > reqHeight) {
            int widthScale = Math.round((float) width / (float) reqWidth);
            int heightScale = Math.round((float) height / (float) reqHeight);

            scale = widthScale > heightScale ? widthScale : heightScale;
        }
        return scale;
    }

    /**
     * 判断两个字符串是否一致
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isSame(String str1, String str2) {
        if (0 == str1.compareTo(str2)) {
            return true;
        }
        return false;
    }

    /**
     * 将图片转换成BASE64
     *
     * @param bitmap
     * @return
     */
    public static String imgToStrByBase64(Bitmap bitmap) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 根据uri获取文件的路径
     *
     * @param uri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri uri) {

        String result = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

        if (cursor == null) {
            return result;
        }

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(index);

        cursor.close();

        return result;
    }

    /**
     * 将图片转换成BASE64
     *
     * @param imgPath
     * @return
     */
    public static String imgToStrByBase64(String imgPath) {
        if (StringUtils.isEmpty(imgPath)) {
            return "";
        }
        File file = new File(imgPath);
        if (!file.exists()) {
            return "";
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

        return imgToStrByBase64(bitmap);
    }

    /**
     * 验证身份证的合法性
     *
     * @param cardId
     * @return
     */
    public static boolean isLegalCardId(String cardId) {
        String result = getIDCardValidate(cardId);
        if (StringUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))" +
                "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])" +
                "|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])" +
                "|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])" +
                "|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])" +
                "|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     */
    private static String getIDCardValidate(String IDStr) {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";

        //身份证长度15位或者18位
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }

        //除最后一位都是数字
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }

        //出生年月是否有效
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }

        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }

        //验证地区码是否有效
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }

        //判断最后一位的值
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        return "";
    }

    /**
     * 清除目录中文件，保留目录结构
     *
     * @param file
     */
    public static void deleteFiles(File file) {

        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] childFiles = file.listFiles();
        if (null == childFiles || 0 == childFiles.length) {
            return;
        }

        for (File f : childFiles) {
            deleteFiles(f);
        }
    }

    /**
     * 清空指定的路径的文件，保留路径结构
     *
     * @param dirPath
     */
    public static void deleteFiles(String dirPath) {
        deleteFiles(new File(dirPath));
    }

    /**
     * 判断电话号码是否正确
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileNumber(String mobile) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 根据毫秒获取时间日期
     *
     * @param pattern
     * @param millionSeconds
     * @return
     */
    public static String getFormatedDateTime(String pattern, long millionSeconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(millionSeconds));
    }

    /**
     * 根据字符串的值返回long型
     *
     * @param str
     * @return
     */
    public static long getLongValue(String str) {
        long result = 0;
        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            result = 0;
        }

        return result;
    }

    /**
     * 拷贝assets目录下的文件
     *
     * @param context
     * @param sourceFile
     * @param fileFolder
     * @param fileName
     * @return
     */
    public static boolean copyAssetFile(Context context, String sourceFile, String fileFolder, String fileName) {
        AssetManager manager = context.getAssets();
        File fFile = new File(fileFolder);
        if (!fFile.exists()) {
            if (!fFile.mkdirs()) {
                Log.i("Utils", "mkdirs() failed");
                return false;
            }
            ;
        }
        try {
            InputStream input = manager.open(sourceFile);
            OutputStream output = new FileOutputStream(fileFolder + File.separator + fileName);
            byte[] buffer = new byte[1024];
            int length = input.read(buffer);

            while (length > 0) {
                output.write(buffer, 0, length);
                length = input.read(buffer);
            }

            output.flush();
            input.close();
            output.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 将2000-01-01格式字符串转换为日期
     *
     * @param dateStr
     * @return
     */
    public static long stringToMillions(String dateStr) {

        long millions = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse(dateStr);
            millions = date.getTime();

        } catch (Exception e) {

        }
        return millions;
    }

    public static int[] getViewHW(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        return new int[]{width, height};
    }


    /**
     * 获取时间时间戳
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return 时间戳
     */
    public static long getTimestamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * @param time 时间戳
     * @return 时间显示格式
     */
    public static String getTime(long time) {
        String result = "";

        long millis = System.currentTimeMillis();
        int day = (int) (millis / 1000 / 3600 / 24 - time / 1000 / 3600 / 24);

        switch (day) {
            case 0:
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                result = format.format(time);
                break;
            case 1:
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
                result = "昨天" + format2.format(time);
                break;
            case 2:
                SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");
                result = "前天" + format3.format(time);
                break;
            default:
                SimpleDateFormat format4 = new SimpleDateFormat("M月d日 HH:mm");
                result = format4.format(time);
                break;
        }

        return result;
    }

    public static int getInt(String string) {
        int result;

        try {
            result = Integer.parseInt(string);
        } catch (Exception e) {
            result = 0;
        }

        return result;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotatingPicture(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    /**
     * 查询通讯记录根据通话时长判断电话是否接听
     */
    public static boolean getCallLogState(Context context, String tel) {
        ContentResolver cr = context.getContentResolver();
        final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DURATION},
                CallLog.Calls.NUMBER + "=? and " + CallLog.Calls.TYPE + "= ?",
                new String[]{tel, CallLog.Calls.OUTGOING_TYPE + ""}, null);

        if (cursor == null) {
            return false;
        }

        cursor.moveToFirst();

        int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
        long durationTime = cursor.getLong(durationIndex);

        cursor.close();

        return durationTime > 0;
    }
}

