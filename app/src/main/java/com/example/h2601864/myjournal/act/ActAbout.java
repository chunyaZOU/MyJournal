package com.example.h2601864.myjournal.act;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.custom.CustomActionBar;

public class ActAbout extends BaseActivity {

    private String themeValue;
    private LinearLayout llContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTheme();
        setContentView(R.layout.act_about_layout);
        setActionBarLayout();
        getViews();
    }

    private void getViews(){
        llContainer=(LinearLayout)findViewById(R.id.llContainer);
        if(themeValue.equals("blue")){
            llContainer.setBackgroundResource(R.drawable.corner_rect_bg_light_blue);
        }else{
            llContainer.setBackgroundResource(R.drawable.corner_rect_bg_light_gray);
        }
    }

    private void setCustomTheme(){
        themeValue=BaseActivity.themeValue;
        if(themeValue.equals("blue")){
            setTheme(R.style.ThemeActBarCustomBlue);
        }else{
            setTheme(R.style.ThemeACtBarCustomGray);
        }
    }

    private void setActionBarLayout() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            CustomActionBar.setCustomActionBar(this, actionBar, "About", R.layout.custom_actionbar_layout);
        }
    }
}
