package com.example.n_notepad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditUploadActivity extends AppCompatActivity {
    private EditText editMemoTitle,editMemoBody;
    private DataBaseHelper helper;
    private SQLiteDatabase db;
    String id;
    String title;
    String body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Title, Bodyの取得
        getMemo();

        //Title, Bodyの表示
        showMemo();

        //キャンセルボタン
        backToShow();

        //保存ボタン
        saveMemo();

    }

    
    //Title, Bodyの取得処理
    private void getMemo() {
        Intent intent = this.getIntent();
        // idを取得
        id = intent.getStringExtra("_id");

        // データベースから値を取得する
        if(helper == null){
            helper = new DataBaseHelper(EditUploadActivity.this);
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }

        // 画面に表示
        // データベースを取得する
        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            String[] projection = {
                    BaseColumns._ID,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                    FeedReaderContract.FeedEntry.COLUMN_NAME_BODY
            };
            // IDをキーにしてSELECTを行う
            String selection = BaseColumns._ID + " = ?";
            String[] selectionArgs = { id };

            Cursor cursor = db.query(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            while(cursor.moveToNext()) {
                // DBから取得したtitle,bodyをStringに変換
                title = cursor.getString(1);
                body = cursor.getString(2);
            }
        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
            db.close();
        }
    }

    //取得したTitle,Bodyの表示 
    private void showMemo() {
        editMemoTitle = (EditText) findViewById(R.id.editMemoTitle);
        editMemoBody = (EditText) findViewById(R.id.editMemoBody);
        editMemoTitle.setText(title);
        editMemoBody.setText(body);
    }

    //キャンセルボタン押下時処理
    private void backToShow() {
        Button cancelButton = findViewById(R.id.cancelMemoButton);
        //キャンセルボタン押下時処理
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUploadActivity.this, ShowActivity.class);
                int sendId = Integer.parseInt(id);
                intent.putExtra("_id", sendId);
                startActivity(intent);
            }
        });
    }

    //保存ボタン押下時処理
    private void saveMemo() {
        Button saveButton = findViewById(R.id.saveMemoButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBを参照しリストを作成
                if(helper == null){
                    helper = new DataBaseHelper(getApplicationContext());
                }
                db = helper.getWritableDatabase();
                // 画面の入力を取得
                editMemoTitle = (EditText) findViewById(R.id.editMemoTitle);
                editMemoBody = (EditText) findViewById(R.id.editMemoBody);
                // DBに登録用に変換
                String sendTitle = editMemoTitle.getText().toString();
                String sendBody = editMemoBody.getText().toString();

                // DB登録実行
                insertData(db, sendTitle, sendBody);

                // トップページに遷移
                Intent intent = new Intent(EditUploadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // DBへの登録処理
    private void insertData(SQLiteDatabase db, String title, String body){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BODY, body);
        // 更新登録
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { id };

        db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }
}
