package com.example.covid19

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QuizButton.setOnClickListener {
            test()
        }
    }

    fun test(){
        val intent = Intent(this, SymptomTest::class.java)
        startActivity(intent)
    }
}