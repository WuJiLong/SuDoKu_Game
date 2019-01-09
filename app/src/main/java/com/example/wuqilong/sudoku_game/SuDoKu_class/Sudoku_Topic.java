package com.example.wuqilong.sudoku_game.SuDoKu_class;

import java.util.Random;

public class Sudoku_Topic {

    public int ans[][]={
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}};//答案
    public boolean show[][]={
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false}};//屏蔽

    public Sudoku_Topic(){

    }

    public boolean isReasonable(){//檢查題目的合法性 避免題目輸入錯誤
        for(int i=0;i<9;i++){

            int chunk_x=(i%3)*3;
            int chunk_y=(i/3)*3;
            boolean x[]={true,true,true,true,true,true,true,true,true};
            boolean y[]={true,true,true,true,true,true,true,true,true};
            boolean block[]={true,true,true,true,true,true,true,true,true};
            for(int j=0;j<9;j++){
                if(ans[i][j]>9||ans[i][j]<=0) return false;//當中有數值不合法

                if(x[ans[i][j]-1])  x[ans[i][j]-1]=false;//檢查橫
                else    return false;

                if(y[ans[j][i]-1])   y[ans[j][i]-1]=false;//檢查列
                else     return false;

                int dx=j%3;
                int dy=j/3;
                if(block[ans[chunk_y+dy][chunk_x+dx]-1]) block[ans[chunk_y+dy][chunk_x+dx]-1]=false;
                else     return false;
            }
        }
        return true;
    }

    public void randomAns(){
        //-------替換數字-------
        int nur[]={1,2,3,4,5,6,7,8,9};
        Random rand=new Random(System.currentTimeMillis());
        for(int i=0;i<9;i++){
            int n=rand.nextInt(nur.length);
            int r=nur[i];nur[i]=nur[n];nur[n]=r;
        }
        for(int i=0;i<81;i++){
            ans[i/9][i%9]=nur[ans[i/9][i%9]-1];
        }

        /*
        //-------翻轉矩陣-------
        int ans_r[][]={{0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}};
        boolean show_r[][]={
                {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false},
                {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false}, {false,false,false,false,false,false,false,false,false}};
        if(rand.nextInt(2)==0){//左右翻轉
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    ans_r[i][j]=ans[i][8-j];
                    show_r[i][j]=show[i][8-j];
                }
            }
            int a_r[][]=ans;//記憶體位置對調
            ans=ans_r;
            ans_r=a_r;
            boolean s_r[][]=show;
            show=show_r;
            show_r=s_r;
        }
        if(rand.nextInt(2)==0){//上下翻轉
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    ans_r[i][j]=ans[8-i][j];
                    show_r[i][j]=show[8-i][j];
                }
            }
            int a_r[][]=ans;
            ans=ans_r;
            ans_r=a_r;
            boolean s_r[][]=show;
            show=show_r;
            show_r=s_r;
        }
        if(rand.nextInt(2)==0){//轉置矩陣
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    ans_r[i][j]=ans[j][i];
                    show_r[i][j]=show[j][i];
                }
            }
            int a_r[][]=ans;
            ans=ans_r;
            ans_r=a_r;
            boolean s_r[][]=show;
            show=show_r;
            show_r=s_r;
        }
        if(rand.nextInt(2)==0){//斜翻轉矩陣
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    ans_r[i][j]=ans[8-j][8-i];
                    show_r[i][j]=show[8-j][8-i];
                }
            }
            ans=ans_r;
            show=show_r;
        }
        //*/
    }
}
