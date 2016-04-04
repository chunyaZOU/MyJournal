package com.example.h2601864.myjournal.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.utilities.DBManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by H2601864 on 2015/10/6.
 */
public class JournalAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private List<HashMap<String, String>> lists;
    private Holder holder;
    private PopupWindow pwMonths;

    public JournalAdapter(Context context, List<HashMap<String, String>> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_layout, null);
            holder.llDate=(LinearLayout)convertView.findViewById(R.id.llDate);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
            holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            holder.tvPath = (TextView) convertView.findViewById(R.id.tvPath);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }

        holder.tvId.setText(lists.get(position).get("id"));
        holder.tvTitle.setText(lists.get(position).get("title"));
        holder.tvContent.setText(lists.get(position).get("content"));
        holder.tvDay.setText(lists.get(position).get("time").substring(9,11));
        holder.tvMonth.setText(lists.get(position).get("time").substring(6,8));
        holder.tvPath.setText(lists.get(position).get("path"));
        holder.tvTime.setText(lists.get(position).get("time"));
        String week=lists.get(position).get("time").substring(0,1);
        //holder.llDate.setOnClickListener(this);
        if (week.equals("1")||week.equals("7")){
            //holder.llDate.setBackground(context.getResources().getDrawable(R.drawable.red_date_bg));
            holder.llDate.setBackgroundResource(R.drawable.red_bg);
        }else{
            //holder.llDate.setBackground(context.getResources().getDrawable(R.drawable.blue_date_bg));
            holder.llDate.setBackgroundResource(R.drawable.light_green_month_bg);
        }
        return convertView;
    }

    class Holder {
        private LinearLayout llDate;
        private TextView tvDay, tvMonth, tvTitle, tvContent, tvId, tvPath,tvTime;
    }


    private TextView tvJanuary,tvFebruary,tvMarch,tvApril,tvMay,tvJune,tvJuly,tvAugust,tvSeptember,tvOctober,tvNovember,tvDecember,tvSmile,tvAll;

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.llDate:
                View view =LayoutInflater.from(context).inflate(R.layout.pw_months_layout,null);
                showPopupWindow(v,view);
                getViews(view);
                break;
            case R.id.tvJanuary:
                sendDataToAct("01");
                break;
            case R.id.tvFebruary:
                sendDataToAct("02");
                break;
            case R.id.tvMarch:
                sendDataToAct("03");
                break;
            case R.id.tvApril:
                sendDataToAct("04");
                break;
            case R.id.tvMay:
                sendDataToAct("05");
                break;
            case R.id.tvJune:
                sendDataToAct("06");
                break;
            case R.id.tvJuly:
                sendDataToAct("07");
                break;
            case R.id.tvAugust:
                sendDataToAct("08");
                break;
            case R.id.tvSeptember:
                sendDataToAct("09");
                break;
            case R.id.tvOctober:
                sendDataToAct("10");
                break;
            case R.id.tvNovember:
                sendDataToAct("11");
                break;
            case R.id.tvDecember:
                sendDataToAct("12");
                break;
            case R.id.tvSmile:
                Toast.makeText(context,"点了没反应啊哦",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvAll:
                ((GetData)context).getDataFromMonth(new DBManager(context).queryJournal("","",""));
                if(pwMonths.isShowing()){
                    pwMonths.dismiss();
                }
                break;
        }
    }

    private void sendDataToAct(String month){
        Cursor cursor=new DBManager(context).queryJournalByMonth(month);
        ((GetData)context).getDataFromMonth(cursor);
        if(pwMonths.isShowing()){
            pwMonths.dismiss();
        }
        cursor.close();
    }

    private void showPopupWindow(View v,View view){
        pwMonths = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
        pwMonths.setOutsideTouchable(true);
        pwMonths.setAnimationStyle(R.style.anim_style);
        pwMonths.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        pwMonths.setContentView(view);
        pwMonths.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private void getViews(View view){
        tvJanuary=(TextView)view.findViewById(R.id.tvJanuary);
        tvJanuary.setOnClickListener(this);
        tvFebruary=(TextView)view.findViewById(R.id.tvFebruary);
        tvFebruary.setOnClickListener(this);
        tvMarch=(TextView)view.findViewById(R.id.tvMarch);
        tvMarch.setOnClickListener(this);
        tvApril=(TextView)view.findViewById(R.id.tvApril);
        tvApril.setOnClickListener(this);
        tvMay=(TextView)view.findViewById(R.id.tvMay);
        tvMay.setOnClickListener(this);
        tvJune=(TextView)view.findViewById(R.id.tvJune);
        tvJune.setOnClickListener(this);
        tvJuly=(TextView)view.findViewById(R.id.tvJuly);
        tvJuly.setOnClickListener(this);
        tvAugust=(TextView)view.findViewById(R.id.tvAugust);
        tvAugust.setOnClickListener(this);
        tvSeptember=(TextView)view.findViewById(R.id.tvSeptember);
        tvSeptember.setOnClickListener(this);
        tvOctober=(TextView)view.findViewById(R.id.tvOctober);
        tvOctober.setOnClickListener(this);
        tvNovember=(TextView)view.findViewById(R.id.tvNovember);
        tvNovember.setOnClickListener(this);
        tvDecember=(TextView)view.findViewById(R.id.tvDecember);
        tvDecember.setOnClickListener(this);
        tvSmile=(TextView)view.findViewById(R.id.tvSmile);
        tvSmile.setOnClickListener(this);
        tvAll=(TextView)view.findViewById(R.id.tvAll);
        tvAll.setOnClickListener(this);
    }

    public interface GetData{
        public void getDataFromMonth(Cursor cursor);
    }
}
