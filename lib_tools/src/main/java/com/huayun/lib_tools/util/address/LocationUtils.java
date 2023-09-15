package com.huayun.lib_tools.util.address;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.huayun.lib_tools.util.PermissionsUtil;
import com.huayun.lib_tools.util.StringUtil;
import com.huayun.lib_tools.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 地址/地图
 */
public class LocationUtils {

    private volatile static LocationUtils uniqueInstance;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context mContext;

    private LocationUtils(Context context) {
        mContext = context;
        getLocation();
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtils(context);
                }
            }
        }
        return uniqueInstance;
    }

    //获取地址信息
    private void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            LogUtil.xLoge("如果是网络定位");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            LogUtil.xLoge("如果是GPS定位");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            LogUtil.xLoge("没有可用的位置提供器");
            return;
        }
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            setLocation(location);
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    private void setLocation(Location location) {
        this.location = location;
        String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
    }

    //获取经纬度
    public Location showLocation() {
        return location;
    }

    // 移除定位监听
    public void removeLocationUpdatesListener() {
        // 需要检查权限,否则编译不过
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null) {
            uniqueInstance = null;
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        /**
         * 当某个位置提供者的状态发生改变时
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        /**
         * 某个设备打开时
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 某个设备关闭时
         */
        @Override
        public void onProviderDisabled(String provider) {

        }

        /**
         * 手机位置发生变动
         */
        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();//精确度
            setLocation(location);
            if (locationListenerCall != null) {
                locationListenerCall.getLocation(location);
            }
        }
    };

    public LocationListenerCall locationListenerCall;

    public void setLocationListenerCall(LocationListenerCall locationListenerCall) {
        this.locationListenerCall = locationListenerCall;
    }

    public interface LocationListenerCall {
        void getLocation(Location location);
    }

    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 根据两地经纬度计算距离
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return 返回单位是米
     */
    public double getDistance(double longitude1, double latitude1,
                              double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;

        s = Math.round(s * 1000d) / 1000d;
        return s;
    }

    /**
     * 计算弧度
     *
     * @param d
     * @return
     */
    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据经纬度获取地址名
     *
     * @param lnt
     * @param lat
     * @return
     */
    public List<String> getAddress(Context context, double lnt, double lat) {
        Geocoder geocoder = new Geocoder(context);
        List<String> addressDes = new ArrayList<>();
        try {
            //根据经纬度获取地理位置信息---这里会获取最近的几组地址信息，具体几组由最后一个参数决定
            List<Address> addresses = geocoder.getFromLocation(lat, lnt, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String provinceName = "";//省
                String cityName = "";//市
                String areaName = "";//区
                String addressInfo;//详细地址
                if (address.getAdminArea() != null && !StringUtil.isBlank(address.getAdminArea())) {
                    provinceName = address.getAdminArea();
                }
                if (address.getLocality() != null && !StringUtil.isBlank(address.getLocality())) {
                    cityName = address.getLocality();
                }
                if (address.getSubLocality() != null && !StringUtil.isBlank(address.getSubLocality())) {
                    areaName = address.getSubLocality();
                }
                addressInfo = address.getFeatureName() + address.getThoroughfare() + "";

                addressDes.add(provinceName);
                addressDes.add(cityName);
                addressDes.add(areaName);
                addressDes.add(addressInfo);
//                stringBuilder.append(address.getCountryName()).append("--");//国家
//                stringBuilder.append(address.getAdminArea()).append("--");//省份
//                stringBuilder.append(address.getLocality()).append("--");//市
//                stringBuilder.append(address.getSubLocality()).append("--");//区
//                stringBuilder.append(address.getFeatureName()).append("--");//周边地址
//                stringBuilder.append(address.getCountryCode()).append("--");//国家编码
//                stringBuilder.append(address.getThoroughfare()).append("--");//道路
//                L.e_log("地址信息--->" + stringBuilder);
            }
        } catch (Exception e) {
            LogUtil.xLoge("获取经纬度地址异常");
            e.printStackTrace();
        }
        return addressDes;
    }


    /**
     * 反地理编码--根据地名获取经纬度
     *
     * @param cityName
     */
    public static void getLatAndLng(Context context, String cityName, LatAndLngCall call) {
        Geocoder geocoder = new Geocoder(context, Locale.CHINA);
        try {
            List addressList = geocoder.getFromLocationName(cityName, 5);
            if (addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                call.latAndLng(address.getLongitude(), address.getLatitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查权限
     *
     * @param context
     * @param callback
     */
    public static void checkAddressPermissions(Context context, OnPermissionCallback callback) {
        PermissionsUtil.requestPermissions(context, callback, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 检测定位权限
     *
     * @return
     */
    public static boolean checkGpsService(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean isGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPS && !isNetwork) {
            return false;
        }
        return true;
    }

    /**
     * @param context
     * @param requestCode
     */
    public static void openPhoneSettingToGps(Activity context, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开地图导航  高德->百度->腾讯->浏览器
     *
     * @param context
     * @param curLon       当前位置经度
     * @param curLat       当前位置维度
     * @param desLat       目的地维度
     * @param desLon       目的地经度
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @param address      详细地址
     * @param appName      App名称
     */
    public static void openPhoneAppMap(Context context, double curLon, double curLat, double desLon, double desLat,
                                       String provinceName, String cityName, String areaName, String address, String appName) {
        if (checkMapAppsIsExist(context, "com.autonavi.minimap")) {
            openGaoDe(context, curLon, curLat, desLon, desLat, provinceName, cityName, areaName, address);
        } else if (checkMapAppsIsExist(context, "com.baidu.BaiduMap")) {
            startBaiduNavi(context, desLon + "",
                    desLat + "", "gcj02", "transit", appName);
        } else if (checkMapAppsIsExist(context, "com.tencent.map")) {
            openTengXun(context, desLat, desLon, provinceName, cityName, areaName, address);
        } else {
            openBrowserToGuide(context, desLat, desLon, provinceName, cityName, areaName, address);
        }
    }


    /**
     * 检测地图应用是否安装
     *
     * @param context
     * @param packagename
     * @return
     */
    public static boolean checkMapAppsIsExist(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    /**
     * 打开高德地图
     *
     * @param context
     * @param curLon       当前位置经度
     * @param curLat       当前位置维度
     * @param desLat       目的地维度
     * @param desLon       目的地经度
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @param address      详细地址
     */
    public static void openGaoDe(Context context, double curLon, double curLat, double desLon, double desLat, String provinceName, String cityName, String areaName, String address) {
        String url = "";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        if (curLon != 0.0 || curLat != 0.0) {
            url = "androidamap://route?sourceApplication=amap&slat=" + curLat + "&slon=" + curLon
                    + "&dlat=" + desLat + "&dlon=" + desLon + "&dname=" + provinceName + cityName
                    + areaName + address + "&dev=0&t=1";
        }
        Uri uri = Uri.parse(url);
        //将功能Scheme以URI的方式传入data
        intent.setData(uri);
        //启动该页面即可
        context.startActivity(intent);
    }

    /**
     * @param desLat     目的地维度
     * @param desLng     目的地经度
     * @param coord_type 坐标类型  允许的值为bd09ll、bd09mc、gcj02、wgs84。
     *                   bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托坐标，gcj02表示经过国测局加密的坐标，wgs84表示gps获取的坐标
     * @param mode       导航类型导航模式
     *                   可选transit（公交）、 driving（驾车）、 walking（步行）和riding（骑行）.
     * @param src        必选参数，格式为：appName  不传此参数，不保证服务
     */
    public static void startBaiduNavi(Context context, String desLng, String desLat, String coord_type, String mode, String src) {
        Intent i1 = new Intent();
        i1.setData(Uri.parse("baidumap://map/direction?destination=" +
                desLat + "," + desLng + "&coord_type=" + coord_type +
                "&mode=" + mode + "&src=" + src + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"));

        context.startActivity(i1);
    }

    /**
     * 腾讯地图
     *
     * @param context
     * @param desLat       目的地经度
     * @param desLon       目的地纬度
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @param address      详细地址
     */
    public static void openTengXun(Context context, double desLat, double desLon, String provinceName, String cityName, String areaName, String address) {
        //终点的显示名称 必要参数
        String txNavName = provinceName + cityName + areaName + address;
        Uri txUri = Uri.parse("qqmap://map/routeplan?type=walk" +
                "&to=" + txNavName
                + "&tocoord=" + desLat + "," + desLon
                + "&referer=频动时刻");
        Intent txNav = new Intent();
        txNav.setData(txUri);
        context.startActivity(txNav);
    }

    /**
     * 打开浏览器 导航
     *
     * @param context
     * @param desLat       目的地经度
     * @param desLon       目的地纬度
     * @param provinceName 省
     * @param cityName     市
     * @param areaName     区
     * @param address      详细地址
     */
    public static void openBrowserToGuide(Context context, double desLat, double desLon, String provinceName, String cityName, String areaName, String address) {
        String url = "http://uri.amap.com/navigation?to=" + desLat + "," + desLon + "," +
                provinceName + cityName
                + areaName + address + "&mode=car&policy=1&src=mypage&coordinate=gaode&callnative=0";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}