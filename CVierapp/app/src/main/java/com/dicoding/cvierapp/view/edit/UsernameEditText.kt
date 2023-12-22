package com.dicoding.cvierapp.view.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UsernameEditText: AppCompatEditText {
    private var _usernameMessage = MutableLiveData<String?>()
    var usernameMessage: LiveData<String?> = _usernameMessage

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    // Clear the error message
                    setError(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun setError(message: String?) {
        _usernameMessage.value = message
    }
}