package com.example.xiaweizi.shortcutsdemo.google;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.example.xiaweizi.shortcutsdemo.google.MyReceiver
 *     e-mail : 1012126908@qq.com
 *     time   : 2019/05/09
 *     desc   :
 * </pre>
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = Main.TAG;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent);
        if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
            // Refresh all shortcut to update the labels.
            // (Right now shortcut labels don't contain localized strings though.)
            new ShortcutHelper(context).refreshShortcuts(/*force=*/ true);
        }
    }
}