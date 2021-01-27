package com.example.bookdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.Result;

public class ResultActivity extends AppCompatActivity {

    // 書籍情報を管理するためのクラス(構造体)
    private class BookInfo{
        int ID;
        String title;
        String author;
        String publisher;
        int price;
        String isbn;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 入力された文字を受け取る
        Intent intent = getIntent();
        String query = intent.getStringExtra("QUERY");


        // ListViewに表示するデータを保持するArrayList
        final ArrayList<HashMap<String, String>> listData = new ArrayList<>();

        // 非同期で実行するサーバーとの通信処理を行うクラスのインスタンスを生成
        SearchTask task = new SearchTask();
        // サーバーから応答が帰ってきたときの処理を記述
        task.setListener(new SearchTask.Listener() {
            @Override
            public void onSuccess(String result){
                // サーバーから受け取ったJSON形式の検索結果をここに記載
                // 検索結果の書籍情報をBookInfoの配列として保持するためのArrayList
                final ArrayList<BookInfo> bookList = new ArrayList<BookInfo>();
                // JSON形式の検索結果(文字列)を、JSONArrayというクラスに展開
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    // JSONArrayに含まれているリスト要素の分だけ繰り返し処理を行うためのforループ
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // ここにJSON形式からデータを取り出す処理を記述
                        // JSON形式からデータを取り出し、BookInfoクラスのインスタンスに値を設定後、ArrayListに追加
                        final BookInfo bookInfo = new BookInfo();
                        bookInfo.ID = jsonObject.getInt("ID");
                        bookInfo.title = jsonObject.getString("TITLE");
                        bookInfo.author = jsonObject.getString("AUTHOR");
                        bookInfo.publisher = jsonObject.getString("PUBLISHER");
                        bookInfo.price = jsonObject.getInt("PRICE");
                        bookInfo.isbn = jsonObject.getString("ISBN");

                        bookList.add(bookInfo);

                        // ListViewに表示するタイトルと著者名を、JSON形式のデータから取り出したデータをもとに設定
                        listData.add(new HashMap() {
                            {put("title", bookInfo.title);}
                            {put("author", bookInfo.author);}
                        });
                    }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }





                // ListViewに表示するためのAdapterを生成
                SimpleAdapter adapter = new SimpleAdapter(ResultActivity.this,
                        listData, // ListViewに表示するデータ
                        android.R.layout.simple_list_item_2, // ListViewで使用するレイアウト(2つのテキスト)
                        new String[]{"title", "author"}, // 表示するHashMapのキー
                        new int[]{android.R.id.text1, android.R.id.text2} // データを表示するid

                );
                // ListViewの初期化設定
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);

                // ListView中の要素がタップされたときの処理を記述
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {



                        Intent intent = new Intent(ResultActivity.this, BookInfoActivity.class);

                        // 書籍の情報(id,タイトル,著者,出版社,価格,ISBN)をIntentに設定する
                        BookInfo bookInfo = bookList.get(position);
                        intent.putExtra("id", bookInfo.ID);
                        intent.putExtra("title", bookInfo.title);
                        intent.putExtra("author", bookInfo.author);
                        intent.putExtra("publisher", bookInfo.publisher);
                        intent.putExtra("price", bookInfo.price);
                        intent.putExtra("isbn", bookInfo.isbn);


                        startActivity(intent);
                    }
                });

            }
        });

        // サーバーとの通信を非同期で起動
        task.execute(query);
    }


}