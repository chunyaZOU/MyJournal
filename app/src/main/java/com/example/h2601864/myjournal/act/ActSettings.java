package com.example.h2601864.myjournal.act;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.custom.CustomActionBar;

public class ActSettings extends Activity {

    private static CheckBoxPreference cbOpenNotification;
    private static ListPreference listPreference;
    private static TimePickerDialog timeDialog;
    private static SharedPreferences sharedPreferences;
    private static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTheme();
        setContentView(R.layout.act_settings_layout2);
        setActionBarLayout();
        c = this;
        initFragment();
    }


    private void setCustomTheme() {
        if (BaseActivity.themeValue.equals("blue")) {
            setTheme(R.style.ThemeActBarCustomBlue);
        } else {
            setTheme(R.style.ThemeACtBarCustomGray);
        }
    }

    private void setActionBarLayout() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            CustomActionBar.setCustomActionBar(this, actionBar, getString(R.string.action_settings), R.layout.custom_actionbar_layout);
        }
    }

    private void initFragment() {
        getFragmentManager().beginTransaction().replace(R.id.content, new SettingFragment()).commit();
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        private String themeValue;
        private int hour,mi;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            getPrefer();
        }

        private void getPrefer() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
            boolean isCheck = sharedPreferences.getBoolean(getString(R.string.pref_key_notifications), true);
            cbOpenNotification = (CheckBoxPreference) findPreference(getResources().getString(R.string.pref_key_notifications));
            cbOpenNotification.setOnPreferenceChangeListener(this);
            if (isCheck) {
                cbOpenNotification.setSummary(getNoficationTime());
            } else {
                cbOpenNotification.setSummary("关闭");
            }

           /* listPreference = (ListPreference) findPreference("theme_list");
            listPreference.setOnPreferenceChangeListener(this);
            listPreference.setSummary(listPreference.getEntry());
            themeValue= (String) listPreference.getEntry();*/
        }

        private String getNoficationTime() {
            String saveTime = sharedPreferences.getString("timeNotification", "20:00");
            hour=Integer.parseInt(saveTime.substring(0,saveTime.indexOf(":")));
            String minute = saveTime.substring(saveTime.indexOf(":") + 1);
            mi=Integer.parseInt(minute);
            minute= minute.length() > 1 ? minute : "0" + minute;
            saveTime = saveTime.substring(0, saveTime.indexOf(":") + 1) + minute;
            return saveTime;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            if (preference.getKey().equals(getResources().getString(R.string.pref_key_notifications))) {

                if (cbOpenNotification.isChecked()) {
                    cbOpenNotification.setChecked(false);
                    cbOpenNotification.setSummary("关闭");
                } else {
                    setNoficationTime();
                }
                return true;
            }
            if (preference.getKey().equals("theme_list")) {
                ListPreference listPreference1 = (ListPreference) preference;
                CharSequence[] entries = listPreference1.getEntries();
                int index = listPreference1.findIndexOfValue((String) newValue);
                listPreference.setSummary(entries[index]);
                if(!themeValue.equals(entries[index])){
                    ((Activity) c).finish();
                    c.startActivity(new Intent(c, c.getClass()));
                }
                return true;
            }
            return false;
        }



        private void setNoficationTime() {
            timeDialog = new TimePickerDialog(c, timeSetListener, hour, mi, true);
            timeDialog.setTitle(null);
            timeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (cbOpenNotification.getSummary().equals("关闭")) {
                        cbOpenNotification.setChecked(false);
                    }
                }
            });
            timeDialog.setCanceledOnTouchOutside(true);
            timeDialog.setCancelable(true);
            timeDialog.show();
        }

        private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker picker, int hourOfDay, int minute) {
                cbOpenNotification.setChecked(true);
                String mi = String.valueOf(minute);
                mi = mi.length() > 1 ? mi : "0" + mi;
                cbOpenNotification.setSummary(hourOfDay + ":" + mi);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("timeNotification", hourOfDay + ":" + minute);
                editor.commit();
                if (timeDialog != null && timeDialog.isShowing()) {
                    timeDialog.dismiss();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ActMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.act_enter, R.anim.act_leave);
    }
}