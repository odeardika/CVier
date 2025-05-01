package com.dicoding.cvierapp.view.result

import ResultRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.cvierapp.response.Data
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultViewModel(private val repository: ResultRepository) : ViewModel() {

    private val _percentageText = MutableLiveData<String>()
    val percentageText: LiveData<String> get() = _percentageText

    private val _progressBarValue = MutableLiveData<Int>()
    val progressBarValue: LiveData<Int> get() = _progressBarValue

    private val _improveContent = MutableLiveData<String>()
    val improveContent: LiveData<String> get() = _improveContent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchResultFromAPI() {
        viewModelScope.launch {
            delay(1000)

            val cvResponse = Data(percentage = 50, improve = listOf("Cloud Computing"))

            _percentageText.value = cvResponse.percentage.toString()
            _progressBarValue.value = cvResponse.percentage!!
            _improveContent.value = cvResponse.improve?.joinToString(separator = "\n") ?: ""
        }
    }
}