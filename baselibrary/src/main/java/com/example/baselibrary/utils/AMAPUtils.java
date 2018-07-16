package com.example.baselibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * 地图帮助类
 */
public class AMAPUtils {
    private static AMAPUtils manager = null;

    private String TAG = "AMAPUtiles";

    public synchronized static AMAPUtils getInstance() {
        if (null == manager) {
            manager = new AMAPUtils();
        }
        return manager;
    }

    private AMAPUtils() {

    }

    /**
     * 放大大小
     *
     * @param distance
     * @return
     */
    public int zoomSize(double distance) {
        if (distance <= 2) {
            return 14;//15
        } else if (distance > 2 && distance <= 5) {
            return 14;
        } else if (distance > 5 && distance <= 10) {
            return 13;
        } else if (distance > 10 && distance <= 20) {
            return 13;
        } else if (distance > 20 && distance <= 30) {
            return 12;
        } else if (distance > 30 && distance <= 50) {
            return 10;
        } else if (distance > 50 && distance <= 100) {
            return 10;
        } else if (distance > 100 && distance <= 200) {
            return 9;
        } else if (distance > 200 && distance <= 500) {
            return 9;
        } else if (distance > 500) {
            return 8;
        }
        return 15;
    }

    /***
     * 是否安装百度地图
     * @return
     */
    public boolean isHaveBaiduMap() {
        try {
            if (!new File("/data/data/" + "com.baidu.BaiduMap").exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, " : " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 是否安装高德地图
     *
     * @return
     */
    public boolean isHaveGaodeMap() {
        try {
            if (!new File("/data/data/" + "com.autonavi.minimap").exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, " : " + e.getMessage());
            return false;
        }
        return true;
    }

    /***
     * 是否安装腾讯地图
     * @return
     */
    public boolean isHaveTencentMap() {
        try {
            if (!new File("/data/data/" + "com.tencent.map").exists()) {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, " : " + e.getMessage());
            return false;
        }
        return true;
    }

    /***
     * 百度地图[传参过来的值可以判断为百度]
     * http://lbsyun.baidu.com/index.php?title=uri/api/android
     */
    public void openBaiduMap(Context context, String dqAddress, String gotoAddress, String gotoLatitude, String gotoLongitude) {
        try {
            if (context != null && !TextUtils.isEmpty(dqAddress) && !TextUtils.isEmpty(gotoAddress) && !TextUtils.isEmpty(gotoLatitude) && !TextUtils.isEmpty(gotoLongitude)) {
                double[] gotoLang=AMAPUtils.getInstance().gaoDeToBaidu(Double.parseDouble(gotoLatitude), Double.parseDouble(gotoLongitude));
                gotoLatitude= String.valueOf(gotoLang[0]);gotoLongitude= String.valueOf(gotoLang[1]);
                String url_map = "intent://map/direction?" +
                        //"origin=latlng:" + dqLatitude + "," + dqLongitude +   //起点  此处不传值默认选择当前位置如果传参则提示参数错误
                        "destination=latlng:" + gotoLatitude + "," + gotoLongitude + "|name:" + gotoAddress +
                        "&mode=driving&" +          //导航路线方式
                        "region=" + dqAddress +           //
                        "&src=" + dqAddress + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                Intent intent = Intent.getIntent(url_map);
                context.startActivity(intent); // 启动调用
            } else {
                Log.d(TAG, "openBaiduMap is failed ,failed msg is have params is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, " : " + e.getMessage());
        }
    }

    /**
     * openGaoDeMap void 调用高德地图apk
     * http://lbs.amap.com/api/uri-api/guide/travel/route
     */
    public void openGaoDeMap(Context context, String dqAddress, String gotoAddress, String gotoLatitude, String gotoLongitude) {
        try {
            if (context != null && !TextUtils.isEmpty(gotoAddress) && !TextUtils.isEmpty(gotoLatitude) && !TextUtils.isEmpty(gotoLongitude)) {
                //double[] gotoLang=AMAPUtils.getInstance().bdToGaoDe(Double.parseDouble(gotoLatitude),Double.parseDouble(gotoLongitude));
                //gotoLatitude=String.valueOf(gotoLang[0]);gotoLongitude=String.valueOf(gotoLang[1]);
                String url = "androidamap://navi?sourceApplication=" + dqAddress + "&poiname=" + gotoAddress + "&lat=" + gotoLatitude + "&lon=" + gotoLongitude + "&dev=0&style=1";
                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(url));
                intent.setPackage("com.autonavi.minimap");
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, " : " + e.getMessage());
        }
    }

    /**
     * 百度转高德
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 高德地图转百度[暂时不用]
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 打开腾讯地图
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     *
     * @param context
     * @param dqAddress
     * @param gotoAddress
     * @param gotoLatitude
     * @param gotoLongitude
     * 驾车：type=drive，policy有以下取值
    0：较快捷
    1：无高速
    2：距离
    policy的取值缺省为0
     * &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     */
    public void openTencentMap(Context context, String dqAddress, String gotoAddress, String gotoLatitude, String gotoLongitude) {
        try {
            if (context != null && !TextUtils.isEmpty(dqAddress) && !TextUtils.isEmpty(gotoAddress) && !TextUtils.isEmpty(gotoLatitude) && !TextUtils.isEmpty(gotoLongitude)) {
                //double[] gotoLang=AMAPUtils.getInstance().bdToGaoDe(Double.parseDouble(gotoLatitude),Double.parseDouble(gotoLongitude));
                //gotoLatitude=String.valueOf(gotoLang[0]);gotoLongitude=String.valueOf(gotoLang[1]);
                String url1 = "qqmap://map/routeplan?type=drive&to=" + gotoAddress + "&tocoord=" + gotoLatitude+ "," +gotoLongitude  + "&policy=2&referer=myapp";
                Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(url1));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, " : " + e.getMessage());
        }
    }
}
