package com.example.android.lovemeter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class RequestPermissionHandler {
    private Activity mActivity;
    private RequestPermissionListener mRequestPermissionListener;
    private int mRequestCode;

    public void requestPermission(Activity activity, @NonNull String[] permissions, int requestCode,
                                  RequestPermissionListener listener) {
        mActivity = activity;
        mRequestCode = requestCode;
        mRequestPermissionListener = listener;

        if (!needRequestRuntimePermissions()) {
            mRequestPermissionListener.onSuccess();
            return;
        }
        requestUnGrantedPermissions(permissions, requestCode);
    }

    public boolean needRequestRuntimePermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void requestUnGrantedPermissions(String[] permissions, int requestCode) {
        String[] unGrantedPermissions = findUnGrantedPermissions(permissions);
        if (unGrantedPermissions.length == 0) {
            mRequestPermissionListener.onSuccess();
            return;
        }
        ActivityCompat.requestPermissions(mActivity, unGrantedPermissions, requestCode);
    }

    public boolean isPermissionGranted(String permission) {
        return ActivityCompat.checkSelfPermission(mActivity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public String[] findUnGrantedPermissions(String[] permissions) {
        List<String> unGrantedPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!isPermissionGranted(permission)) {
                unGrantedPermissionList.add(permission);
            }
        }
        return unGrantedPermissionList.toArray(new String[0]);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mRequestPermissionListener.onFailed();
                        return;
                    }
                }
                mRequestPermissionListener.onSuccess();
            } else {
                mRequestPermissionListener.onFailed();
            }
        }
    }

    public interface RequestPermissionListener {
        void onSuccess();

        void onFailed();
    }
}
