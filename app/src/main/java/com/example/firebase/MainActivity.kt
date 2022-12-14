package com.example.firebase

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var createUserButton: Button

    private lateinit var pd: AlertDialog
    private lateinit var progressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = Firebase.auth
        firebaseAuth.setLanguageCode("ka")

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        createUserButton = findViewById(R.id.create_user_button)

        //progressDialog-ის შემცვლელივით
        val layout = LinearLayout(this)

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        progressText = TextView(this)

        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE

        val params1 = LinearLayout.LayoutParams(100, 100)
        params1.setMargins(32)

        val params2 = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params2.gravity = Gravity.CENTER
        params2.setMargins(32)

        layout.addView(progressBar, params1)
        layout.addView(progressText, params2)

        val builder = AlertDialog.Builder(this)
        builder.setView(layout)
        pd = builder.create()

        // კლიკ ლისენერი
        createUserButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = emailEditText.text.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 7) {
                progressText.text = "მიმდინარეობს რეგისტრაცია..."
                pd.show()
                createUser(email, password)
            }
        }
    }

    private fun createUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            pd.dismiss()
            if (task.isSuccessful) {
                Toast.makeText(this, "წარმატებით შეიქმნა!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, task.exception?.localizedMessage.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}