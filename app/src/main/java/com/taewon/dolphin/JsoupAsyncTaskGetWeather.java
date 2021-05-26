package com.taewon.dolphin;

import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsoupAsyncTaskGetWeather extends AsyncTask<Void, Void, Void>
{
    Context context;
    Elements links;
    public JsoupAsyncTaskGetWeather(Context context, WebView wv){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document doc = Jsoup.connect("https://www.google.com/search?q=%EB%82%A0%EC%94%A8&oq=%EB%82%A0%EC%94%A8&aqs=chrome.0.69i59l3j69i61." +
                    "1040j0j9&sourceid=chrome&ie=UTF-8").get();
            links = doc.select("#wob_wc");
        } catch (Exception e) {
            System.out.println("실패");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        String data = links.text();
        System.out.println(data);
    }
}
