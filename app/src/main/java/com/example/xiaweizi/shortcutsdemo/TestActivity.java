package com.example.xiaweizi.shortcutsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initData();
    }

    private void initData() {
        if (getIntent() != null) {
            String value = getIntent().getStringExtra("key");
            if (!TextUtils.isEmpty(value)) {
                TextView tvTest = findViewById(R.id.tv_test);
                tvTest.setText(value);
            }
        }
    }
}
