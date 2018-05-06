package ru.s4nchez.pix4bay.utils;

/**
 * Created by S4nchez on 22.04.2018.
 */

public class Log {

    private static final String TAG = "sssss";

    public static void l(Object ... objects) {
        StringBuilder message = new StringBuilder();
        for (Object obj : objects) {
            message.append(obj.toString());
            message.append(" ");
        }
        android.util.Log.d(TAG, message.toString().trim());
    }
}