package com.sdk.lib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 焦雷 on 13-12-24.
 */
public class AndashiEnviroment extends Handler {

    private AndashiEnviroment() {
    }

    /**
     * 必须创建文件：assets/cmd/$ENVIRONMENT_VERSION
     */
    public static int ENVIRONMENT_VERSION = 23;
    private static final String ASSETS_CMD_PATH = "cmd";
    private static String cmdPath;
    private static String tmpCmdPath = "/data/data/%s/files/cmd/";

    private static boolean inited = false;

    public synchronized static void init(Context context) {
        if (!inited) {
            cmdPath = context.getFilesDir() + "/cmd/";
            doInit(context);
            inited = true;
        }
    }

    public static void checkCmdPath(Context context){
    	if(TextUtils.isEmpty(cmdPath))
    		cmdPath = String.format(tmpCmdPath, context.getApplicationContext().getPackageName());
    }
    
    public static void doInit(Context context) {
    	checkCmdPath(context);
        File cmdDir = new File(cmdPath);
        if (!cmdDir.exists()) {
            cmdDir.mkdirs();
        }

        // 判断是否已经初始化
        try {
            File versionFile = new File(cmdPath + ENVIRONMENT_VERSION);
            if (versionFile.exists()) {
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // Install all cmd files.
        try {
            String[] cmdFiles = context.getAssets().list(ASSETS_CMD_PATH);
            for (String file : cmdFiles) {
                installCmd(context, file);
            }
        } catch (Throwable e) {
        }
    }

    @SuppressLint("NewApi") 
    private static void installCmd(Context context, String cmdFile) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
        	checkCmdPath(context);
            String cmdFilePath = AndashiEnviroment.cmdPath + cmdFile;
            File targetFile = new File(cmdFilePath);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            in = new BufferedInputStream(context.getAssets().open(ASSETS_CMD_PATH + "/" + cmdFile));
            out = new BufferedOutputStream(new FileOutputStream(targetFile, false));
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            closeQuietly(in);
            closeQuietly(out);
            targetFile.setReadable(true, false);
            targetFile.setWritable(true, false);
            targetFile.setExecutable(true, false);
        } catch (Throwable e) {
        }
    }

    public static String getCmdPath() {
        return cmdPath;
    }
    
    /** 
     * Unconditionally close an <code>InputStream</code>. 
     * <p> 
     * Equivalent to {@link InputStream#close()}, except any exceptions will be ignored. 
     * This is typically used in finally blocks. 
     * 
     * @param input  the InputStream to close, may be null or already closed 
     */
    public static void closeQuietly(InputStream input) { 
        try { 
            if (input != null) { 
                input.close(); 
            } 
        } catch (IOException ioe) { 
            // ignore 
        } 
    } 
  
    /** 
     * Unconditionally close an <code>OutputStream</code>. 
     * <p> 
     * Equivalent to {@link OutputStream#close()}, except any exceptions will be ignored. 
     * This is typically used in finally blocks. 
     * 
     * @param output  the OutputStream to close, may be null or already closed 
     */
    public static void closeQuietly(OutputStream output) { 
        try { 
            if (output != null) { 
                output.close(); 
            } 
        } catch (IOException ioe) { 
            // ignore 
        } 
    } 
}
