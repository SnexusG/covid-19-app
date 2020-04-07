package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class statsTemp extends AppCompatActivity {

    //state-wise stats
    private String[][] stats = new String[31][4];
    private String[] news = new String[5];
    private String totalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_temp);

            Content content = new Content();
            content.execute();

            Content2 content2 = new Content2();
            content2.execute();

    }

    private class Content2 extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("NEWS");
            for(String i : news){
                System.out.println(i);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://www.moneycontrol.com/news/india/coronavirus-india-news-live-updates-covid-19-pandemic-us-delhi-maharashtra-kerala-statewise-tally-death-toll-lockdown-news-5114201.html";
            try {
                Document doc = Jsoup.connect(url).get();
                Elements data = doc.select("ul.live-blog");

                for(int i = 0; i < 5; i++) {
                    String text = data.select("li")
                            .eq(i)
                            .select("p")
                            .text();
                    int size = data.size();
                    news[i] = text;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class Content extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i = 0; i < 31; i++){
                for(int j = 0; j < 4; j++){
                    System.out.print(stats[i][j]+ " ");
                }
                System.out.println();
            }
            System.out.println("Total Cases : " + totalCount);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String url = "https://www.mohfw.gov.in/";
                Document doc = Jsoup.connect(url).get();
                Elements data = doc.select("div.col-xs-12");
                int size = data.size();

             for(int i = 0; i < 31; i++) {
                 for(int j = 1; j < 5; j++) {
                     String text = data.select("tbody")
                             .select("tr")
                             .eq(i)
                             .select("td")
                             .eq(j)
                             .text();
                     stats[i][j-1] = text;
                 }
             }

             totalCount = data.select("tbody")
                     .select("tr")
                     .eq(31)
                     .select("td")
                     .eq(1)
                     .text();


            }catch(Exception e){
                System.out.println("Error");
                e.printStackTrace();
            }
            return null;
        }
    }
}
