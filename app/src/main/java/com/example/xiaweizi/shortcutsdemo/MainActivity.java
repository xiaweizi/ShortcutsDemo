package com.example.xiaweizi.shortcutsdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customAttrValues();
        createPinnedShortcuts();
    }

    private void customAttrValues() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Website")
                    .setLongLabel("Open the website")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://xiaweizi.cn/")))
                    .build();

            if (shortcutManager != null) {
                shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcut));
            }
        }
    }

    private void createPinnedShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                Intent intent = new Intent(this, TestActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                ShortcutInfo pinShortcutInfo =
                        new ShortcutInfo.Builder(this, "my-shortcut").setShortLabel("pinned").setIntent(intent).build();

                Intent pinnedShortcutCallbackIntent =
                        shortcutManager.createShortcutResultIntent(pinShortcutInfo);
                PendingIntent successCallback = PendingIntent.getBroadcast(this, /* request code */ 0,
                        pinnedShortcutCallbackIntent, /* flags */ 0);
                shortcutManager.requestPinShortcut(pinShortcutInfo,
                        successCallback.getIntentSender());
            }
        }
    }
}
