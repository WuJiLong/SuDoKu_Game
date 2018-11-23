package com.example.wuqilong.sudoku_game.SuDoKu_class;

import android.util.Log;

import com.example.wuqilong.sudoku_game.SuDoKu_setting_Activity;

public class Sulution_operation {
    public int[][] table;
    public int[][] ans;
    public Sulution_operation(){
        table=new int[9][];
        ans=new int[9][];
        for(int i=0;i<9;i++){
            table[i]=new int[9];
            ans[i]=new int[9];
            for(int j=0;j<9;j++){
                table[i][j]=0;
                ans[i][j]=0;
            }
        }
    }
    public boolean operation(){
        for(int i=0;i<81;i++){
            ans[i/9][i%9]=table[i/9][i%9];
        }
        int ptr=0;
        while(true){
            Log.i("SuDoKu",String.valueOf(ptr));
            if(ptr<0) return false;
            if(ptr>80) return true;
            if(getNUM(ptr)!=0){
                ptr++;
            }else{
                while(true){
                    ans[ptr/9][ptr%9]++;
                    if(ans[ptr/9][ptr%9]>=10){
                        ans[ptr/9][ptr%9]=0;
                        ptr--;
                        while(ptr>=0 && getNUM(ptr)!=0)
                            ptr--;

                        break;
                    }else if(checkTheNUMforAns(ptr)){
                        ptr++;
                        break;
                    }
                }
            }
        }
    }
    public void pushNUM(int local,int num){
        if(num!=0 && table[local/9][local%9]==0)
            table[local/9][local%9]=num;
        else if(num==0)
            table[local/9][local%9]=0;
    }
    public int getNUM(int local){
        return table[local/9][local%9];
    }
    public void clearALL(){
        for(int i=0;i<81;i++){
            table[i/9][i%9]=0;
            ans[i/9][i%9]=0;
        }
    }
    private boolean checkTheNUMforAns(int local){
        int x=local%9;
        int y=local/9;
        if(ans[y][x]==0) return true;
        int localNUM=ans[y][x];
        int chunkX=x/3;
        int chunkY=y/3;
        for(int i=0;i<9;i++){
            if(ans[y][i]==localNUM &&i!=x) return false;
            if(ans[i][x]==localNUM &&i!=y) return false;
            if(ans[chunkY*3 + i/3][chunkX*3 + i%3]==localNUM && !(chunkY*3 + i/3==y && chunkX*3 + i%3==x))return false;
        }
        return true;
    }

    public boolean checkTheNUM(int local){
        int x=local%9;
        int y=local/9;
        if(table[y][x]==0) return true;
        int localNUM=table[y][x];

        int chunkX=x/3;
        int chunkY=y/3;

        for(int i=0;i<9;i++){
            if(table[y][i]==localNUM &&i!=x) return false;
            if(table[i][x]==localNUM &&i!=y) return false;
            if(table[chunkY*3 + i/3][chunkX*3 + i%3]==localNUM && !(chunkY*3 + i/3==y && chunkX*3 + i%3==x))return false;
        }
        return true;
    }

}
