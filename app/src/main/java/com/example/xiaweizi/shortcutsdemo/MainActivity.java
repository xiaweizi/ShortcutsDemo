package com.example.xiaweizi.shortcutsdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customAttrValues();
//        createPinnedShortcuts();
    }

    private void customAttrValues() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "dynamicId1")
                    .setShortLabel("d-ShortLabel1")
                    .setLongLabel("dynamicLongLabel1")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://xiaweizi.cn/")))
                    .build();

            Intent intent = new Intent(this, TestActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.putExtra("key", "this is from dynamic label2");
            ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "dynamicId2")
                    .setShortLabel("d-ShortLabel2")
                    .setLongLabel("dynamicShortLabel2")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .setDisabledMessage("dynamicMessage1")
                    .build();


            List<ShortcutInfo> shortcutInfos = new ArrayList<>();
            shortcutInfos.add(shortcut);
            shortcutInfos.add(shortcut1);

            if (shortcutManager != null) {
                shortcutManager.setDynamicShortcuts(shortcutInfos);
            }
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
