package com.dicoding.cvierapp.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.cvierapp.R
import com.dicoding.cvierapp.databinding.ActivityProfileBinding
import com.dicoding.cvierapp.view.ViewModelFactory
import com.dicoding.cvierapp.view.editprofile.EditProfileActivity
import com.dicoding.cvierapp.view.main.MainActivity
import com.dicoding.cvierapp.view.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        profileViewModel = getViewModel(this)
        setContentView(profileBinding.root)

        auth = FirebaseAuth.getInstance()

        setAction()
    }

    private fun setAction() {
        profileBinding.profileBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        profileBinding.profileLogout.setOnClickListener {
            showDialog(dialogMessage = getString(R.string.profile_logout_notif))
        }
        profileViewModel.usernameLiveData.observe(this) { username ->
            profileBinding.profileUsernameContent.text = username
        }

        profileViewModel.profileLiveData.observe(this) { profile ->
            Glide.with(this)
                .load(profile)
                .into(profileBinding.profileImage)
        }

        profileViewModel.emailLiveData.observe(this) { email ->
            profileBinding.profileEmailContent.text = email
        }

        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        profileBinding.profileEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    private fun showDialog(dialogMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Notification")
            setMessage(dialogMessage)
            setPositiveButton("Yes") { _, _ ->
                auth.signOut()
                val intent = Intent(this@ProfileActivity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun getViewModel(context: Context): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            profileBinding.profileProgressbar.visibility = View.VISIBLE
            profileBinding.profileContent.visibility = View.GONE
        } else {
            profileBinding.profileProgressbar.visibility = View.GONE
            profileBinding.profileContent.visibility = View.VISIBLE
        }
    }
}