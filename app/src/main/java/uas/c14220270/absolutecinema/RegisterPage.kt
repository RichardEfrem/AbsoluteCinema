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

class RegisterPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _btnBack = findViewById<FloatingActionButton>(R.id.btnBack)

        _btnBack.setOnClickListener{
            val intent = Intent(this@RegisterPage, MainActivity::class.java)
            startActivity(intent)
        }

        val _etEmailRegister = findViewById<EditText>(R.id.etEmailRegister)
        val _etUsername = findViewById<EditText>(R.id.etUsername)
        val _etPasswordRegister = findViewById<EditText>(R.id.etPasswordRegister)
        val _etPasswordRegisterConfirmation = findViewById<EditText>(R.id.etPasswordRegisterConfirmation)
        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        _btnSignUp.setOnClickListener{
            val email = _etEmailRegister.text.toString()
            val password = _etPasswordRegister.text.toString()
            val passwordConfirmation = _etPasswordRegisterConfirmation.text.toString()
            val username = _etUsername.text.toString()

            if (email.isBlank() || password.isBlank() || passwordConfirmation.isBlank() || username.isBlank()) {
                Toast.makeText(this, "Email / Password / Username can't be blank", Toast.LENGTH_SHORT).show()
            }

            if (password != passwordConfirmation) {
                Toast.makeText(this, "Password and Password Confirmation do not match", Toast.LENGTH_SHORT)
                    .show()
            }

            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid
                val userData = hashMapOf(
                    "email" to email,
                    "role" to "user",
                    "username" to username
                )

                userId?.let {
                    db.collection("users").document(email).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfully Signed Up! Login With Your Created Account", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@RegisterPage, LoginPage::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to Save User Data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Sign Up Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}