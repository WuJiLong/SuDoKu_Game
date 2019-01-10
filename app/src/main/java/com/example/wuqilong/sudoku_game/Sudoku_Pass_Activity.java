package com.example.wuqilong.sudoku_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Sudoku_Pass_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_pass);
        init();
    }
    void init(){
        Intent intent = getIntent();
        int t=intent.getIntExtra("time",0);
        TextView tv=findViewById(R.id.time_tv);
        tv.setText(timeString(t));
        Button bt=findViewById(R.id.return_BT);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sudoku_Pass_Activity.this,SuDoKu_main_Activity.class);
                startActivity(intent);
            }
        });
    }
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
    public void onBackPressed() { }

}
