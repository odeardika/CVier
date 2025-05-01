package com.dicoding.cvierapp.view

import ResultRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.cvierapp.repository.CVRepository
import com.dicoding.cvierapp.view.editprofile.EditProfileViewModel
import com.dicoding.cvierapp.view.insert.InsertViewModel
import com.dicoding.cvierapp.view.login.LoginViewModel
import com.dicoding.cvierapp.view.main.MainViewModel
import com.dicoding.cvierapp.view.profile.ProfileViewModel
import com.dicoding.cvierapp.view.register.RegisterViewModel
import com.dicoding.cvierapp.view.result.ResultViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val cvRepository: CVRepository,
    private val resultRepository: ResultRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel() as T
        } else if (modelClass.isAssignableFrom(InsertViewModel::class.java)) {
            return InsertViewModel(cvRepository) as T
        } else if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(resultRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel() as T
        } else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    CVRepository(),
                    ResultRepository()
                )
            }.also { INSTANCE = it }
    }
}