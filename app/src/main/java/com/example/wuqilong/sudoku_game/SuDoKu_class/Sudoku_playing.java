package com.example.wuqilong.sudoku_game.SuDoKu_class;

public class Sudoku_playing {
    Sudoku_Topic topic;
    int Count;
    public int myAns[][]={
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}};
    public boolean mark[][];
    public Sudoku_playing(Sudoku_Topic topic){
        this.topic=topic;
        Count=0;
        for(int i=0;i<81;i++){
            if(topic.show[i/9][i%9]){
                myAns[i/9][i%9]=topic.ans[i/9][i%9];
                Count++;
            }
        }
        mark=topic.show;
    }
    public boolean checkAns(){
        if(Count<81) return false;
        for(int i=0;i<9;i++){
            int chunk_x=(i%3)*3;
            int chunk_y=(i/3)*3;
            boolean x[]={true,true,true,true,true,true,true,true,true};
            boolean y[]={true,true,true,true,true,true,true,true,true};
            boolean block[]={true,true,true,true,true,true,true,true,true};
            for(int j=0;j<9;j++){

                if(x[myAns[i][j]-1])  x[myAns[i][j]-1]=false;//檢查橫
                else    return false;

                if(y[myAns[j][i]-1])   y[myAns[j][i]-1]=false;//檢查列
                else     return false;

                int dx=j%3;
                int dy=j/3;
                if(block[myAns[chunk_y+dy][chunk_x+dx]-1]) block[myAns[chunk_y+dy][chunk_x+dx]-1]=false;
                else     return false;
            }
        }
        return true;
    }
    public int getNumberCount(int number){
        int count=0;
        for(int i=0;i<81;i++){
            if(myAns[i/9][i%9]==number)
                count++;
        }
        return count;
    }
    public void randTopic(){
        topic.randomAns();
        Count=0;
        for(int i=0;i<81;i++){
            if(topic.show[i/9][i%9]){
                myAns[i/9][i%9]=topic.ans[i/9][i%9];
                Count++;
            }else{
                myAns[i/9][i%9]=0;
            }
        }
    }
    public int getNUM(int local){
        return myAns[local/9][local%9];

    }
    public void pushNUM(int local,int num){
        if(num!=0 && myAns[local/9][local%9]==0) {
            myAns[local / 9][local % 9] = num;
            Count++;
        }else if(num==0 && !mark[local/9][local%9] && myAns[local/9][local%9]!=0){
            myAns[local/9][local%9]=0;
            Count--;
        }
    }
    public void clearALL(){
        Count=0;
        for(int i=0;i<81;i++){
            if(topic.show[i/9][i%9]){
                myAns[i/9][i%9]=topic.ans[i/9][i%9];
                Count++;
            }else{
                myAns[i/9][i%9]=0;
            }
        }
    }
    public boolean checkTheNUM(int local){
        int x=local%9;
        int y=local/9;
        if(myAns[y][x]==0) return true;
        int localNUM=myAns[y][x];

        int chunkX=x/3;
        int chunkY=y/3;

        for(int i=0;i<9;i++){
            if(myAns[y][i]==localNUM &&i!=x) return false;
            if(myAns[i][x]==localNUM &&i!=y) return false;
            if(myAns[chunkY*3 + i/3][chunkX*3 + i%3]==localNUM && !(chunkY*3 + i/3==y && chunkX*3 + i%3==x))return false;
        }
        return true;
    }
}
