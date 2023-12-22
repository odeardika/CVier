package com.dicoding.cvierapp.view.editprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.cvierapp.databinding.ActivityEditprofileBinding
import com.dicoding.cvierapp.view.ViewModelFactory
import com.dicoding.cvierapp.view.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class EditProfileActivity: AppCompatActivity() {

    private lateinit var editProfileBinding: ActivityEditprofileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var editProfileViewModel: EditProfileViewModel
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditprofileBinding.inflate(layoutInflater)
        editProfileViewModel = getViewModel(this)
        setContentView(editProfileBinding.root)

        auth = FirebaseAuth.getInstance()

        setAction()
    }

    private fun setAction() {
        editProfileBinding.editprofileBackButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        editProfileBinding.editprofileImage.setOnClickListener {
            openImageChooser()
        }

        editProfileViewModel.profileLiveData.observe(this) { profile ->
            Glide.with(this)
                .load(profile)
                .into(editProfileBinding.editprofileImage)
        }

        editProfileViewModel.usernameLiveData.observe(this) { username ->
            editProfileBinding.editprofileUsernameContent.setText(username)
        }

        editProfileViewModel.emailLiveData.observe(this) { email ->
            editProfileBinding.editprofileEmailContent.setText(email)
        }

        editProfileBinding.editprofileButton.setOnClickListener{
            val newUsername = editProfileBinding.editprofileUsernameContent.text.toString()
            val newEmail = editProfileBinding.editprofileEmailContent.text.toString()
            val imageUri: Uri? = selectedImageUri

            if (imageUri != null) {
                editProfileViewModel.updateProfileWithImage(newUsername, newEmail, imageUri)
            } else {
                editProfileViewModel.updateProfile(newUsername, newEmail)
            }

            val intent = Intent(this, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        editProfileViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun getViewModel(context: Context): EditProfileViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private var selectedImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .into(editProfileBinding.editprofileImage)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            editProfileBinding.editprofileProgressbar.visibility = View.VISIBLE
            editProfileBinding.editprofileContent.visibility = View.GONE
        } else {
            editProfileBinding.editprofileProgressbar.visibility = View.GONE
            editProfileBinding.editprofileContent.visibility = View.VISIBLE
        }
    }
}