package com.dicoding.cvierapp.view.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.cvierapp.R

class ConfirmPassEditText : AppCompatEditText {

    private var _confirmPass = MutableLiveData<String?>()
    var confirmPass: LiveData<String?> = _confirmPass

    var referencePassEditText: PassEditText? = null

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
                _confirmPass.value = isValidConfirmPass(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun isValidConfirmPass(s: String): String? {
        val originalPassword = referencePassEditText?.text.toString()
        if (originalPassword != null && s != originalPassword) {
            return resources.getString(R.string.confirm_pass_alert)
        }
        return null
    }
}