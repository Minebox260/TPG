package com.example.tpg.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.example.tpg.R
import com.example.tpg.data.DataProvider
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)

        val btnLogin: Button = findViewById(R.id.login_button)

        btnLogin.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password, sharedPreferences)
            } else {
                Toast.makeText(this, getString(R.string.connexion_manquant), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String, sharedPreferences: SharedPreferences) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DataProvider.supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, getString(R.string.connexion_reussie), Toast.LENGTH_SHORT).show()
                    val editor = sharedPreferences.edit()
                    editor.putString("userId", DataProvider.supabase.auth.retrieveUserForCurrentSession(false).id)
                    editor.apply()
                    val myIntent = Intent(this@LoginActivity, ScannerActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }

           } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, getString(R.string.connexion_echoue), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myIntent = Intent(this, SettingsActivity::class.java)
        startActivity(myIntent)

        return super.onOptionsItemSelected(item)
    }
}