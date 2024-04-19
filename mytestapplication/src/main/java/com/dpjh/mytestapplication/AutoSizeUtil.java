package com.dpjh.mytestapplication;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;


/**
 * 按照设计图适配不同分辨率的屏幕 【简易版】无业务代码入侵，轻量级修改
 * 感谢->【https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA】
 * 通过更改 displayMetrics.density 的值来实现适配UI设计图，在不同设备上展现相同的效果
 * 手动调用控制适配
 * Date: 2024/4/18 9:47
 * Author: liangdp
 */
public class AutoSizeUtil {

    private static String TAG = "AutoSizeUtil";
    private static boolean isInit = false;
    private static float initDensity;
    private static int initDensityDpi;
    private static float initScaledDensity;
    private static final HashMap<Activity, ConfigChangeListener> listenerMap = new HashMap<>(4);
    private static final HashMap<Activity, ComponentCallbacks> componentCallbacksMap = new HashMap<>(4);

    /**
     * 屏幕按照设计图适配 为了保持设计图比例，最终仅会按照设计图给定的宽或者高选择一个来进行比例适配
     * 调用时机要在 setcontentview之前
     *
     * @param designShortSideDp 短边设计图dp值
     * @param designLongSideDp  长边设计图dp值
     */
    public static void adaptDesign(Activity activity, int designShortSideDp, int designLongSideDp) {
        adaptDesign(activity, designShortSideDp, designLongSideDp, null);
    }

    /**
     * 支持横竖屏切换适配 添加 ConfigChangeListener 即可
     *
     * @param designShortSideDp 短边设计图dp值
     * @param designLongSideDp  长边设计图dp值
     * @param changeListener    横竖屏监听 如果添加了横竖屏监听，请在收到回调之后 重新调用 setContentView() 以及重新初始化View 以保证适配
     */
    public static void adaptDesign(Activity activity, int designShortSideDp, int designLongSideDp, ConfigChangeListener changeListener) {
        initParamsSave();

        DisplayMetrics appDisplayMetrics = MyApp.applicationContext.getResources().getDisplayMetrics();
        boolean isVertical = MyApp.applicationContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        Log.e(TAG, "isVertical: " + isVertical + ", 短边: " + designShortSideDp + "dp, 长边: " + designLongSideDp + "dp");
        Log.e(TAG, " 当前屏幕宽 :" + appDisplayMetrics.widthPixels + "   高:" + appDisplayMetrics.heightPixels);

        //------------------------------------------------------------------------------------------
        //默认页面无法滑动 考虑横屏 选择较小的值，需要根据设计图兼顾各种分辨率，以保证适配显示完全
        //如果页面可以横向滑动ing 可以考虑改动只用短边宽度来适配，否则需要考虑是长边先撑满还是短边先撑满
        float tempAdaptWidthValue;
        float tempAdaptHeightValue;
        float targetDensity;
        if (isVertical) {
            tempAdaptWidthValue = appDisplayMetrics.widthPixels * 1f / designShortSideDp;
            targetDensity = tempAdaptWidthValue;
            //竖屏一般都用宽度来适配  除非极其特殊的分辨率，这里不做考虑
            //tempAdaptHeightValue = appDisplayMetrics.heightPixels * 1f / designLongSideDp;
            //targetDensity = Math.min(tempAdaptWidthValue, tempAdaptHeightValue);
        } else {
            tempAdaptWidthValue = appDisplayMetrics.widthPixels * 1f / designLongSideDp;
            tempAdaptHeightValue = appDisplayMetrics.heightPixels * 1f / designShortSideDp;
            targetDensity = Math.min(tempAdaptWidthValue, tempAdaptHeightValue);
        }
        //------------------------------------------------------------------------------------------

        float targetScaledDensity = targetDensity * (initScaledDensity / initDensity);
        int targetDensityDpi = (int) (targetDensity * DisplayMetrics.DENSITY_DEFAULT);

        Log.e(TAG, " targetDensity :" + targetDensity + "   targetScaledDensity:" + targetScaledDensity + "   targetDensityDpi:" + targetDensityDpi);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics actDisplayMetrics = activity.getResources().getDisplayMetrics();
        actDisplayMetrics.density = targetDensity;
        actDisplayMetrics.scaledDensity = targetScaledDensity;
        actDisplayMetrics.densityDpi = targetDensityDpi;

        if (changeListener != null && listenerMap.get(activity) == null) {
            ComponentCallbacks componentCallback = new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (changeListener != null) {
                        // 横竖屏切换重新改动参数适配
                        adaptDesign(activity, designShortSideDp, designLongSideDp, null);
                        // 务必通过该回调 调用 setContentView() 来重新适配，也要重新初始化 View 相当于是重新布局了
                        changeListener.onConfigChange(newConfig);
                    }
                }

                @Override
                public void onLowMemory() {

                }
            };
            activity.registerComponentCallbacks(componentCallback);
            listenerMap.put(activity, changeListener);
            componentCallbacksMap.put(activity, componentCallback);
        }
    }

    /**
     * 还原已修改过的值 如果想要立即生效要重新绘制页面
     */
    public void cancelAdapt(Activity activity) {
        if (activity == null)
            return;

        try {
            DisplayMetrics appDisplayMetrics = MyApp.applicationContext.getResources().getDisplayMetrics();
            appDisplayMetrics.density = initDensity;
            appDisplayMetrics.scaledDensity = initScaledDensity;
            appDisplayMetrics.densityDpi = initDensityDpi;

            DisplayMetrics actDisplayMetrics = activity.getResources().getDisplayMetrics();
            actDisplayMetrics.density = initDensity;
            actDisplayMetrics.scaledDensity = initScaledDensity;
            actDisplayMetrics.densityDpi = initDensityDpi;

            if (componentCallbacksMap.get(activity) != null) {
                ComponentCallbacks componentCallbacks = componentCallbacksMap.get(activity);
                activity.unregisterComponentCallbacks(componentCallbacks);
                componentCallbacksMap.remove(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有页面的适配 还是一样 不能立即生效
     */
    public void cancelAllAdapt() {
        for (Activity activity : listenerMap.keySet()) {
            cancelAdapt(activity);
        }
        listenerMap.clear();
    }

    /**
     * 初始化参数
     */
    private static void initParamsSave() {
        if (!isInit) {
            isInit = true;
            DisplayMetrics displayMetrics = MyApp.applicationContext.getResources().getDisplayMetrics();
            initDensity = displayMetrics.density;
            initDensityDpi = displayMetrics.densityDpi;
            initScaledDensity = displayMetrics.scaledDensity;
            MyApp.applicationContext.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig.fontScale > 0) {
                        initScaledDensity = MyApp.applicationContext.getResources().getDisplayMetrics().scaledDensity;
                    }
                    Log.e(TAG, newConfig.toString());
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
    }


    public interface ConfigChangeListener {
        void onConfigChange(Configuration configuration);
    }

}
