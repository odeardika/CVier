package com.dicoding.cvierapp.view.profile

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
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val usernameLiveData = MutableLiveData<String?>()
    val profileLiveData = MutableLiveData<String>()
    val emailLiveData = MutableLiveData<String?>()

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
            _isLoading.value = true
            val userId = auth.currentUser?.uid
            val userReference = userId?.let { databaseReference.child("users").child(it) }

            userReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val username = snapshot.child("username").getValue(String::class.java)
                        val photoProfile = snapshot.child("profilePhotoUrl").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)

                        usernameLiveData.postValue(username)
                        profileLiveData.postValue(photoProfile.toString())
                        emailLiveData.postValue(email)
                    }
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}