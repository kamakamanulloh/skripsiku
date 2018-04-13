package com.kamak.skripsweetnewwes.adapter;

/**
 * Created by chukamak on 28/11/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kamak.skripsweetnewwes.R;
import com.kamak.skripsweetnewwes.app.AppController;
import com.kamak.skripsweetnewwes.data.NewsData;

import java.util.List;

public class komenadapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<NewsData> newsItems;
    ImageLoader imageLoader;

    public komenadapter(Activity activity, List<NewsData> newsItems) {
        this.activity = activity;
        this.newsItems = newsItems;
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return newsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_koment, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView timestamp = (TextView) convertView.findViewById(R.id.txt_tgl);
        TextView isi = (TextView) convertView.findViewById(R.id.txt_isi);
        TextView user =(TextView)convertView.findViewById(R.id.txt_nama);
        NewsData news = newsItems.get(position);


        timestamp.setText(news.getDatetime());
        isi.setText(Html.fromHtml(news.getketerangan()));
        user.setText(news.getNama());
        return convertView;
    }

}
