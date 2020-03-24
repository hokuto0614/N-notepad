package com.example.n_notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private DataBaseHelper helper;
    private SQLiteDatabase db;
    // 編集の時に渡すIDのリスト
    ArrayList idList = new ArrayList();
    // リストに表示させる、編集の時に渡すタイトルのリスト
    ArrayList titleList = new ArrayList();
    // 編集画面に渡すbodyのリスト
    // ArrayList bodyList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // メモのデータを格納する変数
        Map<Integer, Map<String, String>> memoMap = new HashMap<>();

        // DBから取得したメモを格納
        memoMap = getMemoMap();

        // memoリストの表示
        showList(memoMap);

        //新規作成ボタン
        createNewMemo();
    }


    // DBからメモのリストを取得
    private Map<Integer,String> getMemoMap() {
        // 返却用のMAP
        // Map<Integer, Map<String, String>> getMemoMap = new HashMap<>();
        Map<Integer,String> getMemoMap = new HashMap<>();

        // DBから取得する値
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                // FeedReaderContract.FeedEntry.COLUMN_NAME_BODY
        };

        //DBを参照しリストを作成
        if(helper == null){
            helper = new DataBaseHelper(MainActivity.this);
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        try {
            //データを取得する
            Cursor cursor = db.query(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            // DB取得したした値を格納する
            while(cursor.moveToNext()) {
                // HashMap<String,String> map = new HashMap<>();
                int itemId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                String getTitle = cursor.getString(1);
                // String getBody = cursor.getString(2);
                // map.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,getTitle);
                // map.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BODY,getBody);
                getMemoMap.put(itemId,getTitle);
            }
        } finally {
            // dbを開いたら確実にclose
            db.close();
        }
        return getMemoMap;
    };

    //リストを表示させる処理
    private void showList(Map<Integer, Map<String, String>> memoMap) {

        ListView listView = (ListView)findViewById(R.id.listView);

        // showActivityにインテントする値をそれぞれListに格納する。
        for(Map.Entry<Integer,String>showMap : memoMap.entrySet()){
            idList.add(showMap.getKey());
            titleList.add(showMap.getValue().get(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
            // bodyList.add(showMap.getValue().get(FeedReaderContract.FeedEntry.COLUMN_NAME_BODY));
        }
        //　リストの作成
        ArrayAdapter arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleList);
        listView.setAdapter(arrayAdapter);

        // リスト項目が選択時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                int sendId = (int) idList.get(position);
                String sendTitle = (String) titleList.get(position);
                // String sendBody = (String) bodyList.get(position);

                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                // 各値をインテントに格納。
                intent.putExtra("_id", sendId);
                // intent.putExtra("title", sendTitle);
                // intent.putExtra("body", sendBody);
                startActivity(intent);
            }
        });
    }

    //新規作成ボタン処理
    private void createNewMemo() {
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditNewActivity.class);
                startActivity(intent);
            }
        });
    }
}
