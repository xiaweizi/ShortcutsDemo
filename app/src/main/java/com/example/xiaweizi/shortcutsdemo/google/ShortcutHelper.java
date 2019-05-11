package com.example.xiaweizi.shortcutsdemo.google;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.xiaweizi.shortcutsdemo.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.example.xiaweizi.shortcutsdemo.google.ShortcutHelper
 *     e-mail : 1012126908@qq.com
 *     time   : 2019/05/09
 *     desc   :
 * </pre>
 */

public class ShortcutHelper {
    private static final String TAG = Main.TAG;

    private static final String EXTRA_LAST_REFRESH =
            "com.example.android.shortcutsample.EXTRA_LAST_REFRESH";

    private static final long REFRESH_INTERVAL_MS = 60 * 60 * 1000;

    private final Context mContext;

    private final ShortcutManager mShortcutManager;

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public ShortcutHelper(Context context) {
        mContext = context;
        mShortcutManager = mContext.getSystemService(ShortcutManager.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void maybeRestoreAllDynamicShortcuts() {
        if (mShortcutManager.getDynamicShortcuts().size() == 0) {
            // NOTE: If this application is always supposed to have dynamic shortcuts, then publish
            // them here.
            // Note when an application is "restored" on a new device, all dynamic shortcuts
            // will *not* be restored but the pinned shortcuts *will*.
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void reportShortcutUsed(String id) {
        mShortcutManager.reportShortcutUsed(id);
    }

    /**
     * Use this when interacting with ShortcutManager to show consistent error messages.
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void callShortcutManager(BooleanSupplier r) {
        try {
            if (!r.getAsBoolean()) {
                Utils.showToast(mContext, "Call to ShortcutManager is rate-limited");
            }
        } catch (Exception e) {
            Log.e(TAG, "Caught Exception", e);
            Utils.showToast(mContext, "Error while calling ShortcutManager: " + e.toString());
        }
    }

    /**
     * Called when the activity starts.  Looks for shortcuts that have been pushed and refreshes
     * them (but the refresh part isn't implemented yet...).
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void refreshShortcuts() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "refreshingShortcuts...");


                // Check all existing dynamic and pinned shortcut, and if their last refresh
                // time is older than a certain threshold, update them.

                final List<ShortcutInfo> updateList = new ArrayList<>();
                for (ShortcutInfo shortcut : getShortcuts()) {
                    Log.i(TAG, shortcut.getId() + " is immutable: " + shortcut.isImmutable());
                    if (shortcut.isImmutable()) {
                        continue;
                    }
                    Log.i(TAG, "Refreshing shortcut: " + shortcut.getId());
                    final ShortcutInfo.Builder b = new ShortcutInfo.Builder(mContext, shortcut.getId());
                    setSiteInformation(b, shortcut.getIntent().getData());
                    updateList.add(b.build());
                }
                // Call update.
                if (updateList.size() > 0) {
                    callShortcutManager(new BooleanSupplier() {
                        @Override
                        public boolean getAsBoolean() {
                            Log.i(TAG, "update short cuts size: " + updateList.size());
                            return mShortcutManager.updateShortcuts(updateList);
                        }
                    });
                }
            }
        });
    }

    /**
     * Return all mutable shortcuts from this app self.
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public List<ShortcutInfo> getShortcuts() {
        // Load mutable dynamic shortcuts and pinned shortcuts and put them into a single list
        // removing duplicates.

        final List<ShortcutInfo> ret = new ArrayList<>();
        final HashSet<String> seenKeys = new HashSet<>();

        // Check existing shortcuts shortcuts
        Log.d(TAG, "getDynamicShortcuts size: " + mShortcutManager.getDynamicShortcuts().size());
        for (ShortcutInfo shortcut : mShortcutManager.getDynamicShortcuts()) {
            Log.d(TAG, "dynamic shortcuts id: " + shortcut.getId());
            if (!shortcut.isImmutable()) {
                ret.add(shortcut);
                seenKeys.add(shortcut.getId());
            }
        }
        Log.d(TAG, "getPinnedShortcuts size: " + mShortcutManager.getPinnedShortcuts().size());
        for (ShortcutInfo shortcut : mShortcutManager.getPinnedShortcuts()) {
            Log.d(TAG, "pinned shortcuts id: " + shortcut.getId());
            if (!shortcut.isImmutable() && !seenKeys.contains(shortcut.getId())) {
                ret.add(shortcut);
                seenKeys.add(shortcut.getId());
            }
        }
        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private ShortcutInfo createShortcutForUrl(String urlAsString) {
        Log.i(TAG, "createShortcutForUrl: " + urlAsString);

        final ShortcutInfo.Builder b = new ShortcutInfo.Builder(mContext, urlAsString);

        final Uri uri = Uri.parse(urlAsString);
        b.setIntent(new Intent(Intent.ACTION_VIEW, uri));

        setSiteInformation(b, uri);

        return b.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private ShortcutInfo.Builder setSiteInformation(ShortcutInfo.Builder b, Uri uri) {
        if (uri == null) return b;
        b.setShortLabel(uri.getHost());
        b.setLongLabel(uri.toString());

        Bitmap bmp = fetchFavicon(uri);
        if (bmp != null) {
            b.setIcon(Icon.createWithBitmap(bmp));
        } else {
            b.setIcon(Icon.createWithResource(mContext, R.drawable.link));
        }

        return b;
    }


    private String normalizeUrl(String urlAsString) {
        if (urlAsString.startsWith("http://") || urlAsString.startsWith("https://")) {
            return urlAsString;
        } else {
            return "http://" + urlAsString;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void addWebSiteShortcut(String urlAsString) {
        final String uriFinal = urlAsString;
        callShortcutManager(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                final ShortcutInfo shortcut = createShortcutForUrl(normalizeUrl(uriFinal));
                return mShortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void removeShortcut(ShortcutInfo shortcut) {
        mShortcutManager.removeDynamicShortcuts(Arrays.asList(shortcut.getId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void disableShortcut(ShortcutInfo shortcut) {
        mShortcutManager.disableShortcuts(Arrays.asList(shortcut.getId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void enableShortcut(ShortcutInfo shortcut) {
        mShortcutManager.enableShortcuts(Arrays.asList(shortcut.getId()));
    }

    private Bitmap fetchFavicon(Uri uri) {
        final Uri iconUri = uri.buildUpon().path("img/avatar.png").build();

        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(iconUri.toString()).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            return BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isDeviceSupportShortcuts() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1;
    }
}
