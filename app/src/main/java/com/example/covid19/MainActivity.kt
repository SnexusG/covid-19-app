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

        checkXrayButton.setOnClickListener {
            scanXray()
        }
    }

    fun test(){
        val intent = Intent(this, SymptomTest::class.java)
        startActivity(intent)
    }

    fun scanXray(){
        val intent = Intent(this, CheckXray::class.java)
        startActivity(intent)
    }
}