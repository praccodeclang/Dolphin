//package com.taewon.dolphin;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class JsoupAsyncTask extends AsyncTask<Void, Void, Void>
//{
//    String PageUrl;
//    Context context;
//    List<NoticeItem> noticeItemList;
//    ListView listView;
//    public JsoupAsyncTask(Context context, String PageUrl, ListView listView){
//        this.context = context;
//        this.PageUrl = PageUrl;
//        noticeItemList = new ArrayList<>();
//        this.listView = listView;
//    }
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//        try {
//            Document doc = Jsoup.connect(PageUrl).get();
//            Elements links = doc.select(".isnotice");
//            Iterator<Element> it = links.iterator();
//            while(it.hasNext())
//            {
//                Element instance = it.next();
//                String title = instance.select(".f-tit.subject span b").text()=="" ? instance.select(".f-tit.subject span").text() : instance.select(".f-tit.subject span b").text();
//                String url = instance.select(".f-tit.subject a").attr("href");
//                String date = instance.select(".f-date.date p").text();
//                for(int i=0; i<3; i++)
//                {
//                    String title, url, date;
//                }
//                NoticeAdapter noticeAdapter = new NoticeAdapter(context, freeBoardItemList);
//                mainFreeBoardListView.setAdapter(freeBoardAdapter);
//                setListViewHeightBasedOnChildren(mainFreeBoardListView);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        test.setText(strings);
//    }
//}
