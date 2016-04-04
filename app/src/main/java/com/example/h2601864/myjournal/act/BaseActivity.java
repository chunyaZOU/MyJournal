package com.example.h2601864.myjournal.act;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.h2601864.myjournal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H2601864 on 2015/11/13.
 */
public class BaseActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private static List<Activity> actList;
    public static String themeValue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        themeValue=sharedPreferences.getString("theme_list","blue");
        //setCustomTheme();
        putAct2List();
    }

    /**
     * 没有找到基类设置主题方法，此方法不能使用
     */
    private void setCustomTheme() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeVal = sharedPreferences.getString("theme_list", "blue");
        if (themeVal.equals("blue")) {
            setTheme(R.style.ActionBarStyleDef);
        } else {
            setTheme(R.style.ActionBarStyleGray);
        }
    }

    private void putAct2List() {
        actList = new ArrayList<>();
        if (!actList.contains(this)) {
            actList.add(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actList.remove(this);
    }

    public static void killProcess() {
        for (Activity act : actList) {
            if (act != null && !act.isFinishing()) {
                act.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
