package com.dicoding.cvierapp.view.editprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class EditProfileViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference

    val usernameLiveData = MutableLiveData<String?>()
    val emailLiveData = MutableLiveData<String?>()
    val profileLiveData = MutableLiveData<String>()

    init {
        _isLoading.value = false
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                fetchData()
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            val userReference = userId?.let { databaseReference.child("users").child(it) }

            userReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val email = snapshot.child("email").getValue(String::class.java)
                        val photoProfile = snapshot.child("profilePhotoUrl").getValue(String::class.java)
                        val username = snapshot.child("username").getValue(String::class.java)


                        usernameLiveData.postValue(username)
                        emailLiveData.postValue(email)
                        profileLiveData.postValue(photoProfile.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    fun updateProfile(username: String, email: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            val userReference = userId?.let { databaseReference.child("users").child(it) }

            userReference?.child("username")?.setValue(username)
            userReference?.child("email")?.setValue(email)
        }
    }

    fun updateProfileWithImage(username: String, email: String, imageUri: Uri) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            val userReference = userId?.let { databaseReference.child("users").child(it) }
            val profileImage = storageReference.child("profile_images/$userId/${System.currentTimeMillis()}.jpg")
            if (userId != null) {
                profileImage.putFile(imageUri)
                    .addOnSuccessListener {
                        profileImage.downloadUrl.addOnSuccessListener { uri ->
                            userReference?.child("profilePhotoUrl")?.setValue(uri.toString())
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to upload profile picture: ${e.message}", e)
                    }
            }
            userReference?.child("username")?.setValue(username)
            userReference?.child("email")?.setValue(email)
        }
    }

    companion object {
        private const val TAG = "EditProfileViewModel"
    }
}