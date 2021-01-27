package com.example.bookdatabase;

//import android.location.GnssAntennaInfo;
//import android.location.GpsStatus;
//import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.util.Log;
//import android.net.sip.SipSession.Listener;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



public class SearchTask extends AsyncTask<String, Void, String> {
    private Listener listener;

    // サーバーとの通信処理を記述する
    protected String doInBackground(String... params){
        // 返り値として返す変数result
        String result = "[]";

        // 接続先のURLを指定してサーバーに接続する
        try {
            // "xxxxxxxxxx部分にローカルホストのIPアドレスを指定"
            URL url = new URL("xxxxxxxxxxxxx:8000/cgi-bin/booksearch_json.py");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // サーバーに、入力されたキーワードを送る
            try {
                urlConnection.setDoOutput(true);
                OutputStream out = urlConnection.getOutputStream();
                String query = "query=" + params[0]; // query=Androidなど
                try {
                    out.write(query.getBytes("UTF-8"));
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    out.close();
                }

                // サーバーからJSON形式の検索結果を受け取る.結果は変数resultに格納
                InputStream in = urlConnection.getInputStream();
                try {
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String str;
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    result = buffer.toString();


                    Log.d("debug", "onSuccess() called. result = " + result); // resultを確認
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    in.close();
                }

                // 例外処理
            }catch (Exception e){
                            e.printStackTrace();
                }finally{
                            urlConnection.disconnect();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            // 結果(変数resultの値)を返す
            return result;
        }

    protected void onPostExecute(String result){
        super.onPostExecute(result);

        // サーバーとの通信が終了したら、画面を更新する
        if (listener != null){
            listener.onSuccess(result);
        }
    }

    // 画面更新処理を登録するためのメソッド
    void setListener(Listener listener){
        this.listener = listener;
    }

    // 画面更新処理を呼び出すためのインタフェース
    interface Listener{
        void onSuccess(String result);
    }

}
