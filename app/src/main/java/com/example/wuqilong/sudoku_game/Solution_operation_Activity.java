package com.example.wuqilong.sudoku_game;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wuqilong.sudoku_game.define.Setting;

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

    }
    void createTextView(){
        FrameLayout layout = findViewById(R.id.SOA_Layout);
        layout.setBackgroundColor(Color.BLACK);
        for(int i=0;i<81;i++){
            int chunk_x=(i%9)/3;
            int chunk_y=(i/9)/3;
            TextView textView = new TextView(this);
            textView.setId(TEXTVIEW_BEGIN_ID+i);

            int strokeWidth = 10; // 5px not dp
            //int roundRadius = 15; // 15px not dp
            int strokeColor=setting.getColor2();
            int fillColor=setting.getColor2();
            if((chunk_x+chunk_y)%2==0) {
                strokeColor = fillColor = setting.getColor1();
            }
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(fillColor);
            gd.setStroke(strokeWidth, strokeColor);
            textView.setBackground(gd);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    (layout.getLayoutParams().width-10)/9 - 10,
                    (layout.getLayoutParams().height-10)/9 - 10);

            params.setMargins((layout.getLayoutParams().width/9)*(i%9) + 10, (layout.getLayoutParams().height/9)*(i/9) + 10, 0, 0);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);//置中
            textView.setTextSize(19);
            textView.setLayoutParams(params);
            layout. addView ( textView );
        }
        if(setting.getSelectMod()==setting.SELECTMOD_BLOCK){
            TextView tv=findViewById(TEXTVIEW_BEGIN_ID+hold_block);
            GradientDrawable gd=(GradientDrawable)tv.getBackground();
            gd.setStroke(10,Color.RED);
            tv.setBackground(gd);
        }
    }
    void setTextViewListener(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId()-TEXTVIEW_BEGIN_ID;
                if(setting.getSelectMod()==setting.SELECTMOD_BLOCK) {
                    if(hold_block != id){
                        int chunk_x=(hold_block%9)/3;
                        int chunk_y=(hold_block/9)/3;
                        GradientDrawable gd =(GradientDrawable)findViewById(TEXTVIEW_BEGIN_ID+hold_block).getBackground();
                        if((chunk_x+chunk_y)%2==0)
                            gd.setStroke(10, setting.getColor1());
                        else
                            gd.setStroke(10, setting.getColor2());
                        findViewById(TEXTVIEW_BEGIN_ID+hold_block).setBackground(gd);

                        GradientDrawable gd2 =(GradientDrawable)v.getBackground();
                        gd2.setStroke(10, Color.RED);
                        v.setBackground(gd2);
                        hold_block = id;
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
}
