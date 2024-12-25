package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _btnBack = findViewById<FloatingActionButton>(R.id.btnBack)

        _btnBack.setOnClickListener{
            val intent = Intent(this@LoginPage, MainActivity::class.java)
            startActivity(intent)
        }

        val _etEmail = findViewById<EditText>(R.id.etEmailLogin)
        val _etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val _btnSignIn = findViewById<Button>(R.id.btnSignIn)

        _btnSignIn.setOnClickListener{
            val email = _etEmail.text.toString()
            val password = _etEmail.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}