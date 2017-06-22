package com.sdk.lib.util;

import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 焦雷 on 13-12-19.
 */
public class ShellUtils {

    /**
     * -2: no btscreen
     * -1: not sure
     * 0: no root
     * 1: su
     * 2: btscreen
     */
    private static int _hasRooted = -1;
    private static final String[] su_list;
    private static final String[] bt_list;
    private static String su_path;
    private static String bt_path;
    private static final int CMD_TIMEOUT = 1000 * 5;// 命令执行超时时间5秒
    private static final String CMD_TIMEOUT_RESULT = "TIMEOUT";

    static {

        System.loadLibrary("andashi");

        su_list = new String[]{
                "/system/etc/EngineX/su",
                "/system/xbin/su",
                "/system/bin/su",
                "/system/etc/EngineX/sumoveso",
                "/system/xbin/sumoveso",
                "/system/bin/sumoveso"
        };

        bt_list = new String[]{
                "/system/xbin/btscreen",
                "/system/etc/EngineX/btscreen",
                "/system/bin/btscreen"
        };

        _hasRooted = -1;
    }

    private ShellUtils() {
    }
    
    public static boolean haveRoot() {
		try {
			String[] paths = new String[] { 
					"/system/etc/EngineX/sumoveso", 
					"/system/xbin/sumoveso",
					"/system/bin/sumoveso", 
					"/system/xbin/su", 
					"/data/bin/su", 
					"/system/bin/su", 
					"/system/xbin/btscreen",
					"/system/etc/EngineX/btscreen", 
					"/system/bin/btscreen" 
			};

			File file;
			for (String path : paths) {
				file = new File(path);
				if (file != null && file.exists()) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return false;
	}

    /**/
    private static Process getRootProcess(String cmd) throws IOException {
        Process rootProcess = Runtime.getRuntime().exec(su_path == null ? "su - 0" : su_path);
        DataOutputStream rootOutStream = new DataOutputStream(rootProcess.getOutputStream());

        String[] temp = cmd.split(" ");
        String cmdPath = AndashiEnviroment.getCmdPath(); 
        for (String str : temp) {
            if (new File(cmdPath + str).exists()) {
                cmd = cmd.replace(str, cmdPath + str);
            }
        }
        //Log.e("dyr", cmd);
        rootOutStream.write((cmd + "\n").getBytes());
        rootOutStream.writeBytes("exit\n");
        rootOutStream.flush();

        return rootProcess;
    }

    private static Process getBtscreenProcess(String cmd) throws IOException {
        Process rootProcess = Runtime.getRuntime().exec((bt_path == null ? "btscreen" : bt_path) + " - " + getCode());
        DataOutputStream rootOutStream = new DataOutputStream(rootProcess.getOutputStream());
        String[] temp = cmd.split(" ");
        String cmdPath = AndashiEnviroment.getCmdPath();
        for (String str : temp) {
            if (new File(cmdPath + str).exists()) {
                cmd = cmd.replace(str, cmdPath + str);
            }
        }
        //Log.e("dyr", cmd);
        rootOutStream.write((cmd + "\n").getBytes());
        rootOutStream.writeBytes("exit\n");
        rootOutStream.flush();
        return rootProcess;
    }

    public static String runCmd(String cmd, boolean isInstallApps) {
        StringBuffer result = new StringBuffer();
        Process process = null;
        InputStream in = null;
        try {
            switch (_hasRooted) {
                /**/
                case 1:
                    process = getRootProcess(cmd);
                    break;
                case 2:
                    process = getBtscreenProcess(cmd);
                    break;
                default:
                    process = Runtime.getRuntime().exec(cmd);
                    break;
            }
            in = process.getInputStream();
            
			if (!isInstallApps) {
				int waitCount = 0;
				while (in.available() <= 0) {
					Thread.sleep(100);
					waitCount++;
					if (waitCount >= CMD_TIMEOUT / 100) {
						return CMD_TIMEOUT_RESULT;
					}
				}
			}

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                result.append(new String(buffer, 0, len));
            }
        } catch (Throwable e) {
        } finally {
            closeProcess(process);
        }
        return result.toString();
    }
    
    public static boolean runCmdWithoutRootAndResult(String cmd) {
		Process process = null;
		Worker worker = null;
		int exitValue = -1;
		try {
			process = Runtime.getRuntime().exec(cmd);
			worker = new Worker(process);
			worker.start();
			worker.join(CMD_TIMEOUT);
			if (worker.exit != null) {
				exitValue = worker.exit;
				return exitValue == 0;
			}
			return false;
		} catch (Throwable e) {
			if (worker != null) {
				worker.interrupt();
				Thread.currentThread().interrupt();
			}
			return false;
		} finally {
			closeProcess(process);
		}
	}

    public static boolean runCmdWithoutResult(String cmd) {
        Process process = null;
        Worker worker = null;
        int exitValue = -1;
        try {
            switch (_hasRooted) {
                /**/
                case 1:
                    process = getRootProcess(cmd);
                    break;
                case 2:
                    process = getBtscreenProcess(cmd);
                    break;
                default:
                    process = Runtime.getRuntime().exec(cmd);
                    break;
            }
            worker = new Worker(process);
            worker.start();
            worker.join(CMD_TIMEOUT);
            if (worker.exit != null) {
                exitValue = worker.exit;
                return exitValue == 0;
            }
            return false;
        } catch (Throwable e) {
            if (worker != null) {
                worker.interrupt();
                Thread.currentThread().interrupt();
            }
            return false;
        } finally {
            closeProcess(process);
        }
    }

    public synchronized static boolean hasRooted() {
        if (_hasRooted != -1) return _hasRooted > 0;
        if (hasBtScreen()) return true;
        
        Process process = null;
        InputStream in = null;
        try {
            for (String path : su_list) {
                if (new File(path).exists()) {
                    su_path = path;
                    _hasRooted = 1;
                    return true;
                }
            }

            process = getRootProcess("id");
            in = process.getInputStream();

            byte[] buffer = new byte[64];
            int len = 0;
            StringBuilder result = new StringBuilder();
            while ((len = in.read(buffer)) != -1) {
                result.append(new String(buffer, 0, len));
            }
            _hasRooted = result.toString().contains("uid=0") ? 1 : 0;

            if (_hasRooted < 1) {
                if (new File("/system/xbin/su").exists() || new File("/system/bin/su").exists()) {
                    su_path = "su";
                    process = getRootProcess("id");
                    in = process.getInputStream();

                    buffer = new byte[64];
                    len = 0;
                    result = new StringBuilder();
                    while ((len = in.read(buffer)) != -1) {
                        result.append(new String(buffer, 0, len));
                    }
                    _hasRooted = result.toString().contains("uid=0") ? 1 : 0;
                }
            }
            
            if( _hasRooted > 0) 
            	return true;
//            return _hasRooted > 0;
        } catch (Throwable e) {
        } finally {
            closeProcess(process);
        }/**/
        
        return false;
    }

    private static boolean hasBtScreen() {
        Process process = null;
        InputStream in = null;
        try {
            for (String path : bt_list) {
                if (new File(path).exists()) {
                    bt_path = path;
                    process = getBtscreenProcess("id");
                    in = process.getInputStream();
                    byte[] buffer = new byte[64];
                    int len = 0;
                    StringBuilder result = new StringBuilder();
                    while ((len = in.read(buffer)) != -1) {
                        result.append(new String(buffer, 0, len));
                    }
                    _hasRooted = result.toString().contains("uid=0") ? 2 : _hasRooted;

                    if (_hasRooted == 2) {
                        return true;
                    } else {
                        bt_path = null;
                    }
                }
            }

            process = getBtscreenProcess("id");
            in = process.getInputStream();
            byte[] buffer = new byte[64];
            int len = 0;
            StringBuilder result = new StringBuilder();
            while ((len = in.read(buffer)) != -1) {
                result.append(new String(buffer, 0, len));
            }
            _hasRooted = result.toString().contains("uid=0") ? 2 : _hasRooted;
            return _hasRooted > 1;
        } catch (Throwable e) {
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable e) {
                }
            }
            closeProcess(process);
        }
    }

    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
            }
        }
    }

    private static Boolean supportMount = null;

    public static boolean supportMount() {
        if (supportMount != null) {
            return supportMount;
        }

        String dev = "";
        String mountInfo = runCmd("mount");
        if (!TextUtils.isEmpty(mountInfo)) {
            String[] lines = mountInfo.split("\n");
            for (String line : lines) {
                if (line.contains(" /system ")) {
                    String[] items = line.split(" ");
                    for (int i = 0; i < items.length; i++) {
                        if (items[i].equals("/system") && i > 0) {
                            dev = items[i - 1];
                        }
                    }
                }
            }
        }

        runCmdWithoutResult("mount -o remount,rw " + dev + " /system && echo test > /system/app/test.tmp");
        boolean wFileExist = new File("/system/app/test.tmp").exists();
        runCmdWithoutResult("rm /system/app/test.tmp && mount -o remount,ro " + dev + " /system");
        return wFileExist;
    }

    private native static long getCode();

    private static void closeProcess(Process process) {
        if (process != null) {
            try {
                process.destroy();
            } catch (Throwable e) {
            }
            process = null;
        }
    }
    
    private static String system_dev = "";

    public static String getSystemDev() {
        if (!TextUtils.isEmpty(system_dev)) {
            return system_dev;
        }

        String mountInfo = runCmd("mount");
        if (!TextUtils.isEmpty(mountInfo)) {
            String[] lines = mountInfo.split("\n");
            for (String line : lines) {
                if (line.contains(" /system ")) {
                    String[] items = line.split(" ");
                    for (int i = 0; i < items.length; i++) {
                        if (items[i].equals("/system") && i > 0) {
							system_dev = items[i - 1];
						}
                    }
                }
            }
        }
        return system_dev;
    }
    
    public static int get_hasRooted() {
        return _hasRooted;
    }
    
    public static String runCmd(String cmd) {
    	return runCmd(cmd, false);
    }
	
    public static String runAndashiCmd(String cmd) {
        cmd = AndashiEnviroment.getCmdPath() + cmd;
        return runCmd(cmd);
    }
    
    public static boolean runAndashiCmdWithoutResult(String cmd) {
        cmd = AndashiEnviroment.getCmdPath() + cmd;
        return runCmdWithoutResult(cmd);
    }
}
