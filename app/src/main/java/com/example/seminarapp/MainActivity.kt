package com.example.seminarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.seminarapp.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding harus paling atas
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Status bar transparan (setelah setContentView)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }

        // Tambahkan ini agar konten tidak tertutup navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        val userName = intent.getStringExtra("USER_NAME") ?: "Pengguna"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        binding.tvWelcome.text = "Selamat Datang"
        binding.tvUserName.text = userName.replaceFirstChar { it.uppercase() }
        binding.tvUserEmail.text = userEmail

        binding.btnDaftarSeminar.setOnClickListener {
            val intent = Intent(this, FormPendaftaranActivity::class.java)
            intent.putExtra("USER_NAME", userName)
            intent.putExtra("USER_EMAIL", userEmail)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}