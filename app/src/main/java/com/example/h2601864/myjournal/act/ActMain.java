package com.example.h2601864.myjournal.act;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.adapter.JournalAdapter;
import com.example.h2601864.myjournal.custom.CustomActionBar;
import com.example.h2601864.myjournal.dialog.ShowInputMethod;
import com.example.h2601864.myjournal.receiver.AlertReceiver;
import com.example.h2601864.myjournal.utilities.CommonUtil;
import com.example.h2601864.myjournal.utilities.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class ActMain extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener, JournalAdapter.GetData {

    //private SwipeRefreshLayout srlRefresh;
    private SwipeMenuListView lvJournal;
    private LinearLayout llNoData;
    private TextView tvNoData;
    private EditText etSearch;
    private ImageView imgClear;
    private LinearLayout llParent;
    private JournalAdapter adapter1;
    private DBManager dbManager;
    private PopupWindow popupWindow;
    private List<HashMap<String, String>> lists;
    private SharedPreferences sharedPreferences;

    private Timer timer;
    private boolean isExit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTheme();
        setContentView(R.layout.act_main_layout);
        dbManager = new DBManager(this);
        setActionBarLayout();
        getViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNotification();
    }

    /**
     * 通知
     */
    private void setNotification() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isCheck = sharedPreferences.getBoolean(getString(R.string.pref_key_notifications), true);
        if (isCheck) {
            String timeNotification = sharedPreferences.getString("timeNotification", "");
            if (!timeNotification.equals("")) {
                setNotification(timeNotification);
            }
        }
    }

    private void setNotification(String timeNotification) {
        int hour = Integer.parseInt(timeNotification.substring(0, timeNotification.indexOf(":")));
        int minute = Integer.parseInt(timeNotification.substring(timeNotification.indexOf(":") + 1));
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long sysTime = System.currentTimeMillis();
        long notificationTime = calendar.getTimeInMillis();
        if (sysTime > notificationTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            notificationTime = calendar.getTimeInMillis();
        }
        long firstTime = SystemClock.elapsedRealtime();
        long timeCha = notificationTime - sysTime;
        firstTime += timeCha;
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 24 * 60 * 60 * 1000, pIntent);
    }

    private void setActionBarLayout() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            CustomActionBar.setCustomActionBar(this, actionBar, getString(R.string.app_name), R.layout.custom_actionbar_layout);
        }
    }

    /**
     * 设置自定义ActionBar主题
     */
    private void setCustomTheme(){
        if(BaseActivity.themeValue.equals("blue")){
            setTheme(R.style.ThemeActBarCustomBlue);
        }else{
            setTheme(R.style.ThemeACtBarCustomGray);
        }
    }

    private void getViews() {

        llParent = (LinearLayout) findViewById(R.id.llParent);
        tvNoData=(TextView)findViewById(R.id.tvNoData);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        llNoData.setOnClickListener(this);
       /* srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        //srlRefresh.setColorSchemeColors(android.R.color.holo_red_light,android.R.color.holo_blue_light,android.R.color.holo_orange_light,android.R.color.darker_gray);
        srlRefresh.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.darker_gray);
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor = dbManager.queryJournal("", "", "");
                        initListView(cursor);
                        srlRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });*/
        lvJournal = (SwipeMenuListView) findViewById(R.id.lvJournal);
        lvJournal.setOnItemClickListener(this);
        //View view =LayoutInflater.from(this).inflate(R.layout.lv_search_layout,null);
        //lvJournal.addHeaderView(view);
        etSearch=(EditText)findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override//输入监听
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchStr=etSearch.getText().toString().trim();
                initListView(dbManager.queryJournal("", searchStr, ""));
                if(searchStr.length()==0){
                    ShowInputMethod.closeInputMethod(ActMain.this,etSearch);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        imgClear=(ImageView)findViewById(R.id.imgClear);
        imgClear.setOnClickListener(this);
        initListView(dbManager.queryJournal("", "", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llNoData:
                etSearch.setText("");
                tvNoData.setText(R.string.no_data);
                initListView(dbManager.queryJournal("", "", ""));
                break;
            case R.id.imgClear:
                etSearch.setText("");
                initListView(dbManager.queryJournal("", "", ""));
                ShowInputMethod.closeInputMethod(ActMain.this,etSearch);
                break;
        }
    }

    /**
     * 显示数据
     * @param cursor
     */
    private void initListView(Cursor cursor) {
        lists = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String time = cursor.getString(cursor.getColumnIndex("createTime"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(time));
            } catch (ParseException e) {
            }
            String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
            time = week + time;
            map.put("id", id);
            map.put("title", title);
            map.put("content", content);
            map.put("path", path);
            map.put("time",time);

            lists.add(map);
        }
        adapter1 = new JournalAdapter(ActMain.this, lists);
        if (lists.size() == 0) {
            //Toast.makeText(MainActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
            //adapter1.notifyDataSetChanged();
            lvJournal.setAdapter(adapter1);
            //lvJournal.invalidate();
            llNoData.setVisibility(View.VISIBLE);
            lvJournal.setEmptyView(llNoData);
        } else {
            llNoData.setVisibility(View.GONE);
            lvJournal.setAdapter(adapter1);
            /*adapter = new SimpleAdapter(this, lists, R.layout.lv_item_layout, new String[]{"id", "title", "content", "path", "month", "day"},
                    new int[]{R.id.tvId, R.id.tvTitle, R.id.tvContent, R.id.tvPath, R.id.tvMonth, R.id.tvDay});
            lvJournal.setAdapter(adapter);*/
        }
        createSwipeMenu();
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                delItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                delItem.setWidth(CommonUtil.dp2px(ActMain.this, 90));
                //delItem.setTitle("Delete");
                //delItem.setTitleSize(18);
                delItem.setIcon(R.drawable.ic_delete);
                //delItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(delItem);
                SwipeMenuItem shareItem = new SwipeMenuItem(getApplicationContext());
                shareItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                shareItem.setWidth(CommonUtil.dp2px(ActMain.this,90));
                //shareItem.setTitle("Share");
                shareItem.setIcon(R.drawable.ic_action_share);
                //delItem.setTitleSize(18);
                //shareItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(shareItem);
            }
        };
        lvJournal.setMenuCreator(creator);
        lvJournal.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                Map<String, String> item = lists.get(position);
                switch (index) {
                    case 0:
                        showAlert(item.get("id"), position);
                        break;
                    case 1:
                        showShare();
                        break;
                }
            }
        });
        lvJournal.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {}

            @Override
            public void onSwipeEnd(int position) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 0, 0, "add").setTitle("Add").setIcon(R.drawable.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //menu.add(0, 1, 0, "search").setTitle("Search").setIcon(R.drawable.search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SubMenu moreMenu = menu.addSubMenu(0, 10, 0, "more");
        MenuItem item = moreMenu.getItem();
        item.setIcon(R.drawable.more);
        item.setTitle("More");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        moreMenu.add(0, 2, 0, "Settings").setTitle("Settings");
        moreMenu.add(0, 3, 0, "About").setTitle("About");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings: //return true;
                break;
            case 0:
                tvNoData.setText(R.string.no_data);
                ActEdit.actionStart(ActMain.this, "", "", "", "", "");
                break;
            case 1:
                //tvSearch.setVisibility(View.VISIBLE);
                tvNoData.setText(R.string.no_data);
                Rect outRect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                //outRect.top状态栏的高度
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.pw_search_layout, null);
                popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(R.style.anim_style);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
                popupWindow.setContentView(view);
                popupWindow.showAtLocation(llParent, Gravity.TOP, 0, outRect.top + getActionBar().getHeight());
                final EditText etSearchContent = (EditText) view.findViewById(R.id.etSearch);
                TextView tvOk = (TextView) view.findViewById(R.id.btnOk);
                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        initListView(dbManager.queryJournal("", etSearchContent.getText().toString().trim(), ""));
                    }
                });
                ShowInputMethod.showInputMethod(ActMain.this, etSearchContent);
                //EditAct.actionStart(MainActivity.this, "", "", "", "", "");
                break;
            case 2:
                startActivity(new Intent(ActMain.this, ActSettings.class));
                break;
            case 3:
                startActivity(new Intent(ActMain.this, ActAbout.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg0) {
        String id = ((TextView) view.findViewById(R.id.tvId)).getText().toString().trim();
        String title = ((TextView) view.findViewById(R.id.tvTitle)).getText().toString().trim();
        String content = ((TextView) view.findViewById(R.id.tvContent)).getText().toString().trim();
        String path = ((TextView) view.findViewById(R.id.tvPath)).getText().toString().trim();
        String time = ((TextView) view.findViewById(R.id.tvTime)).getText().toString().trim();
        ActEdit.actionStart(ActMain.this, id, title, content, path, time);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                ShowInputMethod.closeInputMethod(ActMain.this, null);
                initListView(dbManager.queryJournal("", "", ""));
                break;
        }
    }

    /**
     * 长按分享到其他社交平台
     */
    private void showShare() {
    }

    /**
     * 删除
     *
     * @param id
     */
    private void showAlert(final String id, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ?");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //adapter1.notifyDataSetChanged();
                lists.remove(position);
                dbManager.deleteJournal(id);
                initListView(dbManager.queryJournal("", "", ""));
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void getDataFromMonth(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            tvNoData.setText(R.string.no_data_this_month);
        }
        initListView(cursor);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (!isExit) {
                isExit = true;
                Toast.makeText(ActMain.this, "再点一次退出", Toast.LENGTH_SHORT).show();
                timer=new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        // 延迟两秒后，isExit才被赋值false
                        isExit = false;
                    }
                }, 2000);
            } else {
                finish();
                System.exit(0);
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}