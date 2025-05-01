package com.dicoding.cvierapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = false
    }

    fun registerUser(username: String, email: String, password: String, onComplete: (Boolean, String?) -> Unit ) {
        viewModelScope.launch {
            _isLoading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        Log.d(TAG, "User ID after successful registration: $userId")
                        userId?.let {
                            val userReference = database.child("users").child(it)
                            val photoProfile = "https://i.stack.imgur.com/l60Hf.png"
                            userReference.child("username").setValue(username)
                            userReference.child("email").setValue(email)
                            userReference.child("profilePhotoUrl").setValue(photoProfile)
                            onComplete(true, null)
                        }
                    } else {
                        onComplete(false, task.exception?.message)
                    }
                    _isLoading.value = false
                }
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}