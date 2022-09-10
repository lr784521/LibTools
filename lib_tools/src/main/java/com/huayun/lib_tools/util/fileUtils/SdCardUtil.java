package com.huayun.lib_tools.util.fileUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;

import androidx.annotation.RequiresApi;

import com.huayun.lib_tools.BuildConfig;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SdCardUtil {

    private static String cache_path = ""; // 应用的cache目录用于存放缓存
    public static String folder_name = "hysz"; // 文件夹名称
    public static final String PROJECT_FILE_PATH = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/"+folder_name+"/";// 项目路径
    public static final String PUBLIC_FILE_PATH = Environment.
            getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() ; // 设备公有路径
    public static final String DEFAULT_PHOTO_PATH = PROJECT_FILE_PATH +"pics/";
    public static final String DEFAULT_RECORD_PATH =PROJECT_FILE_PATH + "record/";
    public static String TEMP = "file:///" + PROJECT_FILE_PATH + "camera.jpg";



    /**
     * 判断是否有sd
     */
    public static boolean checkSdState() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取Sd卡路径
     */
    public static String getSd() {
        if (!checkSdState()) {
            return "";
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取相册路径
     */
    public static String getDCIM() {
        if (!checkSdState()) {
            return "";
        }
        String path = getSd() + "/dcim/";
        if (new File(path).exists()) {
            return path;
        }
        path = getSd() + "/DCIM/";
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return "";
            }
        }
        return path;
    }

    /**
     * 获取DCIM目录下的Camera目录
     */
    public static String getCamera() {
        if (!checkSdState()) {
            return "";
        }
        String path = getDCIM() + "/Camera/";
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return "";
            }
        }
        return path;
    }


    /**
     * 初始化文件目录
     */
    public static void initFileDir(Context context) {
        File projectDir = new File(PROJECT_FILE_PATH);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
        File fileDir = new File(DEFAULT_PHOTO_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File recordDir = new File(DEFAULT_RECORD_PATH);
        if(!recordDir.exists()){
            recordDir.mkdirs();
        }
        cache_path = Environment.getExternalStorageDirectory().getPath()
                + "/Android/data/" + context.getPackageName() + "/cache/";

        File cacheDir = new File(cache_path);
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }


    /**
     * 获取app缓存目录
     */
    public static String getCacheDir(Context context) {
        if (!checkSdState()) {
            return "";
        }
        return getSd() + "/Android/data/" + context.getPackageName() + "/cache/";
    }


    /**
     * 相册目录下的图片路径
     */
    public static String getSysImgPath() {
        if (!checkSdState()) {
            return "";
        }
        return getCamera() + "IMG_" + System.currentTimeMillis() + ".jpg";
    }


    /**
     * 获取拓展存储Cache的绝对路径
     *
     * @param context
     */
    public static String getExternalCacheDir(Context context) {
        if (!SdCardUtil.checkSdState())
            return null;
        StringBuilder sb = new StringBuilder();
        File file = context.getExternalCacheDir();
        if (file != null) {
            sb.append(file.getAbsolutePath()).append(File.separator);
        } else {
            sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                    .append("/cache/").append(File.separator).toString();
        }
        return sb.toString();
    }

    public static String getExternalFilesDir(Context context, String type) {
        if (!SdCardUtil.checkSdState())
            return null;
        StringBuilder sb = new StringBuilder();
        File file = context.getExternalFilesDir(type);
        if (file != null) {
            sb.append(file.getAbsolutePath()).append(File.separator);
        } else {
            sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                    .append("/files/").append(File.separator).toString();
        }
        return sb.toString();
    }

    /**
     * 获取拍照路径
     *
     * @param context
     * @return
     */
    public static String getCaremaPath(Context context) {
        return getExternalCacheDir(context) + "carema.jpg";
    }

    public static String getCacheTempImage(Context context) {
        return getExternalCacheDir(context) + System.currentTimeMillis() + ".jpg";
    }
    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public static String getTempCamera() {
        return PROJECT_FILE_PATH + System.currentTimeMillis() + ".jpg";
    }
}
