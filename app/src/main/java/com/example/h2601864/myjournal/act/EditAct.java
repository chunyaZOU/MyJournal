package com.example.h2601864.myjournal.act;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.utilities.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditAct extends ActionBarActivity {

    private EditText etTitle,etContent,etRemark;
    private DBManager dbManager;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dbManager=new DBManager(this);
        getViews();
        getDataFromMainActivity();
    }

    private void getDataFromMainActivity(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        String path=intent.getStringExtra("path");
        String remark=intent.getStringExtra("remark");
        if (id==null||id.equals("")){
            clearEditText();
        }else{
            etTitle.setText(title);
            etContent.setText(content);
            etRemark.setText(remark);
        }
    }

    private void getViews(){
        etTitle=(EditText)findViewById(R.id.etTitle);
        etContent=(EditText)findViewById(R.id.etContent);
        etRemark=(EditText)findViewById(R.id.etRemark);

    }

    private void addjournal(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        String title=etTitle.getText().toString().trim();
        String content=etContent.getText().toString().trim();
        if (title!=null&&!title.equals("")&&content!=null&&!content.equals("")){
            if(id!=null&&!id.equals("")){
                long updateFlag=dbManager.updateJournal(id,title,content,"",sdf.format(new Date()));
                if (updateFlag==1){
                    showAlert();
                }
            }else{
                long addFlag=dbManager.addJournal(title, content, "", sdf.format(new Date()));
                if (addFlag!=-1){
                    showAlert();
                }
            }
        }else{
            Toast.makeText(EditAct.this,"Fill title & content",Toast.LENGTH_SHORT).show();
        }

    }
    private void showAlert(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(EditAct.this);
        builder.setTitle("Save Successful");
        builder.setPositiveButton("Write Another",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id="";
                clearEditText();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Return",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setResult(1);
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    private void clearEditText(){
        etTitle.setText("");
        etContent.setText("");
        etRemark.setText("");
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

    public static void actionStart(Context context,String id,String title,String content,String path,String remark){
        Intent intent=new Intent(context,EditAct.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("path",path);
        intent.putExtra("remark",remark);
        ((MainActivity)context).startActivityForResult(intent, 1);
    }
}
