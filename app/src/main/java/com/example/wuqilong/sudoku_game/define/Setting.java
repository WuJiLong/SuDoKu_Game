package com.example.wuqilong.sudoku_game.define;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Setting {
    public static Activity activity;
    private final static String KEY="com.example.wuqilong.sudokugame";//設定黨Key值

    private final  static String SELECTMOD_KEY="SELECTMOD";//設定檔案中的KEY值
    public final static int SELECTMOD_BLOCK  =0x4145B;//選擇區塊再按數字
    public final static int SELECTMOD_NUMBER =0x4145A;//選擇數字再按區塊

    private final static String COLOR1_KEY="COLOR1";//設定檔案中的KEY值
    private final static String COLOR2_KEY="COLOR2";//設定檔案中的KEY值


    private int selectMod=SELECTMOD_BLOCK;
    //private int noSaveSelectMod=0;
    private int color1=0xffffff00;
    //private int noSaveColor1=0xffffff00;
    private int color2=0xffffffff;
    //private int noSaveColor2=0xffffffff;

    public void loadSettingData(){
        SharedPreferences spref = activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);
        selectMod = spref.getInt(SELECTMOD_KEY,/*default value*/SELECTMOD_BLOCK);//取出數值
        //noSaveSelectMod=selectMod;
        color1 = spref.getInt(COLOR1_KEY,/*default value*/0xffffff00);//取出數值
        //noSaveColor1=color1;
        color2 = spref.getInt(COLOR2_KEY,/*default value*/0xffffffff);//取出數值
        //noSaveColor2=color2;
    }
    public Bundle getDataBundle(){
        Bundle bundle=new Bundle();
        bundle.putInt(SELECTMOD_KEY,selectMod);
        bundle.putInt(COLOR1_KEY,color1);
        bundle.putInt(COLOR2_KEY,color2);

        return bundle;
    }

    public void setDataForBundle(Bundle bundle){
        selectMod=bundle.getInt(SELECTMOD_KEY,selectMod);
        color1=bundle.getInt(COLOR1_KEY,color1);
        color2=bundle.getInt(COLOR2_KEY,color2);
    }
    public void saveSettingData(){//儲存設定檔案

        SharedPreferences spref = activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.clear();//清除所有設定

        //selectMod=noSaveSelectMod;//複製儲存
        editor.putInt(SELECTMOD_KEY,selectMod);//儲存作答模式設定

        //color1=noSaveColor1;//複製儲存
        editor.putInt(COLOR1_KEY,color1);//儲存作答模式設定

        //color2=noSaveColor2;//複製儲存
        editor.putInt(COLOR2_KEY,color2);//儲存作答模式設定

        editor.commit();//儲存設定
    }

    public int getSelectMod(){
        return selectMod;
    }
    public int getColor1(){
        return color1;
    }
    public int getColor2(){
        return color2;
    }

    public void setSelectMod(int m){
        selectMod=m;
    }
    public void setColor1(int c){
        color1=c;
    }
    public void setColor2(int c){
        color2=c;
    }

    public boolean equals(Setting setting){
        if(setting.getSelectMod()!=selectMod) return false;
        if(setting.getColor1()!=color1) return false;
        if(setting.getColor2()!=color2) return false;
        return true;
    }

    public void copyFor(Setting setting){
        selectMod=setting.getSelectMod();
        color1=setting.getColor1();
        color2=setting.getColor2();
    }
}
