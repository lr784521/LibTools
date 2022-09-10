package com.huayun.lib_tools.util.bitmap;

/**
 * Created by jzm on 2016/7/27.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.huayun.lib_tools.R;
import com.huayun.lib_tools.constant.ConstantCodeBase;
import com.huayun.lib_tools.constant.ConstantKeyBase;
import com.huayun.lib_tools.util.toast.ToastEnum;
import com.huayun.lib_tools.util.toast.ToastUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;

import javax.crypto.MacSpi;

public class BitmapUtil {

    public static String path = "/sdcard/jzm/myHead/";// sd路径
    public static final int PIC_FROM_ALBUMS = 10000;//从相册中选取图片
    public static final int PIC_FROM_CAMERA = 20000;//拍照获取图片
    public static final int PIC_FROM_CROP = 30000;// 裁剪图片

    public static final int PHOTO_REQUEST = 100;
    public static final int CAMERA_REQUEST = 200;
    public static Uri photoUri;

    //默认图片最大高度
    private static int defaultHeight = 720;

    //默认图片最大宽度
    private static int defaultWidth = 1280;

    /**
     * 从相册选择图片来源
     */
    public static void getPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, PHOTO_REQUEST);
    }

    /**
     * bitmap 转 byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);
        byte[] byteArray = buf.array();
        return byteArray;
    }

    /**
     * 从系统相机选择图片来源
     */
    public static void getCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "hand.jpg")));
        activity.startActivityForResult(intent, CAMERA_REQUEST);
    }

    /**
     * 图片路径转Bitmap
     *
     * @param picturePath
     * @return
     */
    public static Bitmap decodeBitmapFromPath(String picturePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (options.outHeight / 400 * 1f);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    /**
     * 扫描Bitmap是否是二维码图片
     *
     * @param bitmap
     * @return
     */
    public static Result scanQrCode(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, ConstantKeyBase.UIN_CODE_UTF_8);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
        } catch (ChecksumException e) {
        } catch (FormatException e) {
        }
        return null;
    }

    /****
     * 调用系统自带切图工具对图片进行裁剪
     * 微信也是
     *
     * @param uri
     */
    public static void photoClip(Activity activity, Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, ConstantCodeBase.CODE_ACTION_PHOTO_CLIP);
    }

    //获取裁剪后的图片
    public static Bitmap getBitmapFromZoomPhoto(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            return extras.getParcelable("data");
        }
        return null;
    }

    //创建图片文件
    public static String newFile(Context context) {
        File file = new File(context.getExternalFilesDir(Environment.MEDIA_MOUNTED), "common");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
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

    //从图库获取位图
    public static Bitmap getBitmapFromGallery(Context context, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                int size = calculateInSampleSize(options, defaultWidth, defaultHeight);
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = size;
                return BitmapFactory.decodeFile(filePath, options);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    //计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        int width = options.outWidth;     //获取图片的宽
        int height = options.outHeight;   //获取图片的高
        int inSampleSize = 4;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(height / reqHeight);
            int widthRatio = Math.round(width / reqHeight);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }


    /**
     * 图片压缩通过途径
     * 先裁剪/再压缩质量
     *
     * @param srcPath
     * @return
     */
    public static File getImagePath(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmapTurnFile(compressImage(bitmap));//压缩好比例大小后再进行质量压缩
    }

    /**
     * Bitmap转File并压缩
     *
     * @param bitmap
     * @return
     */
    public static File bitmapTurnFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis()) + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        Bitmap bitmap = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, os);
            if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                os.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
            }
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(is, null, newOpts);
            newOpts.inJustDecodeBounds = false;
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
            float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / hh);
            }
            if (be <= 0) be = 1;
            newOpts.inSampleSize = be;//设置缩放比例
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            is = new ByteArrayInputStream(os.toByteArray());
            bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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

    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 截屏并存储到相册
     */
    public static void screenshot(FragmentActivity activity, View view) {
        // 获取屏幕
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap cache = view.getDrawingCache();
        saveImageToGallery(activity, cache);
    }

    /**
     * 保存到本地相册
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(FragmentActivity context, Bitmap bmp) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保存图片后发送广播通知更新数据库
        Uri uri = Uri.fromFile(file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        ToastUtil.getInstance().showToast(ToastEnum.SUCCESS,R.string.save_success);
    }

}