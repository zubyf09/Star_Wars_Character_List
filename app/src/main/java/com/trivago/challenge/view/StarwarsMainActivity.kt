package com.trivago.challenge.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trivago.challenge.R
import com.trivago.challenge.view.extensions.Activities
import com.trivago.challenge.view.extensions.intentTo

class StarwarsMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starwars_main)

        //Open the Characters activity on app launch
        startActivity(intentTo(Activities.Characters))
        finish()



    }
}
