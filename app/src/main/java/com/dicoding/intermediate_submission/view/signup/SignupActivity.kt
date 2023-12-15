package com.dicoding.intermediate_submission.view.signup

import MainViewModel
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate_submission.R
import com.dicoding.intermediate_submission.databinding.ActivitySignupBinding
import com.dicoding.intermediate_submission.view.ViewModelFactory
import com.dicoding.intermediate_submission.view.login.LoginActivity
import com.dicoding.intermediate_submission.view.main.MainActivity
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    private lateinit var passwordEditText: PasswordEditText
    private lateinit var emailEditText: EmailEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

         viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        ).get(SignupViewModel::class.java)

        setupView()
        setupAction()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        binding.emailEditText;
        binding.passwordEditText;
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            lifecycleScope.launch {
                try {
                    viewModel.registerUser(name, email, password) { response ->
                        if (response.error == true) {Toast.makeText(this@SignupActivity, "Pendaftaran gagal: ${response.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@SignupActivity, "Pendaftaran berhasil: ${response.message}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@SignupActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}