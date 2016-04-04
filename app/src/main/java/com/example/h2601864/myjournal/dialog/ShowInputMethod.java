package com.example.h2601864.myjournal.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;


public class ShowInputMethod {
	/**
	 * 自动显示软键盘
	 * @param context
	 * @param view
	 */
	public static void showInputMethod(final Context context,final View view){
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				InputMethodManager imm=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				//if (imm.isActive()) {
					//imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
				//}
			}
		}, 300);
	}
	/**
	 * 隐藏软键盘
	 * @param act,view
	 */
	public static void closeInputMethod(final Activity act, final View view) {

        InputMethodManager imm=(InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && act.getCurrentFocus() != null) {
			if (act.getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
}
