package com.dicoding.cvierapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.cvierapp.R
import com.dicoding.cvierapp.databinding.ActivityMainBinding
import com.dicoding.cvierapp.view.insert.InsertActivity
import com.dicoding.cvierapp.view.profile.ProfileActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContentView(mainBinding.root)

        setAction()
    }

    private fun setAction() {

        mainViewModel.usernameLiveData.observe(this) { username ->
            mainBinding.mainUsername.text = getString(R.string.main_username, username)
        }

        mainViewModel.profileLiveData.observe(this) { profile ->
            Glide.with(this)
                .load(profile)
                .into(mainBinding.mainPhoto)
        }

        mainBinding.mainButton.setOnClickListener {
            startActivity(Intent(this, InsertActivity::class.java))
        }
        mainBinding.mainPhoto.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}