package uas.c14220270.absolutecinema

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var imageCarousel: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private val slideRunnable = object : Runnable {
        override fun run() {
            val currentItem = imageCarousel.currentItem
            val nextItem = if (currentItem < images.size - 1) currentItem + 1 else 0
            imageCarousel.currentItem = nextItem
            handler.postDelayed(this, 3000) // Auto-slide every 3 seconds
        }
    }

    private val images = listOf(
        R.drawable.sonic3,
        R.drawable.moana2new,
        R.drawable.mufasa,
        R.drawable.venom3,
        R.drawable.deadpoolwolverine
    )

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
            return
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

            
        }

        imageCarousel = findViewById<ViewPager2>(R.id.imageCarousel)
        val adapter = LoginCarouselAdapter(images)
        imageCarousel.adapter = adapter

        handler.postDelayed(slideRunnable, 3000)

        val _btnSignIn = findViewById<Button>(R.id.signInButton)
        val _btnSignUp = findViewById<Button>(R.id.signUpButton)

        _btnSignUp.setOnClickListener{
            val intent = Intent(this@MainActivity,RegisterPage::class.java)
            startActivity(intent)
        }

        _btnSignIn.setOnClickListener{
            val intent = Intent(this@MainActivity,LoginPage::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(slideRunnable)
    }
}