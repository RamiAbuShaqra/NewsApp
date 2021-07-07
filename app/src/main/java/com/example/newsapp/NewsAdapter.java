package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOCATION_SEPARATOR = "T";

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is a recycled view to be used, otherwise create one.
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_sample, parent, false);
        }

        News currentNews = getItem(position);

        // Find the TextView by id and set its text to be the content title.
        TextView contentTitle = convertView.findViewById(R.id.content_title);
        contentTitle.setText(currentNews.getContentTitle());

        // Find the TextView by id and set its text to be the section name.
        TextView sectionName = convertView.findViewById(R.id.section_name);
        sectionName.setText(currentNews.getSectionName());

        // Find the TextView by id and set its text to be the author name.
        TextView authorName = convertView.findViewById(R.id.author_name);
        authorName.setText(currentNews.getAuthorName());

        // Find the TextView by id and set its text to be the published date.
        TextView publishedDate = convertView.findViewById(R.id.published_date);
        String date = dateFormatter(currentNews.getPublishedDate());
        publishedDate.setText(date);

        return convertView;
    }

    /** This helper method is used to re-format the date received in the JSON response.
     *
     * First, it removes the time part and keeps the date part only.
     * Second, it reverse the date format to be (dd-MM-yyyy) instead of (yyyy-MM-dd).
     *
     * @param publishedDate The published date received from the JSON response.
     * @return a new formatted date.
     */
    private String dateFormatter(String publishedDate) {
        String[] subDate = publishedDate.split(LOCATION_SEPARATOR);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date;
        String updatedDate = null;

        try {
            date = inputFormat.parse(subDate[0]);
            updatedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return updatedDate;
    }
}
