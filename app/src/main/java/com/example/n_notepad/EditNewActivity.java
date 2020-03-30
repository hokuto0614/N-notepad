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

public class EditNewActivity extends AppCompatActivity {
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

        //Title,Bodyの表示 
        showMemo();

        //キャンセルボタン
        backToMain();

        //保存ボタン
        saveMemo();

    }

    // title、bodyの値を画面に表示させるための処理
    private void showMemo(){
        editMemoTitle = (EditText) findViewById(R.id.editMemoTitle);
        editMemoBody = (EditText) findViewById(R.id.editMemoBody);
        editMemoTitle.setText(title);
        editMemoBody.setText(body);
    }

    //キャンセルボタン押下時処理
    private void backToMain(){
        Button cancelButton = findViewById(R.id.cancelMemoButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditNewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //保存ボタン押下時処理
    private void saveMemo(){
        Button saveButton = findViewById(R.id.saveMemoButton);
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
                Intent intent = new Intent(EditNewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // DBへの登録処理
    private void insertData(SQLiteDatabase db, String title, String body){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BODY, body);
        // 新規作成
        db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values
        );
    }
}
