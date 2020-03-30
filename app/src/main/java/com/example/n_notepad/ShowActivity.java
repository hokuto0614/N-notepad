package com.example.n_notepad;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {
    TextView viewTitle,viewBody;
    private DataBaseHelper helper;
    private SQLiteDatabase db;
    String id;
    String title;
    String body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //Title, Bodyの表示
        showMemo();

        //編集ボタン
        editMemo();

        //削除ボタン
        deleteMemo();

        //一覧へ戻るボタン
        cancel();
    }
    

    //Title, Bodyの表示処理
    private void showMemo() {
        Intent intent = this.getIntent();
        // idを取得
        int getId = intent.getIntExtra("_id",0);
        id = String.valueOf(getId);

        // データベースから値を取得する

        if(helper == null){
            helper = new DataBaseHelper(ShowActivity.this);
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }

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

        // 画面に表示させる
        viewTitle = findViewById(R.id.showTitle);
        viewBody = findViewById(R.id.showBody);
        viewTitle.setText(title);
        viewBody.setText(body);
    }

    //編集ボタン押下時処理
    private void editMemo() {
        Button editButton = findViewById(R.id.editMemoButton);
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
    }

    //削除ボタン押下時処理
    private void deleteMemo() {
        Button deleteButton = findViewById(R.id.deleteMemoButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBを参照しリストを作成
                if(helper == null){
                    helper = new DataBaseHelper(ShowActivity.this);
                }
                db = helper.getReadableDatabase();
                // selectionArgsに渡すためにIDをString型に変換
                String getid = String.valueOf(id);
                // DELETE先の指定
                String selection = BaseColumns._ID + " = ?";
                String[] selectionArgs = { getid };
                // 削除実行
                db.delete(
                        FeedReaderContract.FeedEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                //　トップページに遷移する
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //一覧へ戻るボタン押下時処理
    private void cancel() {
        Button backButton = findViewById(R.id.backToMain);
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
