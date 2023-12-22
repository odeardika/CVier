package com.dicoding.cvierapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.cvierapp.R
import com.dicoding.cvierapp.databinding.ActivityRegisterBinding
import com.dicoding.cvierapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        setContentView(registerBinding.root)

        setAction()
    }

    private fun setAction() {

        registerBinding.registerUsernameEdit.usernameMessage.observe(this) { it ->
            registerBinding.registerUsernameLayout.error = it
        }

        registerBinding.registerEmailEdit.emailMessage.observe(this) { it ->
            registerBinding.registerEmailLayout.error = it
        }

        registerBinding.registerPassEdit.passMessage.observe(this) { it ->
            registerBinding.registerPassLayout.error = it
        }

        registerBinding.registerPassConfirmEdit.referencePassEditText = registerBinding.registerPassEdit

        registerBinding.registerPassConfirmEdit.confirmPass.observe(this) { it ->
            registerBinding.registerPassConfirmLayout.error = it
        }

        registerBinding.registerButton.setOnClickListener {

            if (registerBinding.registerUsernameEdit.text.isNullOrEmpty()) registerBinding.registerUsernameLayout.error = getString(R.string.must_filled)
            if (registerBinding.registerEmailEdit.text.isNullOrEmpty()) registerBinding.registerEmailLayout.error = getString(R.string.must_filled)
            if (registerBinding.registerPassEdit.text.isNullOrEmpty()) registerBinding.registerPassLayout.error = getString(R.string.must_filled)
            if (registerBinding.registerPassConfirmEdit.text.isNullOrEmpty()) registerBinding.registerPassConfirmLayout.error = getString(R.string.must_filled)

            val allFieldsNotEmpty = listOf(
                registerBinding.registerUsernameEdit,
                registerBinding.registerEmailEdit,
                registerBinding.registerPassEdit,
                registerBinding.registerPassConfirmEdit
            ).all { it.text?.isNotEmpty() == true }

            if (allFieldsNotEmpty) {
                registerViewModel.registerUser(
                    registerBinding.registerUsernameEdit.text.toString(),
                    registerBinding.registerEmailEdit.text.toString(),
                    registerBinding.registerPassEdit.text.toString()
                ) { isSuccess, message ->
                    if (isSuccess) {
                        showDialog(dialogMessage = getString(R.string.register_success))
                    } else {
                        Toast.makeText(this, getString(R.string.register_failed, message), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, RegisterActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        registerBinding.registerToLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showDialog(dialogMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Notification")
            setMessage(dialogMessage)
            setPositiveButton("OK") {_, _ ->
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            registerBinding.apply {
                registerProgressbar.visibility = View.VISIBLE
                registerLayout.visibility = View.GONE
            }
        } else {
            registerBinding.apply {
                registerProgressbar.visibility = View.GONE
                registerLayout.visibility = View.VISIBLE
            }
        }
    }
}