package com.example.n_notepad;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {
    TextView viewTitle,viewBody;
    int id =0;
    String title ="";
    String body = "";
    private DataBaseHelper helper;
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // mainActivityから受け取った値を取得
        Intent intent = getIntent();
        id = intent.getIntExtra("_id",0);
        title = intent.getStringExtra("title");
        body = intent.getStringExtra("body");

        // 画面に表示させる
        viewTitle = findViewById(R.id.showTitle);
        viewBody = findViewById(R.id.showBody);
        viewTitle.setText(title);
        viewBody.setText(body);

        // 各ボタンを表示させる
        Button editButton = findViewById(R.id.editMemoButton);
        Button deleteButton = findViewById(R.id.deleteMemoButton);
        Button backButton = findViewById(R.id.backToMain);

        //編集ボタン押下時処理
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 編集画面に遷移する
                Intent intent = new Intent(ShowActivity.this, EditUploadActivity.class);
                // 編集画面にてDBをIDで検索するため引き継がせる。
                intent.putExtra("_id", id);
                startActivity(intent);
            }
        });

        //削除ボタン押下時処理
        //Todo 削除時DB処理
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBを参照しリストを作成
                if(helper == null){
                    helper = new DataBaseHelper(ShowActivity.this);
                }
                if(db == null){
                    db = helper.getReadableDatabase();
                }
                // selectionArgsに渡すためにIDをString型に変換
                String getid = String.valueOf(id);
                // DELETE先の指定
                String selection = BaseColumns._ID + " = ?";
                String[] selectionArgs = { getid };
                // 削除実行
                db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
                //　トップページに遷移する
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //一覧へ戻るボタン押下時処理
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //　トップページに遷移する
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
