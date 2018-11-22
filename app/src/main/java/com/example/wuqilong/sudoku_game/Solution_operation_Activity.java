package com.example.wuqilong.sudoku_game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wuqilong.sudoku_game.define.Setting;

import java.util.ArrayList;
import java.util.List;

public class Solution_operation_Activity extends AppCompatActivity {
    Setting setting;
    final int TEXTVIEW_BEGIN_ID=0x9487;
    int hold_block=40;
    int hold_num=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_operation);
        init();
    }
    void init(){
        Intent intent = getIntent();
        setting=new Setting();
        setting.setDataForBundle(intent.getExtras());//取得設定

        createTextView();//創建81個TextView
        setTextViewListener();//設定TextView事件
        setNumButton();

        Button returnbt=findViewById(R.id.SOA_return_BT);
        returnbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showReturnDialog();
            }
        });

    }

    private void showReturnDialog() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("返回主畫面");
        MyAlertDialog.setMessage("確定要返回主畫面?全部內容將會被清空。");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:

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
                    if(hold_num==0)
                        ((TextView)v).setText("");
                    else
                        ((TextView)v).setText(String.valueOf(hold_num));
                }
            }
        };
        for(int i=0;i<81;i++){
            TextView tv=findViewById(TEXTVIEW_BEGIN_ID + i);
            tv.setOnClickListener(listener);
        }
    }
    void resetTextViewStyle(){
        for(int i=0;i<81;i++){
            int chunk_x=(i%9)/3;
            int chunk_y=(i/9)/3;

            TextView tv=findViewById(TEXTVIEW_BEGIN_ID + i);
            if(!(tv.getBackground() instanceof GradientDrawable))
                tv.setBackground(new GradientDrawable());
            GradientDrawable gd =(GradientDrawable)tv.getBackground();
            int strokeWidth = 10; // 5px not dp
            //int roundRadius = 15; // 15px not dp
            int strokeColor=setting.getColor2();
            int fillColor=setting.getColor2();
            if((chunk_x+chunk_y)%2==0) {
                strokeColor = fillColor = setting.getColor1();
            }
            gd.setColor(fillColor);
            if(setting.getSelectMod()==setting.SELECTMOD_BLOCK && i==hold_block)
                gd.setStroke(10,Color.RED);
            else
                gd.setStroke(strokeWidth, strokeColor);
            tv.setBackground(gd);
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
        findViewById(R.id.SOA_clearall_BT).setBackground(gd);

        findViewById(R.id.SOA_return_BT).setBackground(gd.getConstantState().newDrawable());


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
                if(setting.getSelectMod()==setting.SELECTMOD_BLOCK){ TextView view= findViewById(TEXTVIEW_BEGIN_ID+hold_block);
                    if(key_num==0)
                        view.setText("");
                    else
                        view.setText(String.valueOf(key_num));
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
                        int strokeColor=Color.parseColor("#FF0000");
                        da.setStroke(strokeWidth, strokeColor);
                        button.setBackground(da);
                    }
                }
            }
        };
        for(Button b:BT)
            b.setOnClickListener(listener);
    }
}
