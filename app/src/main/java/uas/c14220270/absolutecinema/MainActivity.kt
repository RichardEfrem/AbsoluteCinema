package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Simulasikan login berhasil (gunakan kondisi autentikasi yang sesuai)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        loginButton.setOnClickListener {
            // Setelah login berhasil, pindah ke HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Tutup MainActivity agar tidak kembali ke halaman login
        }
    }
}
