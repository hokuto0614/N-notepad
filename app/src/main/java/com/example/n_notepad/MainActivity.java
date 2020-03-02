package com.example.n_notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    // DataBaseHelperクラスを定義
    DataBaseHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBを参照しリストを作成
        if(helper == null){
            helper = new DataBaseHelper(MainActivity.this);
        }
        // メモリストデータを格納する変数
        ArrayList<HashMap<String, String>> memoList = new ArrayList<>();
        // データベースを取得する
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //データを取得する
            Cursor c = db.rawQuery("select uuid, title, body from MEMO_TABLE order by id", null);
            // Cursorの先頭行があるかどうか確認
            boolean next = c.moveToFirst();

            // 取得した全ての行を取得
            while (next) {
                HashMap<String,String> data = new HashMap<>();
                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                String title = c.getString(1);
                String body = c.getString(2);
                if(title.length() > 10){
                    // リストに表示するのは10文字まで
                    title = title.substring(0, 11) + "...";
                }
                data.put("body",body);
                data.put("title",title);
                memoList.add(data);
                // 次の行が存在するか確認
                next = c.moveToNext();
            }
        } finally {
            // dbを開いたら確実にclose
            db.close();
        }
        //新規作成ボタン押下時処理
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivity(intent);
            }
        });

    }
}
