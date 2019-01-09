package com.example.wuqilong.sudoku_game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuqilong.sudoku_game.SuDoKu_class.Sudoku_Topic_Data;
import com.example.wuqilong.sudoku_game.define.MyID;
import com.example.wuqilong.sudoku_game.define.Setting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuDoKu_setting_Activity extends AppCompatActivity {
    Setting setting;
    Setting noSaveSetting;
    boolean change=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_setting);
        init();
    }
    void init(){
        getIntentData();
        recyclerViewInit();//顯示
        buttonInit();
    }
    void getIntentData(){
        Intent intent = getIntent();
        setting=new Setting();
        setting.setDataForBundle(intent.getExtras());
        noSaveSetting=new Setting();
        noSaveSetting.copyFor(setting);
        //noSaveSetting.setDataForBundle(intent.getExtras());
    }
    void recyclerViewInit(){
        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration did=new DividerItemDecoration(recyclerView.getContext(),  layoutManager.getOrientation() );
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(new RecvAdapter(this,noSaveSetting));
    }
    void buttonInit(){
        Button cancel_bt=findViewById(R.id.setting_return_BT);
        Button save_bt=findViewById(R.id.setting_save_BT);
        Button save_and_return_bt=findViewById(R.id.setting_save_and_return_BT);

        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.setting_return_BT:
                        if(!setting.equals(noSaveSetting)){//是否調整過設定?
                            showReturnDialog();
                        }else{//如果設定值與原本相同，則直接返回
                            Intent intent=new Intent(SuDoKu_setting_Activity.this,SuDoKu_main_Activity.class);
                            intent.putExtras(setting.getDataBundle());
                            if(change)
                                setResult(MyID.SETTING_RESULT_CODE_SAVE, intent);
                            else
                                setResult(MyID.SETTING_RESULT_CODE_NO_SAVE, intent); //
                            finish();
                            //startActivity(intent);
                        }
                        break;
                    case R.id.setting_save_BT:
                        if(!setting.equals(noSaveSetting)) {
                            setting.copyFor(noSaveSetting);
                            setting.saveSettingData();
                            change = true;
                            Toast.makeText(SuDoKu_setting_Activity.this, "已儲存設定", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SuDoKu_setting_Activity.this, "沒有變更設定", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.setting_save_and_return_BT:
                        if(!setting.equals(noSaveSetting)){
                            setting.copyFor(noSaveSetting);
                            setting.saveSettingData();
                            change=true;
                            Intent intent=new Intent(SuDoKu_setting_Activity.this,SuDoKu_main_Activity.class);
                            intent.putExtras(setting.getDataBundle());
                            setResult(MyID.SETTING_RESULT_CODE_SAVE, intent); //
                            finish();
                        }else{
                            Intent intent=new Intent(SuDoKu_setting_Activity.this,SuDoKu_main_Activity.class);
                            intent.putExtras(setting.getDataBundle());
                            setResult(MyID.SETTING_RESULT_CODE_NO_SAVE, intent); //
                            finish();
                        }

                        break;
                }
            }
        };
        cancel_bt.setOnClickListener(listener);
        save_bt.setOnClickListener(listener);
        save_and_return_bt.setOnClickListener(listener);
    }
    void showReturnDialog() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("返回主畫面");
        MyAlertDialog.setMessage("設定尚未儲存。\n確定要返回主畫面?");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_NEUTRAL:
                        Intent intent=new Intent(SuDoKu_setting_Activity.this,SuDoKu_main_Activity.class);
                        intent.putExtras(setting.getDataBundle());
                        if(change)
                            setResult(MyID.SETTING_RESULT_CODE_CHANGE, intent);
                        else
                            setResult(MyID.SETTING_RESULT_CODE_NO_SAVE, intent); //
                        finish();
                        break;
                }
            }
        };
        MyAlertDialog.setNeutralButton("不儲存返回",OkClick );
        MyAlertDialog.setNegativeButton("取消",OkClick );
        MyAlertDialog.show();
    }
}



class RecvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private Setting setting;

    //private ArrayList<RecyclerData> data;

    public RecvAdapter(Context con,Setting setting){
        this.context=con;
        this.setting=setting;
        //this.data=data;
    }
    private void setBlockColor(View v){
        v.findViewById(R.id.color_frameLayout).setBackgroundColor(0xff000000);
        v.findViewById(R.id.setting_block_1).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_2).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_3).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_4).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_5).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_6).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_7).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_8).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.setting_block_9).setBackgroundColor(Color.WHITE);
        ((TextView)v.findViewById(R.id.setting_block_1)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_2)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_3)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_4)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_5)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_6)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_7)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_8)).setTextColor(Color.BLACK);
        ((TextView)v.findViewById(R.id.setting_block_9)).setTextColor(Color.BLACK);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        switch (type){
            case 0://作答模式
                View view0= LayoutInflater.from(context).inflate(R.layout.setting_selectmod,parent,false);
                SelectViewHolder viewHolder0=new SelectViewHolder(view0);
                viewHolder0.setTextView((TextView)view0.findViewById(R.id.select_mod_tv));
                return viewHolder0;
            case 1://背景顏色
                View view1= LayoutInflater.from(context).inflate(R.layout.setting_grid_color,parent,false);

                SetColor_12_Holder viewHolder1=new SetColor_12_Holder(view1);
                viewHolder1.setColor1View((TextView) view1.findViewById(R.id.setting_block_1),
                        (TextView)view1.findViewById(R.id.setting_block_3),
                        (TextView)view1.findViewById(R.id.setting_block_5),
                        (TextView)view1.findViewById(R.id.setting_block_7),
                        (TextView)view1.findViewById(R.id.setting_block_9));
                viewHolder1.setColor2View((TextView) view1.findViewById(R.id.setting_block_2),
                        (TextView)view1.findViewById(R.id.setting_block_4),
                        (TextView)view1.findViewById(R.id.setting_block_6),
                        (TextView)view1.findViewById(R.id.setting_block_8));
                viewHolder1.setBT((Button)view1.findViewById(R.id.color1_bt),
                        (Button)view1.findViewById(R.id.color2_bt));

                return viewHolder1;
            case 2:
                View view2= LayoutInflater.from(context).inflate(R.layout.setting_grid_color,parent,false);
                SetFontColor_Holder viewHolder2=new SetFontColor_Holder(view2);
                viewHolder2.setAnserView((TextView)view2.findViewById(R.id.setting_block_5));
                viewHolder2.setTopicView((TextView)view2.findViewById(R.id.setting_block_1),
                        (TextView)view2.findViewById(R.id.setting_block_2),
                        (TextView)view2.findViewById(R.id.setting_block_6));
                viewHolder2.setBT((Button)view2.findViewById(R.id.color1_bt),
                        (Button)view2.findViewById(R.id.color2_bt));

                return viewHolder2;
            case 3:
                View view3= LayoutInflater.from(context).inflate(R.layout.setting_grid_color,parent,false);
                SetChenkColor_Holder viewHolder3=new SetChenkColor_Holder(view3);

                viewHolder3.setChenkView(((TextView)view3.findViewById(R.id.setting_block_5)));
                viewHolder3.addEqualsView(((TextView)view3.findViewById(R.id.setting_block_3)));
                viewHolder3.addEqualsView(((TextView)view3.findViewById(R.id.setting_block_7)));
                viewHolder3.setBT((Button)view3.findViewById(R.id.color1_bt),
                        (Button)view3.findViewById(R.id.color2_bt));

                return viewHolder3;
            case 4:
                View view4= LayoutInflater.from(context).inflate(R.layout.setting_grid_color,parent,false);
                SetErrorColor_Holder viewHolder4=new SetErrorColor_Holder(view4);
                viewHolder4.addErrorBlock( ((TextView)view4.findViewById(R.id.setting_block_3)));
                viewHolder4.addErrorBlock( ((TextView)view4.findViewById(R.id.setting_block_7)));
                viewHolder4.addErrorBlock( ((TextView)view4.findViewById(R.id.setting_block_9)));
                viewHolder4.setBT((Button)view4.findViewById(R.id.color1_bt),
                        (Button)view4.findViewById(R.id.color2_bt));
                return viewHolder4;
            case 5:
                View view5= LayoutInflater.from(context).inflate(R.layout.setting_topic_data,parent,false);
                CheckTopicData_Holder viewHolder5=new CheckTopicData_Holder(view5);
                viewHolder5.setDateTextView((TextView)view5.findViewById(R.id.date_tv),(TextView)view5.findViewById(R.id.new_date_tv));
                viewHolder5.setButton((Button)view5.findViewById(R.id.up_data_bt));
                return viewHolder5;
            default:
        }

        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int postion){
        View view=holder.itemView;
        if(holder instanceof SelectViewHolder){//0
            SelectViewHolder viewHolder=(SelectViewHolder)holder;
            if(setting.getSelectMod()==Setting.SELECTMOD_BLOCK)
                viewHolder.setText(context.getString(R.string.selete_mod_string_block));
            else if(setting.getSelectMod()==Setting.SELECTMOD_NUMBER)
                viewHolder.setText(context.getString(R.string.selete_mod_string_number));
        }else if(holder instanceof SetColor_12_Holder){//1
            SetColor_12_Holder color12Holder=(SetColor_12_Holder)holder;

            ((TextView)view.findViewById(R.id.textView)).setText("背景顏色配置：");
            ((FrameLayout)view.findViewById(R.id.color_frameLayout)).setBackgroundColor(Color.BLACK);

            color12Holder.setColor1(setting.getColor1());
            color12Holder.setColor2(setting.getColor2());
        }else if(holder instanceof SetFontColor_Holder){//2
            SetFontColor_Holder setFontColorHolder=(SetFontColor_Holder) holder;

            ((TextView)view.findViewById(R.id.textView)).setText("文字顏色配置：");
            setBlockColor(view);
            ((TextView)view.findViewById(R.id.setting_block_1)).setText("9");
            ((TextView)view.findViewById(R.id.setting_block_2)).setText("4");
            ((TextView)view.findViewById(R.id.setting_block_5)).setText("8");
            ((TextView)view.findViewById(R.id.setting_block_6)).setText("7");

            setFontColorHolder.setTopicColor(setting.getTopicFontColor());
            setFontColorHolder.setAnserColor(setting.getFontColor());
        }else if(holder instanceof SetChenkColor_Holder){//3
            SetChenkColor_Holder setChenkColorHolder=(SetChenkColor_Holder) holder;
            ((TextView)view.findViewById(R.id.textView)).setText("標記顏色配置：");
            setBlockColor(view);
            ((TextView)view.findViewById(R.id.setting_block_1)).setText("7");
            ((TextView)view.findViewById(R.id.setting_block_3)).setText("6");
            ((TextView)view.findViewById(R.id.setting_block_5)).setText("6");
            ((TextView)view.findViewById(R.id.setting_block_7)).setText("6");

            setChenkColorHolder.resetStyle();
        }else if(holder instanceof SetErrorColor_Holder){//4
            SetErrorColor_Holder setErrorColorHolder=(SetErrorColor_Holder) holder;
            ((TextView)view.findViewById(R.id.textView)).setText("錯誤顏色配置：");
            setBlockColor(view);
            ((TextView)view.findViewById(R.id.setting_block_1)).setText("7");
            ((TextView)view.findViewById(R.id.setting_block_3)).setText("6");
            ((TextView)view.findViewById(R.id.setting_block_7)).setText("6");
            ((TextView)view.findViewById(R.id.setting_block_9)).setText("6");

            setErrorColorHolder.resetStyle();
        }else if(holder instanceof CheckTopicData_Holder){
            CheckTopicData_Holder checkTopicDataHolder=(CheckTopicData_Holder) holder;
            checkTopicDataHolder.getNewDateListener();
            Date o=checkTopicDataHolder.getOldDate();
            Date n = checkTopicDataHolder.getNewDate();

            if(o==null || n==null){
                checkTopicDataHolder.enableButton(false);
            }else if(!o.equals(n)){
                checkTopicDataHolder.enableButton(true);
            }else{
                checkTopicDataHolder.enableButton(false);
            }

        }

        /*
        switch (postion){
            case 0://作答模式
                SelectViewHolder viewHolder=(SelectViewHolder)holder;
                if(setting.getSelectMod()==Setting.SELECTMOD_BLOCK)
                    viewHolder.setText(context.getString(R.string.selete_mod_string_block));
                else if(setting.getSelectMod()==Setting.SELECTMOD_NUMBER)
                    viewHolder.setText(context.getString(R.string.selete_mod_string_number));
                break;
            case 1://背景
                SetColor_12_Holder color12Holder=(SetColor_12_Holder)holder;
                color12Holder.setColor1(setting.getColor1());
                color12Holder.setColor2(setting.getColor2());
                break;
            case 2://文字顏色
                SetFontColor_Holder setFontColorHolder=(SetFontColor_Holder) holder;
                setFontColorHolder.setTopicColor(setting.getTopicFontColor());
                setFontColorHolder.setAnserColor(setting.getFontColor());
                break;
            case 3:
                SetChenkColor_Holder setChenkColorHolder=(SetChenkColor_Holder) holder;
                setChenkColorHolder.resetStyle();
                break;
            case 4:
                SetErrorColor_Holder setErrorColorHolder=(SetErrorColor_Holder) holder;
                setErrorColorHolder.resetStyle();
                break;
            default:
        }*/
    }

    @Override
    public int getItemCount(){
        return 6;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class SelectViewHolder extends  RecyclerView.ViewHolder {
        TextView textView;
        public SelectViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(setting.getSelectMod()==Setting.SELECTMOD_NUMBER) {
                        setting.setSelectMod(Setting.SELECTMOD_BLOCK);
                        setText(context.getString(R.string.selete_mod_string_block));
                    }else{
                        setting.setSelectMod(Setting.SELECTMOD_NUMBER);
                        setText(context.getString(R.string.selete_mod_string_number));
                    }
                }
            });
        }
        public void setTextView(TextView v){
            textView=v;
        }
        public void setText(String s){
            textView.setText(s);
        }
    }
    class SetColor_12_Holder extends  RecyclerView.ViewHolder {
        //private TextView color1View1, color1View2,color1View3,color1View4,color1View5;
        //private TextView color2View1, color2View2, color2View3, color2View4;
        private List<TextView> color1View=new ArrayList<TextView>();
        private List<TextView> color2View=new ArrayList<TextView>();
        private Button color1BT,color2BT;
        public SetColor_12_Holder(View itemView) {
            super(itemView);
        }
        public void setColor1View(TextView a,TextView b,TextView c,TextView d,TextView e){
            color1View.add(a);
            color1View.add(b);
            color1View.add(c);
            color1View.add(d);
            color1View.add(e);
            //color1View1=a;color1View2=b;color1View3=c;color1View4=d;color1View5=e;
        }
        public void setColor2View(TextView a,TextView b,TextView c,TextView d){
            color2View.add(a);
            color2View.add(b);
            color2View.add(c);
            color2View.add(d);
            //color2View1=a;color2View2=b;color2View3=c;color2View4=d;
        }
        public void setBT(Button c1,Button c2){
            color1BT=c1;color2BT=c2;
            final Context  c=context;
            final List<TextView> color1=color1View;
            final List<TextView> color2=color2View;
            View.OnClickListener listener=new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    // ColorDrawable cd = (ColorDrawable) v.getBackground();
                    int colorCode;
                    if(v.getId()==R.id.color1_bt){
                        colorCode=setting.getColor1();
                        new ColorPickerDialog(c, new UpdateColor(1,(Button)v,setting,color1), colorCode,"顏色1選擇").show();
                    }else{
                        colorCode=setting.getColor2();
                        new ColorPickerDialog(c, new UpdateColor(1,(Button)v,setting,color2), colorCode,"顏色2選擇").show();
                    }
                    //pick a color (changed in the UpdateColor listener)
                }
            };
            color1BT.setOnClickListener(listener);
            color2BT.setOnClickListener(listener);
        }
        public void setColor1(int color){
            for(TextView t:color1View)
                t.setBackgroundColor(color);
            color1BT.setBackgroundColor(color);
        }
        public void setColor2(int color){
            for(TextView t:color2View)
                t.setBackgroundColor(color);
            color2BT.setBackgroundColor(color);
        }
    }
    class SetFontColor_Holder extends  RecyclerView.ViewHolder{
        private List<TextView> topicView=new ArrayList<TextView>();
        private List<TextView> ansView=new ArrayList<TextView>();
        private Button color1BT,color2BT;
        public SetFontColor_Holder(View itemView) {
            super(itemView);
        }
        public void setTopicView(TextView a,TextView b,TextView c){
            topicView.add(a);
            topicView.add(b);
            topicView.add(c);
        }
        public void setAnserView(TextView a){
            ansView.add(a);
        }
        public void setBT(Button c1,Button c2){
            color1BT=c1;color2BT=c2;
            final Context  c=context;
            final List<TextView> topic=topicView;
            final List<TextView> anser=ansView;
            View.OnClickListener listener=new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    // ColorDrawable cd = (ColorDrawable) v.getBackground();
                    int colorCode;
                    if(v.getId()==R.id.color1_bt){
                        colorCode=setting.getTopicFontColor();
                        new ColorPickerDialog(c, new UpdateColor(2,(Button)v,setting,topic), colorCode,"題目文字顏色").show();
                    }else{
                        colorCode=setting.getFontColor();
                        new ColorPickerDialog(c, new UpdateColor(2,(Button)v,setting,anser), colorCode,"答案文字顏色").show();
                    }
                    //pick a color (changed in the UpdateColor listener)
                }
            };
            color1BT.setOnClickListener(listener);
            color2BT.setOnClickListener(listener);
        }
        public void setTopicColor(int color){
            for(TextView t:topicView)
                t.setTextColor(color);
            color1BT.setBackgroundColor(color);
        }
        public void setAnserColor(int color){
            for(TextView t:ansView)
                t.setTextColor(color);
            color2BT.setBackgroundColor(color);
        }

    }
    class SetChenkColor_Holder extends  RecyclerView.ViewHolder{
        private TextView chenkView;
        private List<TextView> ansView=new ArrayList<TextView>();
        private Button color1BT,color2BT;
        public SetChenkColor_Holder(View itemView) {
            super(itemView);
        }

        public void setChenkView(TextView chenkView) {
            this.chenkView = chenkView;
        }
        public void addEqualsView(TextView v1) {
            ansView.add(v1);
        }
        public void setBT(Button c1,Button c2){
            color1BT=c1;color2BT=c2;
            final Context  c=context;
            final List<TextView> chenk=new ArrayList<TextView>();
            chenk.add(chenkView);
            final List<TextView> anser=ansView;
            View.OnClickListener listener=new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    // ColorDrawable cd = (ColorDrawable) v.getBackground();
                    int colorCode;
                    if(v.getId()==R.id.color1_bt){
                        colorCode=setting.getCheckColor();
                        new ColorPickerDialog(c, new UpdateColor(3,(Button)v,setting,chenk), colorCode,"選取框顏色").show();
                    }else{
                        colorCode=setting.getEqualsFontColor();
                        new ColorPickerDialog(c, new UpdateColor(3,(Button)v,setting,anser), colorCode,"相同文字顏色").show();
                    }
                    //pick a color (changed in the UpdateColor listener)
                }
            };
            color1BT.setOnClickListener(listener);
            color2BT.setOnClickListener(listener);
        }
        public void resetStyle(){
            for(TextView v:ansView)
                v.setTextColor(setting.getEqualsFontColor());
            color2BT.setBackgroundColor(setting.getEqualsFontColor());

            if (!(chenkView.getBackground() instanceof GradientDrawable))
                chenkView.setBackground(new GradientDrawable());
            GradientDrawable gd = (GradientDrawable) chenkView.getBackground();
            int strokeWidth = 10; // 5px not dp
            gd.setColor(Color.WHITE);
            gd.setStroke(strokeWidth, setting.getCheckColor());
            chenkView.setBackground(gd);
            color1BT.setBackgroundColor(setting.getCheckColor());
        }
    }
    class SetErrorColor_Holder extends  RecyclerView.ViewHolder{
        private List<TextView> errorView=new ArrayList<TextView>();
        private Button color1BT,color2BT;
        SetErrorColor_Holder(View itemView){super(itemView);}
        void addErrorBlock(TextView v){errorView.add(v);}
        public void setBT(Button c1,Button c2){
            color1BT=c1;color2BT=c2;
            final Context  c=context;
            final List<TextView> error=errorView;

            View.OnClickListener listener=new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    // ColorDrawable cd = (ColorDrawable) v.getBackground();
                    int colorCode;
                    if(v.getId()==R.id.color1_bt){
                        colorCode=setting.getErrorCheckColor();
                        new ColorPickerDialog(c, new UpdateColor(4,(Button)v,setting,error), colorCode,"錯誤框顏色").show();
                    }else{
                        colorCode=setting.getErrorFontColor();
                        new ColorPickerDialog(c, new UpdateColor(4,(Button)v,setting,error), colorCode,"錯誤文字顏色").show();
                    }
                    //pick a color (changed in the UpdateColor listener)
                }
            };
            color1BT.setOnClickListener(listener);
            color2BT.setOnClickListener(listener);
        }
        public void resetStyle(){
            for(TextView v:errorView){
                v.setTextColor(setting.getErrorFontColor());

                if (!(v.getBackground() instanceof GradientDrawable))
                    v.setBackground(new GradientDrawable());
                GradientDrawable gd = (GradientDrawable) v.getBackground();
                int strokeWidth = 10; // 5px not dp
                gd.setColor(Color.WHITE);
                gd.setStroke(strokeWidth, setting.getErrorCheckColor());
                v.setBackground(gd);
            }
            color1BT.setBackgroundColor(setting.getErrorCheckColor());
            color2BT.setBackgroundColor(setting.getErrorFontColor());
        }
    }

    class CheckTopicData_Holder extends RecyclerView.ViewHolder{
        TextView oldDateTV,newDateTV;
        Date newDate,oldDate;
        Button upDataBT;
        CheckTopicData_Holder(View itemView){super(itemView);}
        public void setDateTextView(TextView OLD,TextView NEW){
            this.oldDateTV=OLD;
            this.newDateTV=NEW;
        }
        public void setButton(Button b){
            this.upDataBT=b;
            upDataBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Sudoku_Topic_Data.reading){
                        Toast.makeText(upDataBT.getContext(), "有讀取動作正在進行!請稍後再試!", Toast.LENGTH_SHORT).show();
                    } else if(newDate!=null){
                        upDataBT.setEnabled(false);
                        Sudoku_Topic_Data.saveTopicForFIleBase(newDate,oldDateTV);
                        oldDate=newDate;
                    }
                }
            });
        }
        public void enableButton(boolean bool){
            upDataBT.setEnabled(bool);
        }
        public Date getOldDate(){
            oldDate= Sudoku_Topic_Data.getOldDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            String dateString = sdf.format(oldDate);
            oldDateTV.setText(dateString);
            return oldDate;
        }
        public Date getNewDate(){

            return newDate;
        }
        public void getNewDateListener() {

            DatabaseReference reference_contacts = FirebaseDatabase.getInstance().getReference("version");
            //reference_contacts.child("version").getKey();
            newDateTV.setText("正在抓取...");
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    //Post post = dataSnapshot.getValue(Post.class);
                    String s=dataSnapshot.getValue().toString();
                    ParsePosition pos = new ParsePosition(0);
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmm");
                    newDate = simpledateformat.parse(s, pos);
                    newDateTV.setText(s);

                    if(oldDate==null || newDate==null){
                        enableButton(false);
                    }else if(!oldDate.equals(newDate)){
                        enableButton(true);
                    }else{
                        enableButton(false);
                    }
                    Toast.makeText(newDateTV.getContext(), "版本號已抓取", Toast.LENGTH_SHORT).show();
                    // ...
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                    newDateTV.setText("網路錯誤");
                    newDate=null;
                }
            };
            reference_contacts.addValueEventListener(listener);//與filebase同步更新
            //reference_contacts.addListenerForSingleValueEvent(listener);//只抓取一次資料

            /*reference_contacts.addListenerForSingleValueEvent(listener);
            newDate= Sudoku_Topic_Data.getNewDate();
            if(newDate==null){
                newDateTV.setText("網路錯誤");
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String dateString = sdf.format(newDateTV);
                newDateTV.setText(dateString);
            }*/
            //return newDate;
        }
    }
}

class UpdateColor implements ColorPickerDialog.OnColorChangedListener {
    Button bt;
    List<TextView> view;
    Setting setting;
    int ID;
    public UpdateColor(int id,Button bt,Setting setting,List<TextView> view){
        this.ID=id;
        this.bt=bt;
        this.view=view;
        this.setting=setting;
    }
    public void colorChanged(int color) {
        bt.setBackgroundColor(color);
        switch (ID) {
            case 1:
                for (TextView t : view)
                    t.setBackgroundColor(color);
                if (bt.getId() == R.id.color1_bt) {
                    setting.setColor1(color);
                } else {
                    setting.setColor2(color);
                }
                break;
            case 2:
                for(TextView t:view)
                    t.setTextColor(color);
                if(bt.getId()==R.id.color1_bt) {
                    setting.setTopicFontColor(color);
                }else{
                    setting.setFontColor(color);
                }
                break;
            case 3: case 4:
                if(bt.getId()==R.id.color1_bt) {
                    for(TextView v:view){
                        if (!(v.getBackground() instanceof GradientDrawable))
                            v.setBackground(new GradientDrawable());
                        GradientDrawable gd = (GradientDrawable) v.getBackground();
                        int strokeWidth = 10; // 5px not dp
                        gd.setColor(Color.WHITE);
                        gd.setStroke(strokeWidth, color);
                        v.setBackground(gd);
                    }
                    if(ID==3)
                        setting.setCheckColor(color);
                    else
                        setting.setErrorCheckColor(color);
                }else{
                    for(TextView v:view){
                        v.setTextColor(color);
                    }
                    if(ID==3)
                        setting.setEqualsFontColor(color);
                    else
                        setting.setErrorFontColor(color);
                }
                break;

        }

    }
}
