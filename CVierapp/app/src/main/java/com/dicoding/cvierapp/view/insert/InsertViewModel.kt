package com.dicoding.cvierapp.view.insert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.cvierapp.repository.CVRepository
import com.dicoding.cvierapp.response.CVResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class InsertViewModel(private val repository: CVRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val cvResponse = MutableLiveData<Result<CVResponse>>()

    init {
        _isLoading.value = false
    }

    fun uploadCV(file: File, jobIndex: Int) {
        _isLoading.value = true
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val jobType = jobIndex.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                cvResponse.value = repository.postCV(filePart, jobType)
            } finally {
                _isLoading.value = false
            }
        }
    }
}