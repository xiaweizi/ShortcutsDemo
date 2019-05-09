package com.example.xiaweizi.shortcutsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.example.xiaweizi.shortcutsdemo.TranslateReceiver
 *     e-mail : 1012126908@qq.com
 *     time   : 2019/05/09
 *     desc   :
 * </pre>
 */

public class TranslateReceiver extends BroadcastReceiver {

    private static final String TAG = "TranslateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
            Log.i(TAG, "onReceive: receive local changed!!");
        }
    }
}
