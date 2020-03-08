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

public class EditActivity extends AppCompatActivity {
    private EditText editMemoTitle,editMemoBody;
    private DataBaseHelper helper;
    private SQLiteDatabase db;
    // 新規作成フラグ
    boolean newFlag = false;
    String id;
    String title;
    String body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // データベースから値を取得する
        if(helper == null){
            helper = new DataBaseHelper(EditActivity.this);
        }

        Intent intent = this.getIntent();
        // idを取得
        int getId = intent.getIntExtra("_id",0);
        id = String.valueOf(getId);
        // 画面に表示
        if(id.equals("0")){
            // 新規作成の場合
            newFlag = true;
        }else {
            // 編集の場合 データベースから値を取得して表示
            // データベースを取得する
            SQLiteDatabase db = helper.getWritableDatabase();
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
        // title、bodyの値を画面に表示させるための処理
        editMemoTitle = (EditText) findViewById(R.id.editMemoTitle);
        editMemoBody = (EditText) findViewById(R.id.editMemoBody);
        editMemoTitle.setText(title);
        editMemoBody.setText(body);

        Button cancelButton = findViewById(R.id.cancelMemoButton);
        Button saveButton = findViewById(R.id.saveMemoButton);

        //キャンセルボタン押下時処理
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //保存ボタン押下時処理
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBを参照しリストを作成
                if(helper == null){
                    helper = new DataBaseHelper(getApplicationContext());
                }
                if(db == null){
                    db = helper.getWritableDatabase();
                }

                // 画面の入力を取得
                editMemoTitle = (EditText) findViewById(R.id.editMemoTitle);
                editMemoBody = (EditText) findViewById(R.id.editMemoBody);
                // DBに登録用に変換
                String sendTitle = editMemoTitle.getText().toString();
                String sendBody = editMemoBody.getText().toString();

                // DB登録実行
                insertData(db, sendTitle, sendBody);

                // トップページに遷移
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    // DBへの登録処理
    private void insertData(SQLiteDatabase db, String title, String body){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BODY, body);
        if (newFlag) {
            // 新規作成の場合
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }else{
            // 更新登録の場合
            String selection = BaseColumns._ID + " = ?";
            String[] selectionArgs = { id };

            db.update(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }
}
