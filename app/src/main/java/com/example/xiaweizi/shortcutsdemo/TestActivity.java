package com.example.xiaweizi.shortcutsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

            if (getIntent().getData() != null) {
                String zpParams = getIntent().getData().getEncodedQuery();
                Log.e("xiaweizi", "=zpParams= " + zpParams);
                // 拆分获得单个参数
                if (!TextUtils.isEmpty(zpParams)) {
                    String[] params = zpParams.split("&");
                    for (String param : params) {
                        String[] key_Value = param.split("=");
                        if (key_Value != null && key_Value.length == 2) {
                            Log.e("xiaweizi", "=key= " + key_Value[0] + " =value= " + key_Value[1]);
                        }
                    }
                }
            }
        }
    }
}
