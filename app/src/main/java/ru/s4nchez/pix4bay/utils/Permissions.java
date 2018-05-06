package ru.s4nchez.pix4bay.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by S4nchez on 22.04.2018.
 */

// Класс для работы с разрешениями
public class Permissions {

    public static void requestPermissions(Activity activity, int requestCode, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(permissions, requestCode);
        }
    }

    public static void requestPermission(Activity activity, int requestCode, String permission) {
        requestPermissions(activity, requestCode, new String[] { permission });
    }

    public static boolean hasPermissions(Context context, String[] permissions){
        for (String perms : permissions){
            if (context.checkCallingOrSelfPermission(perms) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermission(Context context, String permission) {
        return hasPermissions(context, new String[] { permission });
    }
}