package com.example.wuqilong.sudoku_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wuqilong.sudoku_game.define.MyID;
import com.example.wuqilong.sudoku_game.define.Setting;

public class SuDoKu_main_Activity extends AppCompatActivity {
    Setting setting_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_main);
        init();
    }
    void init(){
        Setting.activity=this;
        setting_data=new Setting();
        setting_data.loadSettingData();
        buttonInit();
    }
    //button init
    void buttonInit(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.main_to_play_BT:
                        Intent intent=new Intent(SuDoKu_main_Activity.this,Sudoku_Choose_Activity.class);
                        intent.putExtras(setting_data.getDataBundle());
                        startActivity(intent);
                        break;
                    case R.id.main_to_osa_BT:
                        Intent intent_osa=new Intent(SuDoKu_main_Activity.this,Solution_operation_Activity.class);
                        intent_osa.putExtras(setting_data.getDataBundle());
                        startActivity(intent_osa);
                        break;
                    case R.id.main_to_setting_BT:
                        Intent intent_setting=new Intent(SuDoKu_main_Activity.this,SuDoKu_setting_Activity.class);
                        intent_setting.putExtras(setting_data.getDataBundle());
                        //startActivity(intent_setting);
                        startActivityForResult(intent_setting, MyID.SETTING_CODE);
                        break;
                }
            }
        };
        Button to_Play_bt=findViewById(R.id.main_to_play_BT);
        to_Play_bt.setOnClickListener(listener);
        Button to_Soa_bt=findViewById(R.id.main_to_osa_BT);
        to_Soa_bt.setOnClickListener(listener);
        Button to_Setting_bt=findViewById(R.id.main_to_setting_BT);
        to_Setting_bt.setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){//intent代號 狀態代碼 intent
        switch(requestCode){
            case MyID.SETTING_CODE:
                if(resultCode==MyID.SETTING_RESULT_CODE_SAVE){
                    setting_data.setDataForBundle(intent.getExtras());//儲存設定到類別
                    Toast.makeText(this, "已儲存設定", Toast.LENGTH_SHORT).show();
                }else if(resultCode==MyID.SETTING_RESULT_CODE_CHANGE){
                    setting_data.setDataForBundle(intent.getExtras());//儲存設定到類別
                    Toast.makeText(this, "部分設定未儲存", Toast.LENGTH_SHORT).show();
                }else if(resultCode==MyID.SETTING_RESULT_CODE_NO_SAVE){
                    Toast.makeText(this, "設定未變動", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

}