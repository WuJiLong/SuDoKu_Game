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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wuqilong.sudoku_game.SuDoKu_class.Sudoku_Topic;
import com.example.wuqilong.sudoku_game.SuDoKu_class.Sudoku_playing;
import com.example.wuqilong.sudoku_game.define.Setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.pow;

public class SudokuPlayingActivity extends AppCompatActivity {
    Sudoku_playing sudokuPlayingObj;
    Setting setting;
    final int TEXTVIEW_BEGIN_ID=0x9487;
    TextView showTime;
    int hold_block=40;
    int hold_num=1;
    private Handler mHandlerTime = new Handler();
    int m_nTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_playing);
        init();
    }
    @Override
    public void onDestroy()
    {
        mHandlerTime.removeCallbacks(timerRun);
        super.onDestroy();
    }
    private final Runnable timerRun = new Runnable()
    {
        public void run()
        {
            ++m_nTime; // 經過的秒數 + 1
            showTime.setText(timeString(m_nTime));
            mHandlerTime.postDelayed(this, 1000);
            // 若要取消可以寫一個判斷在這決定是否啟動下一次即可
        }
    };
    private String timeString(int sec){
        int h=sec/3600;
        int m=(sec/60)%60;
        int s=sec%60;
        String hs=(h<10)?"0"+String.valueOf(h):String.valueOf(h);
        String ms=(m<10)?"0"+String.valueOf(m):String.valueOf(m);
        String ss=(s<10)?"0"+String.valueOf(s):String.valueOf(s);
        return hs+":"+ms+":"+ss;
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("返回上一頁");
        MyAlertDialog.setMessage("遊戲將直接終止，請問要返回上一頁嗎?");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        SudokuPlayingActivity.super.onBackPressed();
                        break;
                }
            }
        };
        MyAlertDialog.setNeutralButton("取消",OkClick );
        //MyAlertDialog.setPositiveButton("儲存並返回",OkClick);
        MyAlertDialog.setNegativeButton("確認",OkClick );
        MyAlertDialog.show();
    }
    void init(){
        sudokuPlayingObj=new Sudoku_playing(Sudoku_Choose_Activity.chenkTopic);

        Intent intent = getIntent();
        setting=new Setting();
        setting.setDataForBundle(intent.getExtras());//取得設定
        showTime=findViewById(R.id.show_time_tv);
        createTextView();
        setTextViewListener();//設定TextView事件
        setNumButton();
        mHandlerTime.postDelayed(timerRun, 1000);
        //startTime=new Date(System.currentTimeMillis());
    }
    private void gamePass(){//查看遊戲是否結束
        if(sudokuPlayingObj.checkAns()){//如果做完答案
            mHandlerTime.removeCallbacks(timerRun);
            Intent intent=new Intent(SudokuPlayingActivity.this,Sudoku_Pass_Activity.class);
            intent.putExtra("time",m_nTime);
            startActivity(intent);
        }
    }
    private void showReturnDialog() {
        AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(this);
        MyAlertDialog.setTitle("返回主畫面");
        MyAlertDialog.setMessage("遊戲將直接終止，請問要返回主畫面嗎?");
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                switch(which){

                    case DialogInterface.BUTTON_NEGATIVE:
                        Intent intent=new Intent(SudokuPlayingActivity.this,SuDoKu_main_Activity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        MyAlertDialog.setNeutralButton("取消",OkClick );
        //MyAlertDialog.setPositiveButton("儲存並返回",OkClick);
        MyAlertDialog.setNegativeButton("確認",OkClick );
        MyAlertDialog.show();
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

                AlertDialog.Builder MyAlertDialog = new AlertDialog.Builder(SudokuPlayingActivity.this);
                MyAlertDialog.setTitle("重置");
                MyAlertDialog.setMessage("遊戲將重新開始，請問要繼續嗎?");
                DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_NEGATIVE:
                                hold_block=40;
                                hold_num=1;
                                //sudokuPlayingObj.clearALL();
                                m_nTime=0;
                                showTime.setText(timeString(m_nTime));
                                sudokuPlayingObj.randTopic();
                                resetTextViewStyle();
                                break;
                        }
                    }
                };
                MyAlertDialog.setNeutralButton("取消",OkClick );
                //MyAlertDialog.setPositiveButton("儲存並返回",OkClick);
                MyAlertDialog.setNegativeButton("確認",OkClick );
                MyAlertDialog.show();
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
                    sudokuPlayingObj.pushNUM(hold_block,key_num);
                    if(key_num!=0)
                        gamePass();
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
                        GradientDrawable da=(GradientDrawable)button.getBackground();//
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
                    sudokuPlayingObj.pushNUM(id,hold_num);
                    if(hold_num!=0)
                        gamePass();
                    resetTextViewStyle();
                }
            }
        };
        for(int i=0;i<81;i++){
            TextView tv=findViewById(TEXTVIEW_BEGIN_ID + i);
            tv.setOnClickListener(listener);
        }
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
        int numForHoldBlock=sudokuPlayingObj.getNUM(hold_block);

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
            else if(!sudokuPlayingObj.checkTheNUM(i))
                gd.setStroke(strokeWidth, setting.getErrorCheckColor());
            else
                gd.setStroke(strokeWidth, strokeColor);

            //Typeface font=tv.getTypeface();
            tv.setBackground(gd);

            if (!sudokuPlayingObj.checkTheNUM(i)){
                tv.setTextColor(setting.getErrorFontColor());
            }else if(setting.getSelectMod()== Setting.SELECTMOD_BLOCK && sudokuPlayingObj.getNUM(i)==numForHoldBlock && i!=hold_block && numForHoldBlock!=0){
                tv.setTextColor(setting.getEqualsFontColor());
            }else if(setting.getSelectMod()==Setting.SELECTMOD_NUMBER && sudokuPlayingObj.getNUM(i)==hold_num){
                tv.setTextColor(setting.getEqualsFontColor());
            }else if(!sudokuPlayingObj.mark[i/9][i%9]){
                tv.setTextColor(setting.getFontColor());
            }else{
                tv.setTextColor(setting.getTopicFontColor());
            }
            tv.setTextSize(30);

            if(sudokuPlayingObj.myAns[i/9][i%9]!=0)
                tv.setText(String.valueOf(sudokuPlayingObj.myAns[i/9][i%9]));
            else
                tv.setText("");
            if(sudokuPlayingObj.mark[i/9][i%9]){
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            }
        }
    }
}
