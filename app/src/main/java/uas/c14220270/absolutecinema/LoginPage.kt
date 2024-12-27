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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPage : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

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
            val password = _etPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                SharedPreferencesManager.init(this)
                fetchUserData(email)
            }.addOnFailureListener{
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserData(email: String) {
        db.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username").toString()
                    val role = document.getString("role").toString()

                    // Save the data for global access
                    SharedPreferencesManager.saveString("username", username)
                    SharedPreferencesManager.saveString("role", role)

                    Toast.makeText(this, "Welcome $username", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginPage, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
    }
}