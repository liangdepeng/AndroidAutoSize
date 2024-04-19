package com.dpjh.mytestapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * xxxx
 * Date: 2024/4/17 17:27
 * Author: liangdp
 */
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        AutoSizeUtil.adaptDesign(this,360,720);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_test);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
        systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
