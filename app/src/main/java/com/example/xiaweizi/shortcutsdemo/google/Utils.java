package com.example.xiaweizi.shortcutsdemo.google;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.example.xiaweizi.shortcutsdemo.google.Utils
 *     e-mail : 1012126908@qq.com
 *     time   : 2019/05/09
 *     desc   :
 * </pre>
 */

public class Utils {
    private Utils() {
    }

    public static void showToast(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
