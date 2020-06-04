package com.zhangteng.base.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhangteng.base.R;


/**
 * Created by swing on 2017/11/4.
 */

public class ActivityHelper {


    public static void jumpToActivity(Activity activity, Class cls, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        anim(activity, code);
    }

    public static void jumpToActivityWithBundle(Activity activity, Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        anim(activity, code);
    }

    public static void jumpActivityWithBundle(Activity activity, Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        anim(activity, code);
        activity.finish();
    }

    public static void jumpActivity(Activity activity, Class cls, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        anim(activity, code);
        activity.finish();
    }

    public static void jumpToActivityForParams(Activity activity, Class cls, String key, String value, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtra(key, value);
        activity.startActivity(intent);
        anim(activity, code);
    }

    public static void jumpActivityForParams(Activity activity, Class cls, String key, String value, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtra(key, value);
        activity.startActivity(intent);
        anim(activity, code);
        activity.finish();
    }


    public static void jumpActivityResult(Activity activity, Class cls, int requestCode, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        anim(activity, code);
    }


    public static void anim(Activity activity, int code) {
        if (code == 1) {
            ActivityHelper.setActivityAnimShow(activity);
        } else if (code == 2) {
            ActivityHelper.setActivityAnimClose(activity);
        }
    }


    //跳转进activity时的动画
    public static void setActivityAnimShow(Activity activity) {
        activity.overridePendingTransition(getNextActivityAnimShow(), getCurrentActivityAnimShow());
    }

    //退出activity时的动画
    public static void setActivityAnimClose(Activity activity) {
        activity.overridePendingTransition(getPreviousActivityAnimClose(), getCurrentActivityAnimClose());
    }

    /**
     * 下一个活动进入动画
     */
    public static int getNextActivityAnimShow() {
        return R.anim.activity_show;
    }

    /**
     * 当前活动退出动画（start活动时）
     */
    public static int getCurrentActivityAnimShow() {
        return R.anim.activity_show_1;
    }

    /**
     * 上一个活动进入动画
     */
    public static int getPreviousActivityAnimClose() {
        return R.anim.activity_close_1;
    }

    /**
     * 当前活动退出动画（finish活动时）
     */
    public static int getCurrentActivityAnimClose() {
        return R.anim.activity_close;
    }
}
