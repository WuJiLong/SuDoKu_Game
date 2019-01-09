package com.example.wuqilong.sudoku_game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wuqilong.sudoku_game.SuDoKu_class.Sulution_operation;
import com.example.wuqilong.sudoku_game.define.Setting;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class Solution_operation_Activity extends AppCompatActivity {
    Setting setting;
    final int TEXTVIEW_BEGIN_ID=0x9487;

    final String DATA_KEY="com.example.wuqilong.solution_operation";


    int hold_block=40;
    int hold_num=1;
    Sulution_operation topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_operation);
        init();
    }
    void init(){
        topic=new Sulution_operation();
        Intent intent = getIntent();
        setting=new Setting();
        setting.setDataForBundle(intent.getExtras());//取得設定
        getData();
        createTextView();//創建81個TextView
        setTextViewListener();//設定TextView事件
        setNumButton();

    }
    void getData(){
        SharedPreferences spref = getApplication()
                .getSharedPreferences(DATA_KEY, Context.MODE_PRIVATE);
        if(spref.getBoolean("UTILITY",false)){
            for(int i=0;i<27;i++){
                int n=spref.getInt("data_"+String.valueOf(i),0);
                for(int j=0;j<3;j++){
                    topic.pushNUM(i*3+j,(n%10));
                    n=  n/10;
                }
            }
        }
    }

    private void showReturnDialog() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("返回主畫面");
        MyAlertDialog.setMessage("確定要返回主畫面?");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences spref = getApplication()
                        .getSharedPreferences(DATA_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spref.edit();
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        editor.clear();
                        if(which==DialogInterface.BUTTON_POSITIVE){
                            editor.putBoolean("UTILITY",true);
                            for(int i=0;i<27;i++){
                                int n=0;
                                for(int j=0;j<3;j++)
                                    n+=topic.getNUM(i*3+j)*pow(10,j);
                                Log.i("SuDoKu",String.valueOf(n));
                                editor.putInt("data_"+String.valueOf(i),n);
                            }
                        }
                        editor.commit();
                    case DialogInterface.BUTTON_NEGATIVE:
                        Intent intent=new Intent(Solution_operation_Activity.this,SuDoKu_main_Activity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        MyAlertDialog.setNeutralButton("取消",OkClick );
        MyAlertDialog.setPositiveButton("儲存並返回",OkClick);
        MyAlertDialog.setNegativeButton("不儲存返回",OkClick );
        MyAlertDialog.show();
    }
    private void showErrorDialog() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("無法計算");
        MyAlertDialog.setMessage("題目錯誤導致無法計算!!");
        MyAlertDialog.setNegativeButton("確定",null );
        MyAlertDialog.show();
    }

    void createTextView(){
        FrameLayout layout = findViewById(R.id.SOA_Layout);
        layout.setBackgroundColor(Color.BLACK);
        for(int i=0;i<81;i++){

            TextView textView = new TextView(this);
            textView.setId(TEXTVIEW_BEGIN_ID+i);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    (layout.getLayoutParams().width-10)/9 - 10,
                    (layout.getLayoutParams().height-10)/9 - 10);

            params.setMargins((layout.getLayoutParams().width/9)*(i%9) + 10, (layout.getLayoutParams().height/9)*(i/9) + 10, 0, 0);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);//置中
            textView.setTextSize(19);
            textView.setLayoutParams(params);
            layout. addView ( textView );
        }
        resetTextViewStyle();

    }
    void setTextViewListener(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId()-TEXTVIEW_BEGIN_ID;
                if(setting.getSelectMod()==setting.SELECTMOD_BLOCK) {
                    if(hold_block != id){
                        hold_block = id;
                        resetTextViewStyle();
                    }
                }else{
                    topic.pushNUM(id,hold_num);
                    resetTextViewStyle();
                }
            }
        };
        for(int i=0;i<81;i++){
            TextView tv=findViewById(TEXTVIEW_BEGIN_ID + i);
            tv.setOnClickListener(listener);
        }
    }
    void resetTextViewStyle(){
        int numForHoldBlock=topic.getNUM(hold_block);

        for(int i=0;i<81;i++) {
            int chunk_x = (i % 9) / 3;
            int chunk_y = (i / 9) / 3;

            TextView tv = findViewById(TEXTVIEW_BEGIN_ID + i);
            if (!(tv.getBackground() instanceof GradientDrawable))
                tv.setBackground(new GradientDrawable());
            GradientDrawable gd = (GradientDrawable) tv.getBackground();
            int strokeWidth = 10; // 5px not dp
            int strokeColor = setting.getColor2();
            int fillColor = setting.getColor2();
            if ((chunk_x + chunk_y) % 2 == 0) {
                strokeColor = fillColor = setting.getColor1();
            }
            gd.setColor(fillColor);
            if (setting.getSelectMod() == setting.SELECTMOD_BLOCK && i == hold_block)
                gd.setStroke(strokeWidth, setting.getCheckColor());
            else if(!topic.checkTheNUM(i))
                gd.setStroke(strokeWidth, setting.getErrorCheckColor());
            else
                gd.setStroke(strokeWidth, strokeColor);

            //Typeface font=tv.getTypeface();
            tv.setBackground(gd);

            if (!topic.checkTheNUM(i)){
                tv.setTextColor(setting.getErrorFontColor());
            }else if(setting.getSelectMod()==Setting.SELECTMOD_BLOCK && topic.getNUM(i)==numForHoldBlock && i!=hold_block && numForHoldBlock!=0){
                tv.setTextColor(setting.getEqualsFontColor());
            }else if(setting.getSelectMod()==Setting.SELECTMOD_NUMBER && topic.getNUM(i)==hold_num){
                tv.setTextColor(setting.getEqualsFontColor());
            }else if(topic.ans[i/9][i%9]!=0 && topic.table[i/9][i%9]==0){
                tv.setTextColor(setting.getFontColor());
            }else{
                tv.setTextColor(setting.getTopicFontColor());
            }
            tv.setTextSize(30);


            if(topic.table[i/9][i%9]!=0)
                tv.setText(String.valueOf(topic.table[i/9][i%9]));
            else if(topic.ans[i/9][i%9]!=0)
                tv.setText(String.valueOf(topic.ans[i/9][i%9]));
            else
                tv.setText("");
        }
    }

    private void setNumButton(){
        final List<Button> BT=new ArrayList<>();
        BT.add((Button)findViewById(R.id.SOA_clear_BT));
        BT.add((Button)findViewById(R.id.SOA_num1_BT));
        BT.add((Button)findViewById(R.id.SOA_num2_BT));
        BT.add((Button)findViewById(R.id.SOA_num3_BT));
        BT.add((Button)findViewById(R.id.SOA_num4_BT));
        BT.add((Button)findViewById(R.id.SOA_num5_BT));
        BT.add((Button)findViewById(R.id.SOA_num6_BT));
        BT.add((Button)findViewById(R.id.SOA_num7_BT));
        BT.add((Button)findViewById(R.id.SOA_num8_BT));
        BT.add((Button)findViewById(R.id.SOA_num9_BT));



        for(int i=0;i<10;i++) {
            int strokeWidth = 5; // 5px not dp
            int roundRadius = 15; // 15px not dp
            int strokeColor;
            int fillColor = Color.parseColor("#FFFFFF");
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(fillColor);
            gd.setCornerRadius(roundRadius);

            Button button=BT.get(i);
            if(hold_num==i && setting.getSelectMod()==setting.SELECTMOD_NUMBER) strokeColor=Color.RED;
            else strokeColor=Color.parseColor("#000000");
            gd.setStroke(strokeWidth, strokeColor);
            button.setBackground(gd);
        }
        int strokeWidth = 5; // 5px not dp
        int roundRadius = 15; // 15px not dp
        int strokeColor=Color.parseColor("#000000");
        int fillColor = Color.parseColor("#FFFFFF");
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);

        Button clearallbt=findViewById(R.id.SOA_clearall_BT);
        clearallbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                topic.clearALL();
                resetTextViewStyle();
            }
        });
        clearallbt.setBackground(gd);


        Button returnbt=findViewById(R.id.SOA_return_BT);
        returnbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showReturnDialog();
            }
        });
        returnbt.setBackground(gd.getConstantState().newDrawable());

        Button sobt=findViewById(R.id.SOA_solutionOperation_BT);
        sobt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!topic.checkTheTopic()){
                    showErrorDialog();
                }else if(topic.operation()){
                    Log.i("SuDoKu","OK!!!");
                    resetTextViewStyle();
                }else{
                    Log.i("SuDoKu","error");
                    showErrorDialog();
                }
            }
        });
        sobt.setBackground(gd.getConstantState().newDrawable());


        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int key_num=1;
                switch (v.getId()){
                    case R.id.SOA_clear_BT:key_num=0;break;
                    case R.id.SOA_num1_BT:key_num=1;break;
                    case R.id.SOA_num2_BT:key_num=2;break;
                    case R.id.SOA_num3_BT:key_num=3;break;
                    case R.id.SOA_num4_BT:key_num=4;break;
                    case R.id.SOA_num5_BT:key_num=5;break;
                    case R.id.SOA_num6_BT:key_num=6;break;
                    case R.id.SOA_num7_BT:key_num=7;break;
                    case R.id.SOA_num8_BT:key_num=8;break;
                    case R.id.SOA_num9_BT:key_num=9;break;
                }
                if(setting.getSelectMod()==setting.SELECTMOD_BLOCK){
                    topic.pushNUM(hold_block,key_num);
                }else{
                    /*code block*/{
                        Button button=BT.get(hold_num);
                        Drawable da=button.getBackground();
                        int strokeWidth = 5; // 5px not dp
                        int strokeColor=Color.parseColor("#000000");
                        ((GradientDrawable)da).setStroke(strokeWidth, strokeColor);
                        button.setBackground(da);
                    }
                    hold_num=key_num;
                    /*code block*/{
                        Button button=BT.get(hold_num);
                        GradientDrawable da=(GradientDrawable)button.getBackground();//因為已set過
                        int strokeWidth = 5; // 5px not dp
                        int strokeColor=setting.getCheckColor();
                        da.setStroke(strokeWidth, strokeColor);
                        button.setBackground(da);
                    }
                }
                resetTextViewStyle();
            }
        };
        for(Button b:BT)
            b.setOnClickListener(listener);
    }

}
