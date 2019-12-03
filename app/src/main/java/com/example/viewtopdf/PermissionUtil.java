package com.example.viewtopdf;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.provider.Settings;


import androidx.core.content.ContextCompat;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtil {
    public static PermissionUtil instance;

    public static PermissionUtil getInstance() {
        if (instance == null) {
            instance = new PermissionUtil();
        }
        return instance;
    }

    public boolean havePermission(Context context, String s) {
        boolean hava = true;
        if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
            hava = false;
        }
        return hava;
    }


    public static int audioSource = MediaRecorder.AudioSource.MIC;
    public static int sampleRateInHz = 44100;
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    public static int bufferSizeInBytes = 0;

    /**
     * 判断是是否有录音权限
     */
    public static boolean isHasPermission(final Context context) {
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        try {
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            context.startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
        }
        audioRecord.stop();
        audioRecord.release();
        return true;
    }


    /**
     * 判断是否拥有内存卡读写权限
     */
    public static boolean WriteAndReadPer(Activity mcontext, int REQUEST_CODE_SAVE_IMG) {
        boolean isHava = false;
        if (Build.VERSION.SDK_INT >= 19) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(mcontext, mPermissionList)) {
                //已经同意过
                isHava = true;
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(mcontext, "保存图片需要读取sd卡的权限", REQUEST_CODE_SAVE_IMG, mPermissionList);
            }
        } else {
        }
        return isHava;
    }

    /**
     * 登陆时申请： 内存卡读写  录音  gps  定位  照相 读取手机状态，蓝牙
     */
    public static boolean loginPermission(Activity activity, int LOGINPRESSION) {
        boolean isHava = false;
        if (Build.VERSION.SDK_INT >= 19) {
            //读取sd卡的权限
            String[] mPermissionList = new String[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                mPermissionList = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.REQUEST_INSTALL_PACKAGES,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_LOGS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.SET_DEBUG_APP,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.ACCESS_NETWORK_STATE
                };
            }
            if (EasyPermissions.hasPermissions(activity, mPermissionList)) {
                //已经同意过
                isHava = true;
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                isHava = false;
                EasyPermissions.requestPermissions(activity, "请允许对应权限", LOGINPRESSION, mPermissionList);
            }
        } else {
            isHava = true;
        }
        return isHava;
    }

    /**
     * 确定权限是否申请
     */
    public static boolean checkRecorderPermissionMethod(Activity activity, int LOGINPRESSION) {
        boolean isHava = false;
        if (Build.VERSION.SDK_INT >= 19) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
            };
            if (EasyPermissions.hasPermissions(activity, mPermissionList)) {
                isHava = true;
            } else {
                isHava = false;
                EasyPermissions.requestPermissions(activity, "请允许对应权限", LOGINPRESSION, mPermissionList);
            }
        } else {
            isHava = true;
        }
        return isHava;
    }

    /**
     * 单独申请相机权限
     */
    public static boolean cameraPermission(Activity activity, int LOGINPRESSION) {
        boolean isHava = false;
        if (Build.VERSION.SDK_INT >= 19) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
            };
            if (EasyPermissions.hasPermissions(activity, mPermissionList)) {
                isHava = true;
            } else {
                EasyPermissions.requestPermissions(activity, "请开启您的照相机权限", LOGINPRESSION, mPermissionList);
            }
        } else {
            isHava = true;
        }
        return isHava;
    }

    /**
     * 华为手机专用
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }



}
