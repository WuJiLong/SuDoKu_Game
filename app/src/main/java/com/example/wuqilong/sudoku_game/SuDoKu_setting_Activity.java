package com.example.wuqilong.sudoku_game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.wuqilong.sudoku_game.define.MyID;
import com.example.wuqilong.sudoku_game.define.Setting;

import java.util.ArrayList;
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
        recyclerViewInit();
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        switch (type){
            case 0:
                View view0= LayoutInflater.from(context).inflate(R.layout.setting_selectmod,parent,false);
                SelectViewHolder viewHolder0=new SelectViewHolder(view0);
                viewHolder0.setTextView((TextView)view0.findViewById(R.id.select_mod_tv));
                return viewHolder0;
            case 1:
                View view1= LayoutInflater.from(context).inflate(R.layout.setting_grid_color,parent,false);
                SetColor_12_Holder viewHolder1=new SetColor_12_Holder(view1);
                ((FrameLayout)view1.findViewById(R.id.color_frameLayout)).setBackgroundColor(0xff000000);
                viewHolder1.setColor1View((TextView) view1.findViewById(R.id.setting_color1_1),
                        (TextView)view1.findViewById(R.id.setting_color1_2),
                        (TextView)view1.findViewById(R.id.setting_color1_3),
                        (TextView)view1.findViewById(R.id.setting_color1_4),
                        (TextView)view1.findViewById(R.id.setting_color1_5));
                viewHolder1.setColor2View((TextView) view1.findViewById(R.id.setting_color2_1),
                        (TextView)view1.findViewById(R.id.setting_color2_2),
                        (TextView)view1.findViewById(R.id.setting_color2_3),
                        (TextView)view1.findViewById(R.id.setting_color2_4));
                viewHolder1.setBT((Button)view1.findViewById(R.id.color1_bt),
                        (Button)view1.findViewById(R.id.color2_bt));
                return viewHolder1;

            default:
        }

        //viewHolder.setTvTitle((TextView) view.findViewById(R.id.item_title));
        //viewHolder.setSubTitle((TextView) view.findViewById(R.id.subTitle));
        //viewHolder.setTextView((TextView) view.findViewById(R.id.text));
        // imageView2
        // viewHolder.setImageView((ImageView) view.findViewById(R.id.imageView2));
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int postion){
        switch (postion){
            case 0:
                SelectViewHolder viewHolder=(SelectViewHolder)holder;
                if(setting.getSelectMod()==Setting.SELECTMOD_BLOCK)
                    viewHolder.setText(context.getString(R.string.selete_mod_string_block));
                else if(setting.getSelectMod()==Setting.SELECTMOD_NUMBER)
                    viewHolder.setText(context.getString(R.string.selete_mod_string_number));
                break;
            case 1:
                SetColor_12_Holder color12Holder=(SetColor_12_Holder)holder;
                color12Holder.setColor1(setting.getColor1());
                color12Holder.setColor2(setting.getColor2());
                break;
            default:
        }
       /* RecyclerData item=data.get(postion);
        holder.setTitleString(item.getTitle());
        holder.setSubTitleString(item.getSubTitle());
        holder.setTextViewString(item.getText());
        holder.setimageViewImage(item.getpictureID());*/
    }

    @Override
    public int getItemCount(){
        return 2;
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
                        new ColorPickerDialog(c, new UpdateColor((Button)v,setting,color1), colorCode,"顏色1選擇").show();
                    }else{
                        colorCode=setting.getColor2();
                        new ColorPickerDialog(c, new UpdateColor((Button)v,setting,color2), colorCode,"顏色2選擇").show();
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
}

class UpdateColor implements ColorPickerDialog.OnColorChangedListener {
    Button bt;
    List<TextView> view;
    Setting setting;
    public UpdateColor(Button bt,Setting setting,List<TextView> view){
        this.bt=bt;
        this.view=view;
        this.setting=setting;
    }
    public void colorChanged(int color) {
        bt.setBackgroundColor(color);
        for(TextView t:view)
            t.setBackgroundColor(color);
        if(bt.getId()==R.id.color1_bt) {
            setting.setColor1(color);
        }else{
            setting.setColor2(color);
        }

    }
}
