package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPreferencesManager.init(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser: FirebaseUser? = auth.currentUser
        val email = currentUser?.email
        val Username = SharedPreferencesManager.getString("username")
        val role = SharedPreferencesManager.getString("role")

        val _tvProfileName = findViewById<TextView>(R.id.profile_name)
        val _tvEmail = findViewById<TextView>(R.id.email_address)

        _tvProfileName.setText(Username.toString())
        _tvEmail.setText(email.toString())

        val _btnMyTicket = findViewById<LinearLayout>(R.id.my_ticket)

        val _btnQrScanner = findViewById<LinearLayout>(R.id.qr_scanner)
        if (role != "admin") {
            _btnQrScanner.visibility = Button.GONE
        }

        val _btnLogout = findViewById<Button>(R.id.logout_button)
        _btnLogout.setOnClickListener{
            SharedPreferencesManager.clearAll()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}