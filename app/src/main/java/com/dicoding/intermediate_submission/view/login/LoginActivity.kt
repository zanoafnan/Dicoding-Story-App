package com.dicoding.intermediate_submission.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.intermediate_submission.data.pref.UserModel
import com.dicoding.intermediate_submission.databinding.ActivityLoginBinding
import com.dicoding.intermediate_submission.view.ViewModelFactory
import com.dicoding.intermediate_submission.view.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        loginViewModel.isLoading.observe(this) {
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
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                // Perform the login
                lifecycleScope.launch {
                    try {
                        val response = loginViewModel.login(email, password)

                        if (response.error) {
                            // Login failed, show an error message
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Login Failed")
                                setMessage(response.message ?: "Unable to login.")
                                setPositiveButton("OK") { _, _ -> }
                                create()
                                show()
                            }
                        } else {
                            // Login successful, show a success message and navigate to the main activity
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Login Berhasil")
                                setMessage("You have successfully logged in.")
                                setPositiveButton("OK") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    } catch (e: Exception) {
                        // Handle network or other exceptions
                        e.printStackTrace()
                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle("Login Gagal")
                            setMessage("Email atau Password tidak valid. Silakan coba lagi")
                            setPositiveButton("OK") { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            } else {
                // Handle empty email or password fields
                AlertDialog.Builder(this).apply {
                    setTitle("Validation Error")
                    setMessage("Please enter both email and password.")
                    setPositiveButton("OK") { _, _ -> }
                    create()
                    show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
