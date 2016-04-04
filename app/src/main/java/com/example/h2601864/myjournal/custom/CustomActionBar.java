package com.example.h2601864.myjournal.custom;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.h2601864.myjournal.R;

/**
 * Created by H2601864 on 2015/11/11.
 */
public class CustomActionBar{

    private static TextView tvTitle;
    //private static ImageView imgBack;

    /**
     * 自定义ActionBar
     * @param context
     * @param actionBar
     * @param titleName
     * @param layout
     */
    public static void setCustomActionBar(final Context context,ActionBar actionBar,String titleName,int layout){

        actionBar.setDisplayShowHomeEnabled(false);//隐藏log和icon
        actionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(context).inflate(layout,null);
        tvTitle=(TextView)view.findViewById(R.id.tvTitle);
        tvTitle.setText(titleName);
       /* imgBack=(ImageView)view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });*/
        ActionBar.LayoutParams layoutParams=new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(view, layoutParams);
    }
}
