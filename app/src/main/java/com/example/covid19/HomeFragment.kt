package com.example.covid19

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import org.jsoup.Jsoup

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        QuizButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, SymptomTest::class.java)
            startActivity(intent)
        })

        xrayButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, CheckXray::class.java)
            startActivity(intent)
        })

        logOutBtn.setOnClickListener(View.OnClickListener {
            if (mAuth!!.currentUser != null) {
                mAuth!!.signOut()
                val intent = Intent(activity, Login::class.java)
                startActivity(intent)
            }
        })

        val content = Content_Stats()
        content.execute()
        val content2 = Content_News()
        content2.execute()
    }

    private inner class Content_News : AsyncTask<Void?, Void?, Void?>() {

        private val news = arrayOfNulls<String>(5)

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            println("NEWS")
            for (i in news) {
                println(i)
            }
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
                    news[i - 1] = text
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


    }

    private inner class Content_Stats : AsyncTask<Void?, Void?, Void?>() {


        private val stats = Array(31) { arrayOfNulls<String>(4) }
        private var totalCount: String? = null

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