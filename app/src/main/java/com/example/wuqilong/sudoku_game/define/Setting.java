package com.example.wuqilong.sudoku_game.define;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


public class Setting {
    public static Activity activity;
    private final static String KEY="com.example.wuqilong.sudokugame";//設定檔Key值

    //-------------------------------OK
    private final  static String SELECTMOD_KEY="SELECTMOD";//設定檔案中的KEY值
    public final static int SELECTMOD_BLOCK  =0x4145B;//選擇區塊再按數字
    public final static int SELECTMOD_NUMBER =0x4145A;//選擇數字再按區塊
    private int selectMod=SELECTMOD_BLOCK;
    //-------------------------------OK
    private final static String COLOR1_KEY="COLOR1";//設定檔案中的KEY值
    private int color1=Color.YELLOW;
    //-------------------------------OK
    private final static String COLOR2_KEY="COLOR2";//設定檔案中的KEY值
    private int color2=Color.WHITE;
    //-------------------------------OK
    private final static String FONTCOLOR_KEY="FONTCOLOR";//設定檔案中的KEY值
    private int fontColor=Color.GRAY;
    //-------------------------------OK
    private final static String TOPIC_FONTCOLOR_KEY="TOPICFONTCOLOR";//設定檔案中的KEY值
    private int topicFontColor=Color.BLACK;
    //-------------------------------OK
    private final static String EQUALSFONTCOLOR_KEY="EQUALSFONTCOLOR";//設定檔案中的KEY值
    private int equalsFontColor=Color.GREEN;
    //-------------------------------OK
    private final static String CHECKCOLOR_KEY="CHECKCOLOR";//設定檔案中的KEY值
    private int checkColor=Color.GREEN;
    //
    private final static String ERRORFONTCOLOR_KEY="ERRORFONTCOLOR";//設定檔案中的KEY值
    private int errorFontColor=Color.RED;
    //
    private final static String ERRORCHECKCOLOR_KEY="ERRORCHECKCOLOR";//設定檔案中的KEY值
    private int errorCheckColor=Color.RED;


    public void loadSettingData(){
        SharedPreferences spref = activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);

        selectMod = spref.getInt(SELECTMOD_KEY,/*default value*/selectMod);//取出數值
        color1 = spref.getInt(COLOR1_KEY,/*default value*/color1);//取出數值
        color2 = spref.getInt(COLOR2_KEY,/*default value*/color2);//取出數值
        fontColor = spref.getInt(FONTCOLOR_KEY,/*default value*/fontColor);//取出數值
        topicFontColor = spref.getInt(TOPIC_FONTCOLOR_KEY,/*default value*/topicFontColor);//取出數值
        errorFontColor = spref.getInt(ERRORFONTCOLOR_KEY,/*default value*/errorFontColor);//取出數值
        equalsFontColor = spref.getInt(EQUALSFONTCOLOR_KEY,/*default value*/equalsFontColor);//取出數值
        checkColor=spref.getInt(CHECKCOLOR_KEY,/*default value*/checkColor);//取出數值
        errorCheckColor=spref.getInt(ERRORCHECKCOLOR_KEY,/*default value*/errorCheckColor);//取出數值


    }
    public Bundle getDataBundle(){
        Bundle bundle=new Bundle();
        bundle.putInt(SELECTMOD_KEY,selectMod);
        bundle.putInt(COLOR1_KEY,color1);
        bundle.putInt(COLOR2_KEY,color2);
        bundle.putInt(FONTCOLOR_KEY,fontColor);
        bundle.putInt(TOPIC_FONTCOLOR_KEY,topicFontColor);
        bundle.putInt(ERRORFONTCOLOR_KEY,errorFontColor);
        bundle.putInt(EQUALSFONTCOLOR_KEY,equalsFontColor);
        bundle.putInt(CHECKCOLOR_KEY,checkColor);
        bundle.putInt(ERRORCHECKCOLOR_KEY,errorCheckColor);
        return bundle;
    }

    public void setDataForBundle(Bundle bundle){
        selectMod=bundle.getInt(SELECTMOD_KEY,selectMod);
        color1=bundle.getInt(COLOR1_KEY,color1);
        color2=bundle.getInt(COLOR2_KEY,color2);
        fontColor=bundle.getInt(FONTCOLOR_KEY,fontColor);
        topicFontColor=bundle.getInt(TOPIC_FONTCOLOR_KEY,topicFontColor);
        errorFontColor=bundle.getInt(ERRORFONTCOLOR_KEY,errorFontColor);
        equalsFontColor=bundle.getInt(EQUALSFONTCOLOR_KEY,equalsFontColor);
        checkColor=bundle.getInt(CHECKCOLOR_KEY,checkColor);
        errorCheckColor=bundle.getInt(ERRORCHECKCOLOR_KEY,errorCheckColor);
    }
    public void saveSettingData(){//儲存設定檔案

        SharedPreferences spref = activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.clear();//清除所有設定
        editor.putInt(SELECTMOD_KEY,selectMod);//儲存作答模式設定
        editor.putInt(COLOR1_KEY,color1);//
        editor.putInt(COLOR2_KEY,color2);//
        editor.putInt(FONTCOLOR_KEY,fontColor);//
        editor.putInt(TOPIC_FONTCOLOR_KEY,topicFontColor);//
        editor.putInt(ERRORFONTCOLOR_KEY,errorFontColor);//
        editor.putInt(EQUALSFONTCOLOR_KEY,equalsFontColor);//
        editor.putInt(CHECKCOLOR_KEY,checkColor);//
        editor.putInt(ERRORCHECKCOLOR_KEY,errorCheckColor);//


        editor.commit();//儲存設定
    }

    public int getSelectMod(){ return selectMod; }
    public int getColor1(){ return color1; }
    public int getColor2(){ return color2; }
    public int getFontColor(){ return fontColor; }
    public int getTopicFontColor(){ return topicFontColor; }
    public int getErrorFontColor(){ return errorFontColor; }
    public int  getEqualsFontColor(){return equalsFontColor;}
    public int getCheckColor(){return checkColor;}
    public int getErrorCheckColor(){return errorCheckColor;}

    public void setSelectMod(int m){ selectMod=m; }
    public void setColor1(int c){ color1=c; }
    public void setColor2(int c){ color2=c; }
    public void setFontColor(int c){ fontColor=c; }
    public void setTopicFontColor(int c){ topicFontColor=c; }
    public void setEqualsFontColor(int c){ equalsFontColor=c;}
    public void setCheckColor(int c){checkColor=c;}
    public void setErrorFontColor(int c){ errorFontColor=c; }
    public void setErrorCheckColor(int c){errorCheckColor=c;}

    public boolean equals(Setting setting){
        if(setting.getSelectMod()!=selectMod) return false;
        if(setting.getColor1()!=color1) return false;
        if(setting.getColor2()!=color2) return false;
        if(setting.getFontColor()!=fontColor) return false;
        if(setting.getTopicFontColor()!=topicFontColor) return false;
        if(setting.getErrorFontColor()!=errorFontColor) return false;
        if(setting.getEqualsFontColor()!=equalsFontColor) return false;
        if(setting.getCheckColor()!=checkColor)return false;
        if(setting.getErrorCheckColor()!=errorCheckColor)return false;
        return true;
    }

    public void copyFor(Setting setting){
        selectMod=setting.getSelectMod();
        color1=setting.getColor1();
        color2=setting.getColor2();
        fontColor=setting.getFontColor();
        topicFontColor=setting.getTopicFontColor();
        errorFontColor=setting.getErrorFontColor();
        equalsFontColor=setting.getEqualsFontColor();
        checkColor=setting.getCheckColor();
        errorCheckColor=setting.getErrorCheckColor();
    }
}
