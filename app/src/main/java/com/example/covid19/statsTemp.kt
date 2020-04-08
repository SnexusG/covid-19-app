package com.example.covid19

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup

class statsTemp : AppCompatActivity() {
    //state-wise stats
    private val stats = Array(31) { arrayOfNulls<String>(4) }
    private val news = arrayOfNulls<String>(5)
    private var totalCount: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats_temp)
        val content = Content_Stats()
        content.execute()
        val content2 = Content_News()
        content2.execute()
    }

    private inner class Content_News : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            println("NEWS")
            for (i in news) {
                println(i)
            }
        }

        override fun onCancelled() {
            super.onCancelled()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val url = "https://www.moneycontrol.com/news/india/coronavirus-india-news-live-updates-covid-19-pandemic-us-delhi-maharashtra-kerala-statewise-tally-death-toll-lockdown-news-5114201.html"
            try {
                val doc = Jsoup.connect(url).get()
                val data = doc.select("ul.live-blog")
                for (i in 1..5) {
                    val text = data.select("li")
                            .eq(i)
                            .select("p")
                            .text()
                    val size = data.size
                    news[i-1] = text
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


    }

    private inner class Content_Stats : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            for (i in 0..30) {
                for (j in 0..3) {
                    print(stats[i][j].toString() + " ")
                }
                println()
            }
            println("Total Cases : $totalCount")
        }

        override fun onCancelled(aVoid: Void?) {
            super.onCancelled(aVoid)
        }

         override fun doInBackground(vararg params: Void?): Void? {
            try {
                val url = "https://www.mohfw.gov.in/"
                val doc = Jsoup.connect(url).get()
                val data = doc.select("div.col-xs-12")
                val size = data.size
                for (i in 0..30) {
                    for (j in 1..4) {
                        val text = data.select("tbody")
                                .select("tr")
                                .eq(i)
                                .select("td")
                                .eq(j)
                                .text()
                        stats[i][j - 1] = text
                    }
                }
                totalCount = data.select("tbody")
                        .select("tr")
                        .eq(31)
                        .select("td")
                        .eq(1)
                        .text()
            } catch (e: Exception) {
                println("Error")
                e.printStackTrace()
            }
            return null
        }
    }
}