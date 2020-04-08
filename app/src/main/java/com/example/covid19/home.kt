package com.example.covid19

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navbar)
        bottomNavigationView.setOnNavigationItemSelectedListener(btmnavListner)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
    }

    private val btmnavListner = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        var selected: Fragment = HomeFragment()
        when (menuItem.itemId) {
            R.id.navigation_home -> selected = HomeFragment()
            R.id.navigation_chat -> selected = ChatFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container
                , selected).commit()
        true
    }
}