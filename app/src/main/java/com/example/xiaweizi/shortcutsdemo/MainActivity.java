package com.example.xiaweizi.shortcutsdemo;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.xiaweizi.shortcutsdemo.google.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "shortCut::";

    private static final String ID_DYNAMIC_1 = "id_dynamic_1";
    private static final String ID_DYNAMIC_2 = "id_dynamic_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        setDynamicShortcuts();
                    }
                });
            }
        });
        final TextView tvUpdate = findViewById(R.id.update);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPinnedShortcuts();
            }
        });
    }

    private void updateShortcutInfo() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            Log.i(TAG, "updateShortcutInfo: ");
            long lastTime = System.currentTimeMillis();
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            if (shortcutManager != null) {
                List<ShortcutInfo> dynamicShortcuts = shortcutManager.getDynamicShortcuts();
                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "dynamicId1")
                        .setShortLabel("d-ShortLabel4")
                        .setLongLabel("dynamicLongLabel4")
                        .setIcon(Icon.createWithResource(this, R.drawable.add))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/guide/topics/ui/shortcuts/managing-shortcuts")))
                        .setRank(4)
                        .build();
                dynamicShortcuts.add(shortcutInfo);
            }
            Log.i(TAG, " total time: " + (System.currentTimeMillis() - lastTime));
            int size = shortcutManager.getPinnedShortcuts().size();
            Log.i(TAG, "pinned size: " + size);
        }
    }

    private void setDynamicShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            List<ShortcutInfo> shortcutInfo = new ArrayList<>();
            shortcutInfo.add(createShortcutInfo1());
            shortcutInfo.add(createShortcutInfo2());
            if (shortcutManager != null) {
                shortcutManager.setDynamicShortcuts(shortcutInfo);
            }
            Utils.showToast(this, "set dynamic shortcuts success!");
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private ShortcutInfo createShortcutInfo1() {
        return new ShortcutInfo.Builder(this, ID_DYNAMIC_1)
                .setShortLabel(getString(R.string.dynamic_shortcut_short_label1))
                .setLongLabel(getString(R.string.dynamic_shortcut_long_label1))
                .setIcon(Icon.createWithResource(this, R.drawable.add))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xiaweizi.cn/")))
                .build();
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private ShortcutInfo createShortcutInfo2() {
        Intent intent = new Intent(this, TestActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("key", "fromDynamicShortcut");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
        String label = getResources().getString(R.string.dynamic_shortcut_long_label2);
        SpannableStringBuilder colouredLabel = new SpannableStringBuilder(label);
        colouredLabel.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return new ShortcutInfo.Builder(this, ID_DYNAMIC_2)
                .setShortLabel(getString(R.string.dynamic_shortcut_short_label2))
                .setLongLabel(colouredLabel)
                .setIcon(Icon.createWithResource(this, R.drawable.link))
                .setIntent(intent)
                .build();
    }

    private void createPinnedShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                Intent intent = new Intent(this, TestActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("key", "fromPinnedShortcut");
                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "my-shortcut")
                        .setShortLabel(getString(R.string.pinned_shortcut_short_label2))
                        .setLongLabel(getString(R.string.pinned_shortcut_long_label2))
                        .setIcon(Icon.createWithResource(this, R.drawable.add))
                        .setIntent(intent)
                        .build();
                Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                        pinnedShortcutCallbackIntent, 0);
                boolean b = shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
                Utils.showToast(this, "set pinned shortcuts " + (b ? "success" : "failed") + "!");
            }
        }
    }
}
