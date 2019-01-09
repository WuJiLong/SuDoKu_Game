package com.example.wuqilong.sudoku_game.SuDoKu_class;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuqilong.sudoku_game.define.Setting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    static public void saveTopicForFIleBase(Date version, final TextView oldDateTV){
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
                Toast.makeText(Setting.activity, "題目"+String.valueOf(count)+"題已抓取完成", Toast.LENGTH_SHORT).show();
                oldDateTV.setText(dateString);
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
}
