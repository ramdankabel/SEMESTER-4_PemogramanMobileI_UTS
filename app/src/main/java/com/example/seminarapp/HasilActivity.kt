package com.example.seminarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seminarapp.databinding.ActivityHasilBinding

class HasilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Hasil Pendaftaran"

        val nama = intent.getStringExtra("NAMA") ?: "-"
        val email = intent.getStringExtra("EMAIL") ?: "-"
        val noHp = intent.getStringExtra("NO_HP") ?: "-"
        val jenisKelamin = intent.getStringExtra("JENIS_KELAMIN") ?: "-"
        val seminar = intent.getStringExtra("SEMINAR") ?: "-"

        binding.tvNamaValue.text = nama
        binding.tvEmailValue.text = email
        binding.tvNoHpValue.text = noHp
        binding.tvJenisKelaminValue.text = jenisKelamin
        binding.tvSeminarValue.text = seminar

        binding.btnKembaliUtama.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}