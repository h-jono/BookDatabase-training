package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onSearchButtonClicked(View view){
        Intent intent = new Intent(this, ResultActivity.class);
        EditText keywordText = findViewById(R.id.keywordText);  // UIであるEditTextへの参照をIDによって取得、
        // 入力された文字列を"QUERY"というキーのバリューとしてIntentに登録
        intent.putExtra("QUERY", keywordText.getText().toString());

        startActivity(intent);
    }

}