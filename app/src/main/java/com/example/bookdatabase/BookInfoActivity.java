package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        // Intentからの情報の取り出し
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        final String title = intent.getStringExtra("title");
        final String author = intent.getStringExtra("author");
        String publisher = intent.getStringExtra("publisher");
        String price = Integer.toString(intent.getIntExtra("price", 0));
        String isbn = intent.getStringExtra("isbn");

        // UIコンポーネントへの参照の取得
        final TextView titleView = (TextView) findViewById(R.id.titleView);
        TextView authorView = (TextView) findViewById(R.id.authorView);
        TextView publisherView = (TextView) findViewById(R.id.publisherView);
        TextView priceView = (TextView) findViewById(R.id.priceView);
        TextView isbnView = (TextView) findViewById(R.id.isbnView);
        Button webSearchButton = (Button) findViewById(R.id.webSearchButton);

        // 書籍情報の表示
        titleView.setText(title);
        authorView.setText(author);
        publisherView.setText(publisher);
        priceView.setText(price);
        isbnView.setText(isbn);

        //webで検索ボタンの実装
        webSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent()
                        // web検索の指定
                        .setAction(Intent.ACTION_WEB_SEARCH)
                        .putExtra(SearchManager.QUERY,
                                titleView.getText().toString()));
            }
        });


    }
}