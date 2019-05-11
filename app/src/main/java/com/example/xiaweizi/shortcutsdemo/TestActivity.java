package com.example.xiaweizi.shortcutsdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initData();
    }

    private void initData() {
        if (getIntent() != null) {
            TextView tvTest = findViewById(R.id.tv_test);
            String value = getIntent().getStringExtra("key");
            if (!TextUtils.isEmpty(value)) {
                tvTest.setText(value);
            }

            if (getIntent().getData() != null && TextUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW)) {
                Uri uri = getIntent().getData();
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null && pathSegments.size() > 0) {
                    tvTest.setText(pathSegments.get(0));
                }
            }
        }
    }
}
