package com.huayun.lib_tools.util.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.huayun.lib_tools.util.scan_code.encode.CodeCreator;

import java.util.Hashtable;

/**
 * 二维码工具类
 */
public class QRImageUtil {
    private static float scale;

    /**
     * 创建 带背景二维码
     * @param context
     * @param content
     * @return
     */
    public static Bitmap createQRImage(Context context, String content) {
        scale = context.getResources().getDisplayMetrics().density;
        return createQRImage(content);
    }


    /**
     * 创建无背景 二维码
     * @param context
     * @param content
     * @return
     */
    public static Bitmap createNobgQRImage(Context context, String content) {
        scale = context.getResources().getDisplayMetrics().density;
        return createNoBgQRImage(content);
    }

    /**
     * 无logo二维码
     * @param content
     * @return
     */
    public static Bitmap createNoLogoQr(String content){
        return CodeCreator.createQRCode(content, 400, 400, null);
    }

    /**
     * 有logo二维码
     * @param context
     * @param content
     * @param imgRes
     * @return
     */
    public static Bitmap createLogoQr(Context context, String content, int imgRes){
        return CodeCreator.createQRCode(content, 400, 400, BitmapFactory.decodeResource(context.getResources(),imgRes));
    }


    /**
     * 生成二维码图片
     *
     * @param content
     * @return
     */
    private static Bitmap createNoBgQRImage(String content) {

        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 设置按钮启用
        // btn_go.setEnabled(true);

        // 二维码的宽高
        int QR_WIDTH = dip2px(400);
        int QR_HEIGHT = dip2px(400);

        try {
            // 判断content合法性
            // if (content == null || "".equals(content) || content.length() <
            // 1) {
            // return null;
            // }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xFF000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xFFFFFFFF;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码图片
     *
     * @param content
     * @return
     */
    private static Bitmap createQRImage(String content) {

        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 设置按钮启用
        // btn_go.setEnabled(true);

        // 二维码的宽高
        int QR_WIDTH = dip2px(400);
        int QR_HEIGHT = dip2px(400);

        try {
            // 判断content合法性
            // if (content == null || "".equals(content) || content.length() <
            // 1) {
            // return null;
            // }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xFF000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xFFFFFFFF;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int dip2px(float dipValue) {
//        final float scale = fragment.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 圆角 bitmap
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 50;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
