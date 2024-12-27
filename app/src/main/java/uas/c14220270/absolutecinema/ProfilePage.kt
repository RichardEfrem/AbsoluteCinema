package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfilePage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPreferencesManager.init(this)
        auth = FirebaseAuth.getInstance()
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