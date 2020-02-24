package com.example.n_notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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
                Intent intent = new Intent(EditActivity.this, ShowActivity.class);
                startActivity(intent);
            }

            //ToDo 保存時DB処理

        });
    }

}
