package com.dicoding.cvierapp.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.cvierapp.R
import com.dicoding.cvierapp.databinding.ActivityLoginBinding
import com.dicoding.cvierapp.view.main.MainActivity
import com.dicoding.cvierapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(loginBinding.root)

        setAction()
    }

    private fun setAction() {

        loginBinding.loginEmailEdit.emailMessage.observe(this) {
            loginBinding.loginEmailLayout.error = it
        }

        loginBinding.loginPassEdit.passMessage.observe(this) {
            loginBinding.loginPassLayout.error = it
        }

        loginBinding.loginButton.setOnClickListener {

            if (loginBinding.loginEmailEdit.text.isNullOrEmpty()) loginBinding.loginEmailLayout.error = getString(R.string.must_filled)
            if (loginBinding.loginPassEdit.text.isNullOrEmpty()) loginBinding.loginPassLayout.error = getString(R.string.must_filled)

            if (loginBinding.loginEmailEdit.text?.isNotEmpty() == true && loginBinding.loginPassEdit.text?.isNotEmpty() == true) {
                loginViewModel.loginUser(
                    loginBinding.loginEmailEdit.text.toString(),
                    loginBinding.loginPassEdit.text.toString()
                ) { isSuccess, message ->
                    if (isSuccess) {
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, getString(R.string.login_failed, message), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        loginBinding.loginToRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loginBinding.apply {
                loginProgressbar.visibility = View.VISIBLE
                loginLayout.visibility = View.GONE
            }
        } else {
            loginBinding.apply {
                loginProgressbar.visibility = View.GONE
                loginLayout.visibility = View.VISIBLE
            }
        }
    }
}