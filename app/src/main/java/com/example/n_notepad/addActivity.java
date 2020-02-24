package com.example.n_notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class addActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button cancel2Button = findViewById(R.id.cancelMemoButton2);
        Button save2Button = findViewById(R.id.saveMemoButton2);

        //キャンセルボタン押下時処理
        cancel2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //保存ボタン押下時処理
        save2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });
    }
}
