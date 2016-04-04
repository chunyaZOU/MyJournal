package com.example.h2601864.myjournal.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.adapter.JournalAdapter;
import com.example.h2601864.myjournal.utilities.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private SwipeRefreshLayout srlRefresh;
    private ListView lvJournal;
    private LinearLayout llParent;
    private SimpleAdapter adapter;
    private JournalAdapter adapter1;
    private DBManager dbManager;
    private CursorAdapter cursorAdapter;

    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);
        getViews();
    }


    private void getViews() {

        //generateTestData();
        llParent = (LinearLayout) findViewById(R.id.llParent);
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
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
                }, 6000);
            }
        });
        lvJournal = (ListView) findViewById(R.id.lvJournal);
        lvJournal.setOnItemClickListener(this);
        lvJournal.setOnItemLongClickListener(this);
        Cursor cursor = dbManager.queryJournal("", "", "");
        initListView(cursor);
    }

    private void initListView(Cursor cursor) {
        List<HashMap<String, String>> lists = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String time = cursor.getString(cursor.getColumnIndex("createtime"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(time));
            } catch (ParseException e) {
            }
            String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
            time = time + week;
            map.put("id", id);
            map.put("title", title);
            map.put("content", content);
            map.put("path", path);
            map.put("month", time.substring(5, 7));
            map.put("day", time.substring(8, 10));
            map.put("week", time.substring(10));
            lists.add(map);
        }
        if (lists.size() == 0) {
            Toast.makeText(MainActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
        } else {
            adapter1 = new JournalAdapter(MainActivity.this, lists);
            lvJournal.setAdapter(adapter1);
            /*adapter = new SimpleAdapter(this, lists, R.layout.lv_item_layout, new String[]{"id", "title", "content", "path", "month", "day"},
                    new int[]{R.id.tvId, R.id.tvTitle, R.id.tvContent, R.id.tvPath, R.id.tvMonth, R.id.tvDay});
            lvJournal.setAdapter(adapter);*/
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 0, 0, "add").setTitle("Add").setIcon(R.drawable.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 0, "search").setTitle("Search").setIcon(R.drawable.search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 2, 0, "date").setTitle("Share").setIcon(R.drawable.share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
                EditAct.actionStart(MainActivity.this, "", "", "", "", "");
                break;
            case 1:
                Rect outRect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.pw_search_layout, null);
                popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(R.style.anim_style);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x88424242));
                popupWindow.setContentView(view);
                popupWindow.showAtLocation(llParent, Gravity.TOP, 0, outRect.top + getSupportActionBar().getHeight());
                final EditText etSearchContent = (EditText) view.findViewById(R.id.etSearch);
                Button btnOk = (Button) view.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Cursor cursor = dbManager.queryJournal("", etSearchContent.getText().toString().trim(), "");
                        initListView(cursor);
                    }
                });
                //EditAct.actionStart(MainActivity.this, "", "", "", "", "");
                break;
            case 2:
                //EditAct.actionStart(MainActivity.this, "", "", "", "", "");
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
        String remark = ((TextView) view.findViewById(R.id.tvPath)).getText().toString().trim();
        EditAct.actionStart(MainActivity.this, id, title, content, path, remark);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                Cursor cursor = dbManager.queryJournal("", "", "");
                initListView(cursor);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg0) {
        String id = ((TextView) view.findViewById(R.id.tvId)).getText().toString().trim();
        showAlert(id);
        return false;
    }

    private void showAlert(final String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ?");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dbManager.deleteJournal(id);
                Cursor cursor = dbManager.queryJournal("", "", "");
                initListView(cursor);
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
}