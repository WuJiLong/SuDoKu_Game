package com.example.wuqilong.sudoku_game;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.aware.SubscribeDiscoverySession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuqilong.sudoku_game.SuDoKu_class.Sudoku_Topic;
import com.example.wuqilong.sudoku_game.SuDoKu_class.Sudoku_Topic_Data;
import com.example.wuqilong.sudoku_game.define.Setting;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sudoku_Choose_Activity extends AppCompatActivity {
    Setting setting;
    final int TEXTVIEW_BEGIN_ID=0x9487;
    List<Sudoku_Topic> topicList;
    int check=0;
    static Sudoku_Topic chenkTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_choose);
        init();
    }
    private void init(){
        Intent intent = getIntent();
        setting=new Setting();
        setting.setDataForBundle(intent.getExtras());//取得設定
        topicList= Sudoku_Topic_Data.getTopicList();
        if(topicList.size()>=1) chenkTopic=topicList.get(0);
        //Toast.makeText(this, "題目數量"+String.valueOf(topicList.size()), Toast.LENGTH_SHORT).show();
        settingButton();
        createTextView();
    }
    void settingButton(){

        findViewById(R.id.start_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chenkTopic!=null){
                    chenkTopic.randomAns();
                    Intent intent=new Intent(Sudoku_Choose_Activity.this,SudokuPlayingActivity.class);
                    intent.putExtras(setting.getDataBundle());
                    startActivity(intent);
                }
            }
        });
        if(topicList.size()==0) findViewById(R.id.start_bt).setEnabled(false);

        findViewById(R.id.to_main_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sudoku_Choose_Activity.this,SuDoKu_main_Activity.class);
                startActivity(intent);
            }
        });
        final Button lastBT=findViewById(R.id.last_topic_bt);
        lastBT.setEnabled(false);
        final Button nextBT=findViewById(R.id.next_topic_bt);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.last_topic_bt)
                    check--;
                else
                    check++;
                if(check<=0){
                    check=0;
                    lastBT.setEnabled(false);
                    if(topicList.size()!=0)
                        nextBT.setEnabled(true);
                }else if(check>=topicList.size()-1){
                    check = topicList.size()-1;
                    nextBT.setEnabled(false);
                    if(topicList.size()!=0)
                        lastBT.setEnabled(true);
                }else{
                    nextBT.setEnabled(true);
                    lastBT.setEnabled(true);
                }
                if(topicList.size()>=1)
                    chenkTopic=topicList.get(check);
                resetTextViewStyle();
            }
        };
        lastBT.setOnClickListener(listener);
        nextBT.setOnClickListener(listener);
    }
    void createTextView(){
        FrameLayout layout = findViewById(R.id.Choose_Layout);
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
    void resetTextViewStyle(){
        if(check<0) check=0;
        if(check>=topicList.size()) check=topicList.size()-1;
        Sudoku_Topic t=topicList.get(check);
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
            if(topicList.size()!=0) {
                if(t.show[i/9][i%9]){
                    gd.setColor(Color.GRAY);
                    gd.setStroke(strokeWidth,Color.GRAY);
                }else{
                    gd.setColor(fillColor);
                    gd.setStroke(strokeWidth, strokeColor);
                }
            }else{
                gd.setColor(Color.GRAY);
                gd.setStroke(strokeWidth,Color.GRAY);
            }
            //Typeface font=tv.getTypeface();
            tv.setBackground(gd);


        }
    }
}
