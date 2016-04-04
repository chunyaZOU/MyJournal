package com.example.h2601864.myjournal.act;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.custom.CustomActionBar;
import com.example.h2601864.myjournal.dialog.ShowInputMethod;
import com.example.h2601864.myjournal.utilities.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActEdit extends BaseActivity implements View.OnLongClickListener{

    private LinearLayout llTv,llEt;
    private TextView tvTime,tvTitle1,tvContent;
    private EditText etTitle,etContent;
    private DBManager dbManager;
    private String id;
    private Date date;
    private SimpleDateFormat sdf;
    private InputMethodManager imm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomTheme();
        setContentView(R.layout.act_edit_layout);
        dbManager=new DBManager(this);
        setActionBarLayout();
        getViews();
        getDataFromActMain();
    }

    private void setCustomTheme(){
        if(BaseActivity.themeValue.equals("blue")){
            setTheme(R.style.ThemeActBarCustomBlue);
        }else{
            setTheme(R.style.ThemeACtBarCustomGray);
        }
    }

    private void setActionBarLayout(){
        ActionBar actionBar=getActionBar();
        if(actionBar!=null){
            CustomActionBar.setCustomActionBar(this, actionBar, getString(R.string.title_act_edit), R.layout.custom_actionbar_layout);
        }
    }

    private void getDataFromActMain(){
        imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        String path=intent.getStringExtra("path");
        String time=intent.getStringExtra("time");
        if(TextUtils.isEmpty(id)){
            llEt.setVisibility(View.VISIBLE);
            llTv.setVisibility(View.GONE);
            clearEditText();
        }else{
            llEt.setVisibility(View.GONE);
            llTv.setVisibility(View.VISIBLE);
            time=time.substring(1).replace("-","/");
            tvTime.setText(time);
            tvTitle1.setText(title);
            tvContent.setText(content);
            //让TextView可滚动，布局文件设置属性android:scrollbars="vertical"
            tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
    }

    private void getViews(){
        sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date=new Date();
        tvTime=(TextView)findViewById(R.id.tvTime);
        tvTime.setText(sdf.format(date).replace("-","/"));
        tvTitle1=(TextView)findViewById(R.id.tvTitle1);
        tvTitle1.setOnLongClickListener(this);
        tvContent=(TextView)findViewById(R.id.tvContent);
        tvContent.setOnLongClickListener(this);
        etTitle=(EditText)findViewById(R.id.etTitle);
        etTitle.setOnLongClickListener(this);
        etContent=(EditText)findViewById(R.id.etContent);
        etContent.setOnLongClickListener(this);
        llTv=(LinearLayout)findViewById(R.id.llTv);
        llEt=(LinearLayout)findViewById(R.id.llEt);
    }

    private void addjournal(){

        String title=etTitle.getText().toString().trim();
        String content=etContent.getText().toString().trim();
        if (title!=null&&!title.equals("")&&content!=null&&!content.equals("")){
            if(id!=null&&!id.equals("")){
                long updateFlag=dbManager.updateJournal(id,title,content,"",sdf.format(date));
                if (updateFlag==1){
                    showAlert();
                }
            }else{
                long addFlag=dbManager.addJournal(title, content, "", sdf.format(date));
                if (addFlag!=-1){
                    showAlert();
                }
            }
        }else{
            Toast.makeText(ActEdit.this,"Fill title & content",Toast.LENGTH_SHORT).show();
        }

    }
    private void showAlert(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(ActEdit.this);
        builder.setTitle("Save Successful");
        builder.setPositiveButton("Another",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id="";
                etTitle.requestFocus();
                clearEditText();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Return",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setResult(1);
                //finish();
                Intent intent=new Intent(ActEdit.this,ActMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.create();
        builder.show();
    }

    private void clearEditText(){
        etTitle.setText("");
        etContent.setText("");
        ShowInputMethod.showInputMethod(ActEdit.this,etTitle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ActEdit.this,ActMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.act_enter,R.anim.act_leave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0,0,0,"Done").setTitle("Done").setIcon(R.drawable.finish).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
            addjournal();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context,String id,String title,String content,String path,String time){
        Intent intent=new Intent(context,ActEdit.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("path",path);
        intent.putExtra("time",time);
        ((ActMain)context).startActivityForResult(intent, 1);
    }

    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case R.id.tvTitle1:
                showEditText();
                etTitle.requestFocus();
                etTitle.setSelection(etTitle.getText().length());
                imm.showSoftInput(etTitle,InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.tvContent:
                showEditText();
                etContent.requestFocus();
                etContent.setSelection(etContent.getText().length());
                imm.showSoftInput(etContent,InputMethodManager.SHOW_IMPLICIT);
                break;
        }
        return true;
    }

    private void showEditText(){
        llEt.setVisibility(View.VISIBLE);
        llTv.setVisibility(View.GONE);
        etTitle.setText(tvTitle1.getText().toString().trim());
        etContent.setText(tvContent.getText().toString().trim());
        Toast.makeText(this,"现在可编辑",Toast.LENGTH_SHORT).show();
    }
}