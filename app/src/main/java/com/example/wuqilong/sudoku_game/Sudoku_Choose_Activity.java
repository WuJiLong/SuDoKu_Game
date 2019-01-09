package com.example.wuqilong.sudoku_game;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wuqilong.sudoku_game.SuDoKu_class.Sudoku_Topic;
import com.example.wuqilong.sudoku_game.define.Setting;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sudoku_Choose_Activity extends AppCompatActivity {
    Setting setting;
    final int TEXTVIEW_BEGIN_ID=0x9487;
    List<Sudoku_Topic> topicList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_choose);
        init();
        //Date expiredDate = stringToDate("201901091926", "yyyyMMddHHmm");
    }
    /*
    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }*/
    private void init(){
        Intent intent = getIntent();
        setting=new Setting();
        setting.setDataForBundle(intent.getExtras());//取得設定

        settingButton();
        createTextView();
    }
    void settingButton(){
        findViewById(R.id.to_main_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sudoku_Choose_Activity.this,SuDoKu_main_Activity.class);
                startActivity(intent);
            }
        });

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
                gd.setColor(fillColor);
                gd.setStroke(strokeWidth, strokeColor);
            }else{
                gd.setColor(Color.GRAY);
                gd.setStroke(strokeWidth,Color.GRAY);
            }
            //Typeface font=tv.getTypeface();
            tv.setBackground(gd);


        }
    }
}
