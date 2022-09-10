package com.huayun.lib_tools.util.fileUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.huayun.lib_tools.util.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * @Description: 文件操作
 */
public class FileUtil {

    /**
     * 读取文件为byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] readFile(String filePath) {
        final File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            InputStream stream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            stream.read(buffer);
            stream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存文件
     *
     * @param filePath
     * @param data
     */
    public static void saveFile(String filePath, byte[] data) {
        File targetFile = new File(filePath);
        FileOutputStream osw;
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
                osw = new FileOutputStream(targetFile);
                osw.write(data);
                osw.close();
            } else {
                osw = new FileOutputStream(targetFile, true);
                osw.write(data);
                osw.flush();
                osw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存对象为文件
     *
     * @param name   name.txt
     * @param object
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean saveFile(String name, Object object) {
        if (null == object) {
            return false;
        }
        boolean isCreateFiledir = false;  //文件夹是否创建成功
        boolean isCreateFile = false;  //标识文件是否创建成功
        //判断设备是否存在SD卡
        if (SdCardUtil.checkSdState()) {
            //创建文件夹
            File filedir = new File(SdCardUtil.PUBLIC_FILE_PATH, SdCardUtil.folder_name);
            if (filedir.exists()) {
                isCreateFiledir = true;
                LogUtil.xLoge("文件夹已经存在");
                LogUtil.xLoge("文件夹路径==>" + filedir.getAbsolutePath());
            } else {
                try {
                    filedir.mkdir();
                    isCreateFiledir = true;
                    LogUtil.xLoge("文件夹创建成功");
                } catch (Exception e) {
                    LogUtil.xLoge("文件夹创建错误==>" + e.getMessage());
                    isCreateFiledir = false;
                }
            }
            if(isCreateFiledir){
                String keyAndMnemonicPath = filedir.getAbsolutePath() + "/" + name;// 私钥-助记词文件路径
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                File f = new File(SdCardUtil.PROJECT_FILE_PATH);
                //判断文件是否存在
                if (!f.exists()) {
                    LogUtil.xLoge("文件已存在==>" + f.getAbsolutePath());
                    f.mkdirs();
                }
                try {
                    fos = new FileOutputStream(keyAndMnemonicPath);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(object);
                    oos.close();
                    fos.close();
                    LogUtil.xLoge("文件保存成功");
                    isCreateFile = true;
                } catch (FileNotFoundException e) {
                    LogUtil.xLoge("文件创建异常==>" + e.getMessage());
                    isCreateFile = false;
                } catch (IOException e) {
                    LogUtil.xLoge("文件创建异常==>" + e.getMessage());
                    isCreateFile = false;
                }
            }
        } else {
            //不存在SD卡
            isCreateFile = false;
        }
        return isCreateFile;
    }

    /**
     * 读取对象文件
     *
     * @param path
     * @return
     */
    public static Object readObjectFile(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            ois.close();
            fis.close();
            return object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件拷贝
     *
     * @param prefile
     * @param newfile
     */
    public static int copyFile(String prefile, String newfile) {
        try {
            InputStream fosfrom = new FileInputStream(prefile);
            OutputStream fosto = new FileOutputStream(newfile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * asset文件夹读取文件
     *
     * @param context
     * @param path
     * @return
     */
    public static String readTxtFromAsset(Context context, String path) {
        Resources resource = context.getResources();
        AssetManager am = resource.getAssets();
        InputStream is = null;
        String content = "";
        try {
            is = am.open(path);
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            content = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String 原文件路径 如：/aa
     * @param newPath String 复制后路径 如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果捕捉到错误则通知UI线程
        }
    }

    /**
     * 从sd卡读文件
     *
     * @param path
     * @return
     */
    public static String readTxtFromSd(String path) {
        String content = "";
        InputStream stream = null;
        final File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            stream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            stream.read(buffer);
            content = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件路径
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if(files==null){
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到传入文件的大小 自动转化为KB、MB和GB返回String
     *
     * @param f
     * @return
     * @throws IOException
     */
    public static String formentFileSize(File f) throws IOException {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }

        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (s < 1024) {
            fileSizeString = df.format((double) s) + "B";
        } else if (s < 1048576) {
            fileSizeString = df.format((double) s / 1024) + "K";
        } else if (s < 1073741824) {
            fileSizeString = df.format((double) s / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) s / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取文件大小
     *
     * @param f
     * @return double类型，保留两位小数，单位mb
     * @throws IOException
     */
    public static double formentFileSizeMB(File f) throws IOException {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            f.createNewFile();
            System.out.println("文件夹不存在");
        }
        DecimalFormat df = new DecimalFormat("#.00");
        double size = Double.valueOf(df.format((double) s / 1048576));
        return size;
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public static boolean isFilePresence(String path){
        File file = new File(path);
        //判断文件是否存在
        if(file.exists()){
            return true;
        }else {
            return false;
        }
    }


}
