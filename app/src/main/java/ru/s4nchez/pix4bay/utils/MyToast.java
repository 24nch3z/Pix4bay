package ru.s4nchez.pix4bay.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by S4nchez on 01.05.2018.
 */

// Для удобного вывода сообщений в Тосте + тут, если два сообщения быстро сменяются,
// то первое закроется моментально и отобразится второе сообщение
public class MyToast {

    private static MyToast sMyToast;

    private Context mContext;
    private Toast mToast;

    private MyToast(Context applicationContext) {
        mContext = applicationContext.getApplicationContext();
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
    }

    public static MyToast get(Context context) {
        if (sMyToast == null) {
            sMyToast = new MyToast(context);
        }
        return sMyToast;
    }

    public void show(Object ... messages) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < messages.length; i++) {
            text.append(messages[i].toString());
            text.append("\n");
        }

        mToast.setText(text.toString().trim());
        mToast.show();
    }
}
