package com.example.seminarapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.seminarapp.databinding.ActivityFormPendaftaranBinding
import com.google.android.material.snackbar.Snackbar

class FormPendaftaranActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormPendaftaranBinding

    private val daftarSeminar = listOf(
        "-- Pilih Seminar --",
        "Seminar Nasional Kecerdasan Buatan 2025",
        "Workshop Pengembangan Aplikasi Mobile",
        "Seminar Keamanan Siber & Ethical Hacking",
        "Seminar Transformasi Digital Industri 4.0",
        "Workshop UI/UX Design Modern",
        "Seminar Big Data & Machine Learning",
        "Seminar Cloud Computing & DevOps"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Form Pendaftaran"
            setDisplayHomeAsUpEnabled(true)
        }

        setupSpinner()
        setupRealTimeValidation()
        setupClickListeners()

        // Pre-fill nama dari login (opsional)
        val userName = intent.getStringExtra("USER_NAME") ?: ""
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        if (userName.isNotEmpty()) binding.etNama.setText(userName.replaceFirstChar { it.uppercase() })
        if (userEmail.isNotEmpty()) binding.etEmail.setText(userEmail)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarSeminar)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSeminar.adapter = adapter
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
                when {
                    email.isEmpty() -> binding.tilEmail.error = null
                    !email.contains("@") -> binding.tilEmail.error = "Email harus mengandung '@'"
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                        binding.tilEmail.error = "Format email tidak valid"
                    else -> binding.tilEmail.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etNoHp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val noHp = s.toString()
                when {
                    noHp.isEmpty() -> binding.tilNoHp.error = null
                    !noHp.all { it.isDigit() } -> binding.tilNoHp.error = "Nomor HP hanya boleh angka"
                    !noHp.startsWith("08") -> binding.tilNoHp.error = "Nomor HP harus diawali dengan 08"
                    noHp.length < 10 -> binding.tilNoHp.error = "Nomor HP minimal 10 digit"
                    noHp.length > 13 -> binding.tilNoHp.error = "Nomor HP maksimal 13 digit"
                    else -> binding.tilNoHp.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            if (validateAllInput()) {
                showConfirmationDialog()
            }
        }
    }

    private fun validateAllInput(): Boolean {
        var isValid = true

        val nama = binding.etNama.text.toString().trim()
        if (nama.isEmpty()) {
            binding.tilNama.error = "Nama wajib diisi"
            isValid = false
        } else if (nama.length < 3) {
            binding.tilNama.error = "Nama minimal 3 karakter"
            isValid = false
        }

        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.tilEmail.error = "Email wajib diisi"
            isValid = false
        } else if (!email.contains("@")) {
            binding.tilEmail.error = "Email harus mengandung '@'"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Format email tidak valid"
            isValid = false
        }

        val noHp = binding.etNoHp.text.toString().trim()
        if (noHp.isEmpty()) {
            binding.tilNoHp.error = "Nomor HP wajib diisi"
            isValid = false
        } else if (!noHp.startsWith("08")) {
            binding.tilNoHp.error = "Nomor HP harus diawali dengan 08"
            isValid = false
        } else if (noHp.length < 10 || noHp.length > 13) {
            binding.tilNoHp.error = "Nomor HP harus 10-13 digit"
            isValid = false
        }

        if (!binding.rbLakiLaki.isChecked && !binding.rbPerempuan.isChecked) {
            Snackbar.make(binding.root, "Jenis kelamin wajib dipilih", Snackbar.LENGTH_SHORT).show()
            isValid = false
        }

        if (binding.spinnerSeminar.selectedItemPosition == 0) {
            Snackbar.make(binding.root, "Pilih seminar terlebih dahulu", Snackbar.LENGTH_SHORT).show()
            isValid = false
        }

        if (!binding.checkboxPersetujuan.isChecked) {
            Snackbar.make(binding.root, "Anda harus menyetujui pernyataan terlebih dahulu", Snackbar.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Pendaftaran")
            .setMessage("Apakah data yang Anda isi sudah benar?")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Ya") { _, _ ->
                navigateToHasil()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToHasil() {
        val jenisKelamin = if (binding.rbLakiLaki.isChecked) "Laki-laki" else "Perempuan"
        val seminarDipilih = binding.spinnerSeminar.selectedItem.toString()

        val intent = Intent(this, HasilActivity::class.java).apply {
            putExtra("NAMA", binding.etNama.text.toString().trim())
            putExtra("EMAIL", binding.etEmail.text.toString().trim())
            putExtra("NO_HP", binding.etNoHp.text.toString().trim())
            putExtra("JENIS_KELAMIN", jenisKelamin)
            putExtra("SEMINAR", seminarDipilih)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}