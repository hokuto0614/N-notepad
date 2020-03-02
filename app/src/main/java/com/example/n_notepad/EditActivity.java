package com.example.n_notepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class EditActivity  extends AppCompatActivity {

    // DataBaseHelperクラスを定義
    DataBaseHelper helper = null;
    // 新規フラグ
    boolean newFlag = false;
    // id
    String id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // データベースから値を取得する
        if(helper == null){
            helper = new DataBaseHelper(EditActivity.this);
        }
        // ListActivityからインテントを取得
        Intent intent = this.getIntent();
        // 値を取得
        id = intent.getStringExtra("id");
        // 画面に表示
        if(id.equals("")){
            // 新規作成の場合
            newFlag = true;
        }else {
            // 編集の場合 データベースから値を取得して表示
            // データベースを取得する
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                // rawQueryというSELECT専用メソッドを使用してデータを取得する
                Cursor c = db.rawQuery("select body from MEMO_TABLE where uuid = '" + id + "'", null);
                // Cursorの先頭行があるかどうか確認
                boolean next = c.moveToFirst();
                // 取得した全ての行を取得
                while (next) {
                    // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                    String dispBody = c.getString(0);
                    EditText body = (EditText) findViewById(R.id.editMemoContent);
                    body.setText(dispBody, TextView.BufferType.NORMAL);
                    next = c.moveToNext();
                }
            } finally {
                // finallyは、tryの中で例外が発生した時でも必ず実行される
                // dbを開いたら確実にclose
                db.close();
            }
        }

        Button cancelButton = findViewById(R.id.cancelMemoButton);
        Button saveButton = findViewById(R.id.saveMemoButton);

        //キャンセルボタン押下時処理
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });

        //保存ボタン押下時処理
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 入力内容を取得する
                EditText title = (EditText) findViewById(R.id.editMemoTitle);
                EditText body = (EditText) findViewById(R.id.editMemoContent);
                String titleStr = title.getText().toString();
                String bodyStr = body.getText().toString();

                // データベースに保存する
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    if (newFlag) {
                        // 新規作成の場合
                        // 新しくidを発行する
                        id = UUID.randomUUID().toString();
                        // INSERT
                        db.execSQL("insert into MEMO_TABLE(uuid, title, body) VALUES('" + id + "', '" + bodyStr + "', '" + titleStr + "')");
                    } else {
                        // UPDATE
                        db.execSQL("update MEMO_TABLE set title = '" + titleStr + "', body = '" + bodyStr + "' where uuid = '" + id + "'");
                    }
                } finally {
                    // finallyは、tryの中で例外が発生した時でも必ず実行される
                    // dbを開いたら確実にclose
                    db.close();
                }
                // 保存後に一覧へ戻る
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
