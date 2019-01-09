package com.example.wuqilong.sudoku_game.SuDoKu_class;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuqilong.sudoku_game.R;
import com.example.wuqilong.sudoku_game.define.Setting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sudoku_Topic_Data {
    static private final String KEY="com.example.wuqilong.sudokugame.topic.data";//數獨資料Key值
    static private final String OLDDATE_KEY="OLDDATE";//設定檔案中的KEY值
    static private final String TOPIC_NUMBER_KEY="TOPICNUMBER";//設定檔案中的KEY值
    static private final String TOPIC_BEGIN_KEY="TOPIC_BEGIN";//設定檔案中的KEY值
    static private Date oldDate;
    static public boolean reading=false;

    static public Date getOldDate(){
        SharedPreferences spref = Setting.activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);
        String dateString=spref.getString(OLDDATE_KEY,"199711201200");
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmm");
        oldDate = simpledateformat.parse(dateString, pos);
        return oldDate;
    }
    static public void saveTopicForFIleBase(Date version, final View view){
        reading=true;
        final Date date=version;
        DatabaseReference reference_contacts = FirebaseDatabase.getInstance().getReference("topic_list");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                //Toast.makeText(newDateTV.getContext(), "版本號已抓取", Toast.LENGTH_SHORT).show();
                // ...
                SharedPreferences spref = Setting.activity.getApplication()
                        .getSharedPreferences(KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spref.edit();
                editor.clear();//清除所有設定

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String dateString = sdf.format(date);
                editor.putString(OLDDATE_KEY,dateString);
                int count=0;//計數
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    String topic = ds.child("topic").getValue().toString();
                    editor.putString(TOPIC_BEGIN_KEY+String.valueOf(count),topic);
                    count++;
                }
                editor.putInt(TOPIC_NUMBER_KEY,count);
                editor.commit();//儲存設定
                Toast.makeText(Setting.activity, "題目"+String.valueOf(count)+"題 已抓取完成", Toast.LENGTH_SHORT).show();
                //oldDateTV.setText(dateString);
                ((TextView)view.findViewById(R.id.date_tv)).setText(dateString);
                ((TextView)view.findViewById(R.id.topic_num_tv)).setText("題目數量:"+String.valueOf(count));
                reading=false;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        reference_contacts.addListenerForSingleValueEvent(listener);//只抓取一次資料
    }
    static public int getTopinNumber(){
        SharedPreferences spref = Setting.activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return spref.getInt(TOPIC_NUMBER_KEY,0);
    }
    static public List<Sudoku_Topic> getTopicList(){
        List<Sudoku_Topic> topicList=new ArrayList<>();
        SharedPreferences spref = Setting.activity.getApplication()
                .getSharedPreferences(KEY, Context.MODE_PRIVATE);
        int count =spref.getInt(TOPIC_NUMBER_KEY,0);
        for(int i=0;i<count;i++){
            String topicString=spref.getString(TOPIC_BEGIN_KEY+String.valueOf(i),null);
            if(topicString==null) continue;
            int ptr=0;
            Sudoku_Topic topic=new Sudoku_Topic();
            int ct=0;
            while(ptr<topicString.length()){
                char c=topicString.charAt(ptr);
                if(ct<81){
                    if(c>='1'&&c<='9'){
                        topic.ans[ct/9][ct%9]=c-0x30;
                        ct++;
                    }
                }else if(ct<162){
                    if(c=='T'|| c=='F' || c=='t' || c=='f'){
                        topic.show[(ct-81)/9][ct%9] = ((c=='T')||(c=='t'));
                        ct++;
                    }
                }
                ptr++;
            }
            if(topic.isReasonable()){//如果題目合法
                topicList.add(topic);
            }
        }
        return topicList;
    }
}
