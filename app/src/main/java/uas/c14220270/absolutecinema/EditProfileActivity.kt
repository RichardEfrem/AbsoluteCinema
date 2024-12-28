package uas.c14220270.absolutecinema


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        val editName = findViewById<EditText>(R.id.edit_name)
        val editPhone = findViewById<EditText>(R.id.edit_phone)
        val editEmail = findViewById<EditText>(R.id.edit_email)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val name = editName.text.toString().trim()
            val phone = editPhone.text.toString().trim()
            val email = editEmail.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }
}
