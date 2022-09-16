package com.huayun.lib_tools.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.huayun.lib_tools.listener.ISaveFile;
import com.huayun.lib_tools.util.fileUtils.SdCardUtil;
import com.huayun.lib_tools.util.log.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 手机功能工具类
 */
@SuppressWarnings(value = "all")
public class PhotoUtil {

    private static File mPictureFile; //当前拍照图片
    private static Uri mUri; //当前拍照URi

    /**
     * 获取拍照图片
     *
     * @return
     */
    public static File getPhotoFile() {
        return mPictureFile;
    }

    /**
     * 获取拍照Uri
     *
     * @return
     */
    public static Uri getPhotoUri() {
        return mUri;
    }

    /**
     * android 7.0系统解决拍照的问题
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void initPhotoError() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    /**
     * 打开相机
     * <p>
     * OPPO 5.1手机报ActivityNotFoundException
     * 需要try...catch处理
     */
    public static void openCamera(Activity activity, int requestCode) {
        PermissionsUtil.requestPermissions(activity, new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    Intent intent = new Intent();
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        mPictureFile = new File(Environment.getExternalStorageDirectory(),
                                "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        mUri = Uri.fromFile(mPictureFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                        try {
                            activity.startActivityForResult(intent, requestCode);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();

                            LogUtil.xLoge("打开相机异常==>" + e.getMessage());
                        }
                    }
                }
            }
        }, Manifest.permission.CAMERA);
    }

    /**
     * 打开图库
     *
     * @param activity    上下文
     * @param requestCode 请求码
     */
    public static void openAlbum(Activity activity, int requestCode) {
        openAlbum(activity, requestCode, "image/*");
    }

    /**
     * 打开图库
     *
     * @param activity    上下文
     * @param requestCode 请求码
     * @param type        图库展示文件类型
     */
    public static void openAlbum(Activity activity, int requestCode, String type) {
        PermissionsUtil.requestPermissions(activity, new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, type);
                    activity.startActivityForResult(intent, requestCode);
                }
            }
        }, Permission.Group.STORAGE);
    }

    /**
     * 获取图库选择图片路径
     */
    public static String getAlbumResult(Context context, Uri selectedImage) {
        String[] filePathColum = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColum, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColum[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     */
    public static void cropPhoto(Activity activity, int code, Uri uri) {
        cropPhoto(activity, code, uri, 5, 5, 256, 256, SdCardUtil.getSysImgPath());
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     */
    public static void cropPhoto(Activity activity, int code, Uri uri, int aspectX, int aspectY, int outputX, int outputY, String filepath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        /* aspectX aspectY 是裁剪后图片的宽高比 */
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        /* outputX outputY 是裁剪图片宽 */
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("noFaceDetection", true);// 关闭人脸
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(filepath));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 从Uri获取文件路径
     *
     * @param activity
     * @param uri
     * @return
     */
    public static String getPath(Activity activity, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            Cursor cursor = activity.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 截屏并存储到相册
     */
    public static void screenshot(FragmentActivity activity, View view, ISaveFile call) {
        // 获取屏幕
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap cache = view.getDrawingCache();
        saveImageToGallery(activity, cache, call);
    }

    /**
     * 保存到本地相册
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(FragmentActivity context, Bitmap bmp, ISaveFile call) {
        final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)
                ? Environment.getExternalStorageDirectory().getAbsolutePath()
                : "/mnt/sdcard";//保存到SD卡

        // 首先保存图片
        File appDir = new File(SAVE_PIC_PATH + "/USDImage/");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        long nowSystemTime = System.currentTimeMillis();
        String fileName = nowSystemTime + ".png";
        File file = new File(appDir, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            call.onResult(true);
        } catch (Exception e) {
            call.onResult(false);
        }
    }
}
