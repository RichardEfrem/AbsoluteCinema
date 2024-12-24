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

class RegisterPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

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
        val _etPasswordRegister = findViewById<EditText>(R.id.etPasswordRegister)
        val _etPasswordRegisterConfirmation = findViewById<EditText>(R.id.etPasswordRegisterConfirmation)
        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)

        auth = Firebase.auth

        _btnSignUp.setOnClickListener{
            val email = _etEmailRegister.text.toString()
            val password = _etPasswordRegister.text.toString()
            val passwordConfirmation = _etPasswordRegisterConfirmation.text.toString()

            if (email.isBlank() || password.isBlank() || passwordConfirmation.isBlank()) {
                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            }

            if (password != passwordConfirmation) {
                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                    .show()
            }

            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Successfully Signed Up, Login With Your Created Account", Toast.LENGTH_LONG).show()
                intent = Intent(this@RegisterPage, LoginPage::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}