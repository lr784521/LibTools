package com.huayun.lib_tools.util.bitmap;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * 获取图片--Uri
 */
public class UriUtil {
    public static Uri getUri(Context context, String path) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(path));
        } else {
            uri = Uri.fromFile(new File(path));
        }
        return uri;
    }

    public static Uri getUri(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static Uri getFromFile(String path) {
        return Uri.fromFile(new File(path));
    }
}
