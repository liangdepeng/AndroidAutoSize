package com.dpjh.mytestapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        AutoSizeUtil.adaptDesign(this, 360, 720, new AutoSizeUtil.ConfigChangeListener() {
//            @Override
//            public void onConfigChange(Configuration configuration) {
//                setContentView(R.layout.activity_main);
//                initView();
//            }
//        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.e("MainActivity","onCreate");

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
        systemUiVisibility =
                systemUiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

//        boolean light = false;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int systemUiVisibility2 = getWindow().getDecorView().getSystemUiVisibility();
//            if (light) { //白色文字
//                systemUiVisibility2 = systemUiVisibility2 & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            } else { //黑色文字
//                systemUiVisibility2 = systemUiVisibility2 | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            }
//            getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility2);
//        }




        initView();

    }

    private void initView() {
        findViewById(R.id.iv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity2.class));
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        ,Manifest.permission.READ_EXTERNAL_STORAGE},111);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},222);
            }
        });

        float density = getResources().getDisplayMetrics().density;
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        ((TextView) findViewById(R.id.text)).setText("density->"+density+ "  \nscaledDensity->"+fontScale+"  \n高 dp->"+getScreenHeightDp(this)+"  \n宽 dp->"+getScreenWidthDp(this));

        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;

        ((TextView) findViewById(R.id.text2)).setText(" 宽-> "+widthPixels+"  \n高-> "+heightPixels);


    }


    //dp转px
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //sp转px
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 获取手机高度是多少dp
    public static int getScreenHeightDp(Context context) {
        return px2dp(context,context.getResources().getDisplayMetrics().heightPixels);
    }

    public static int getScreenWidthDp(Context context) {
        return px2dp(context,context.getResources().getDisplayMetrics().widthPixels);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity","onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainActivity","onRestart");
    }
}