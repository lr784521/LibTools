package com.huayun.lib_tools.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.huayun.lib_tools.constant.ConstantCodeBase;
import com.huayun.lib_tools.util.fileUtils.SdCardUtil;
import com.huayun.lib_tools.util.log.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
public class PhotoUtil {


    private static File mPictureFile; //当前拍照图片
    private static Uri mUri; //当前拍照URi

    /**
     * 获取拍照图片
     * @return
     */
    public static File getPhotoFile() {
        return mPictureFile;
    }
    /**
     * 获取拍照Uri
     * @return
     */
    public static Uri getPhotoUri() {
        return mUri;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void initPhotoError() {
        // android 7.0系统解决拍照的问题
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
    public static void openCamera(Activity activity) {
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
                            activity.startActivityForResult(intent, ConstantCodeBase.CODE_ACTION_OPEN_CAMERA);
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
     * 打开相册
     */
    public static void openAlbum(Activity activity){
        PermissionsUtil.requestPermissions(activity, new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                    activity.startActivityForResult(intent, ConstantCodeBase.CODE_ACTION_OPEN_ALBUM);
                }
            }
        }, Permission.Group.STORAGE);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void cropPhoto(Activity activity, int code, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        /* aspectX aspectY 是裁剪后图片的宽高比 */
        intent.putExtra("aspectX", 5);
        intent.putExtra("aspectY", 5);
        /* outputX outputY 是裁剪图片宽 */
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("noFaceDetection", true);// 关闭人脸
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(SdCardUtil.getSysImgPath()));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     */
    public static void cropPhoto(Activity activity, int code, Uri uri, String filepath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        /* aspectX aspectY 是裁剪后图片的宽高比 */
        intent.putExtra("aspectX", 5);
        intent.putExtra("aspectY", 5);
        /* outputX outputY 是裁剪图片宽 */
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("noFaceDetection", true);// 关闭人脸
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(filepath));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 对图片进行剪裁，通过Intent来调用系统自带的图片剪裁API
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void cropPhotoTop(Activity activity, int code, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        /* aspectX aspectY 是裁剪后图片的宽高比 */
        intent.putExtra("aspectX", 64);
        intent.putExtra("aspectY", 45);
        /* outputX outputY 是裁剪图片宽 */
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 450);
        intent.putExtra("noFaceDetection", true);// 关闭人脸
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(SdCardUtil.getSysImgPath()));
        activity.startActivityForResult(intent, code);
    }

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


    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
