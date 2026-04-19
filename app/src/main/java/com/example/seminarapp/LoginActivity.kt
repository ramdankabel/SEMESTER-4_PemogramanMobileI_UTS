package com.example.seminarapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.seminarapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Data user hardcode
    private val validUsers = mapOf(
        "admin@email.com" to "admin123",
        "user@email.com" to "user123",
        "mahasiswa@utb.ac.id" to "mahasiswa"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRealTimeValidation()
        setupClickListeners()
    }

    private fun setupRealTimeValidation() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.tilEmail.error = "Format email tidak valid"
                } else {
                    binding.tilEmail.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && s.toString().length < 6) {
                    binding.tilPassword.error = "Password minimal 6 karakter"
                } else {
                    binding.tilPassword.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                // Cek hardcode dulu
                val hardcodeValid = validUsers[email] == password

                // Cek SharedPreferences (user yang sudah register)
                val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                val savedPassword = sharedPref.getString("email_$email", null)
                val savedNama = sharedPref.getString("nama_$email", null)
                val sharedPrefValid = savedPassword == password

                if (hardcodeValid || sharedPrefValid) {
                    val userName = savedNama ?: email.substringBefore("@")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    intent.putExtra("USER_NAME", userName)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "Email atau password salah!", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email wajib diisi"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Format email tidak valid"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password wajib diisi"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "Password minimal 6 karakter"
            isValid = false
        }

        return isValid
    }
}