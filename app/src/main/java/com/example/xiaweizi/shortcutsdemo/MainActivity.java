package com.example.xiaweizi.shortcutsdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "shortCut::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createPinnedShortcuts();
        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        customAttrValues();
                    }
                }).start();
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
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_recent_light))
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

    private void customAttrValues() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            long lastTime = System.currentTimeMillis();
            Intent intent = new Intent(this, TestActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.putExtra("key", "this is from dynamic label2");

            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            int maxShortcutCountPerActivity = shortcutManager.getMaxShortcutCountPerActivity();
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://xiaweizi.cn/"));
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
            String label = getResources().getString(R.string.dynamic_shortcut_short_label);
            SpannableStringBuilder colouredLabel = new SpannableStringBuilder(label);
            colouredLabel.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "dynamicId1")
                    .setShortLabel(colouredLabel)
                    .setLongLabel("dynamicLongLabel1")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_ces))
                    .setIntent(intent)
                    .build();

            ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "dynamicId2")
                    .setShortLabel("1")
                    .setLongLabel("12")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .setDisabledMessage("dynamicMessage2")
                    .build();



            List<ShortcutInfo> shortcutInfos = new ArrayList<>();
            shortcutInfos.add(shortcut);
            shortcutInfos.add(shortcut1);

            if (shortcutManager != null) {
                shortcutManager.setDynamicShortcuts(shortcutInfos);
                shortcutManager.reportShortcutUsed("dynamicId1");
            }

            List<ShortcutInfo> manifestShortcuts = shortcutManager.getManifestShortcuts();
            for (int i = 0; i < manifestShortcuts.size(); i++) {
                Log.i(TAG, "manifest-" + i + " id: " + manifestShortcuts.get(i).getId() + " rank: " + manifestShortcuts.get(i).getRank());
            }
            List<ShortcutInfo> dynamicShortcuts = shortcutManager.getDynamicShortcuts();
            for (int i = 0; i < dynamicShortcuts.size(); i++) {
                Log.i(TAG, "dynamic-" + i + " id: " + dynamicShortcuts.get(i).getId() + " rank: " + dynamicShortcuts.get(i).getRank());
            }

            Log.i(TAG, "total time: " + (System.currentTimeMillis() - lastTime) + " current thread name: " + Thread.currentThread().getName());
        }
    }

    private void createPinnedShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                Intent intent = new Intent(this, TestActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "my-shortcut").setShortLabel("pinned").setIntent(intent).build();
                Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                        pinnedShortcutCallbackIntent, 0);
                shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());

            }
        }
    }
}
