package com.example.seminarapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.seminarapp.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRealTimeValidation()
        setupClickListeners()
    }

    private fun setupRealTimeValidation() {
        binding.etNama.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && s.toString().length < 3) {
                    binding.tilNama.error = "Nama minimal 3 karakter"
                } else {
                    binding.tilNama.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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
                // Cek konfirmasi password juga
                val konfirmasi = binding.etKonfirmasiPassword.text.toString()
                if (konfirmasi.isNotEmpty() && s.toString() != konfirmasi) {
                    binding.tilKonfirmasiPassword.error = "Password tidak cocok"
                } else {
                    binding.tilKonfirmasiPassword.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etKonfirmasiPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.etPassword.text.toString()
                if (s.toString() != password) {
                    binding.tilKonfirmasiPassword.error = "Password tidak cocok"
                } else {
                    binding.tilKonfirmasiPassword.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val konfirmasi = binding.etKonfirmasiPassword.text.toString()

            if (validateInput(nama, email, password, konfirmasi)) {
                // Simpan ke SharedPreferences
                val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                sharedPref.edit()
                    .putString("email_$email", password)
                    .putString("nama_$email", nama)
                    .apply()

                Snackbar.make(binding.root, "Registrasi berhasil! Silakan login.", Snackbar.LENGTH_LONG).show()
                binding.root.postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 1500)
            }
        }
        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun validateInput(nama: String, email: String, password: String, konfirmasi: String): Boolean {
        var isValid = true

        if (nama.isEmpty()) {
            binding.tilNama.error = "Nama wajib diisi"
            isValid = false
        } else if (nama.length < 3) {
            binding.tilNama.error = "Nama minimal 3 karakter"
            isValid = false
        }

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

        if (konfirmasi.isEmpty()) {
            binding.tilKonfirmasiPassword.error = "Konfirmasi password wajib diisi"
            isValid = false
        } else if (konfirmasi != password) {
            binding.tilKonfirmasiPassword.error = "Password tidak cocok"
            isValid = false
        }

        return isValid
    }
}